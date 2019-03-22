package com.github.manevolent.jbot.command.executor.chained;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandArgumentException;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;
import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.command.executor.chained.argument.ChainedCommandArgument;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ChainedCommandExecutor implements CommandExecutor {
    private final CommandChain root = new CommandChain(null);

    protected CommandChain withArguments(ChainedCommandArgument... arguments) {
        return withArguments(Arrays.asList(arguments));
    }

    protected CommandChain withArguments(Collection<ChainedCommandArgument> arguments) {
        if (arguments.size() <= 0) throw new IllegalArgumentException("argument length cannot be <= 0");

        CommandChain chain = this.root;
        for (ChainedCommandArgument argument : arguments) chain.addChild(chain = new CommandChain(argument));

        return chain;
    }

    @Override
    public List<String> getHelp(CommandSender sender, String label, String[] args) throws CommandExecutionException {
        List<String> arguments = Arrays.asList(args);

        // Get initial children of this chain
        Map<CommandChain, ChainState> chainMap = new HashMap<>();
        chainMap.put(root, new ChainState(sender, new ArrayList<>(arguments), new ArrayList<>()));

        List<CommandChain> completedChains = new ArrayList<>();

        // While we have more than 1 chain in the queue, try to whittle the chains down. The only difference here
        // is we are open to any priority but NONE, and we want to fan out the chains when we're done.
        while (chainMap.size() > 0) {
            List<PrioritizedChain> prioritizedChainList = new LinkedList<>();

            for (CommandChain child : chainMap.keySet()) {
                ChainState state = chainMap.get(child);
                if (state.next() == null) {
                    // Test chain completed. flesh out this chain and add all its children.
                    // We do this because help is supposed to check by command prefix, and we want ambiguation.

                    List<CommandChain> children = new ArrayList<>(child.getChildren());
                    while (children.size() > 0) {
                        Iterator<CommandChain> childIterator = children.iterator();
                        List<CommandChain> toAppend = new ArrayList<>();

                        while (childIterator.hasNext()) {
                            CommandChain testChild = childIterator.next();
                            if (testChild.getChildren().size() <= 0) {
                                completedChains.add(testChild);
                            } else {
                                toAppend.addAll(testChild.getChildren());
                            }

                            childIterator.remove();
                        }

                        children.addAll(toAppend);
                    }
                    continue;
                }

                // We could drill down farther with these arguments, so let's do that.
                for (CommandChain childChild : child.getChildren()) {
                    ChainState childState = state.clone();
                    ChainPriority chainPriority = childChild.getArgument().cast(childState);
                    if (chainPriority.getOrdinal() < 0) continue; // NONE -- ignore this for help
                    prioritizedChainList.add(new PrioritizedChain(childChild, chainPriority, childState));
                }
            }

            chainMap.clear();

            for (PrioritizedChain chain : prioritizedChainList) chainMap.put(chain.getChain(), chain.getChainState());
        }

        // If we completed no chains but have at least one, we did something wrong.
        if (completedChains.size() <= 0 && root.getChildren().size() > 0)
            throw new CommandArgumentException("Arguments not acceptable; see command help for more information.");

        // Stream chains
        return completedChains
                .stream()
                .map(ChainedCommandExecutor::chainToHelpString)
                .collect(Collectors.toList());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandExecutionException {
        List<String> arguments = Arrays.asList(args);

        // Get initial children of this chain
        Map<CommandChain, ChainState> chainMap = new HashMap<>();
        chainMap.put(root, new ChainState(sender, new ArrayList<>(arguments), new ArrayList<>()));

        List<PrioritizedChain> completedChains = new ArrayList<>(1);

        // While we have more than 1 chain in the queue, try to whittle the chains down.
        while (chainMap.size() > 0) {
            List<PrioritizedChain> prioritizedChainList = new LinkedList<>();

            for (CommandChain child : chainMap.keySet()) {
                ChainState state = chainMap.get(child);

                for (CommandChain childChild : child.getChildren()) {
                    ChainState childState = state.clone();
                    ChainPriority chainPriority = childChild.getArgument().cast(childState);
                    if (chainPriority.getOrdinal() < 0) continue;
                    prioritizedChainList.add(new PrioritizedChain(childChild, chainPriority, childState));
                }
            }

            // Find a chain to use
            prioritizedChainList.sort(
                    (prioritizedChain, t1) ->
                            prioritizedChain.getPriority().getOrdinal() > t1.getPriority().getOrdinal() ? 1 : -1
            );

            chainMap.clear();

            ChainPriority bestPriority = prioritizedChainList
                    .stream()
                    .map(PrioritizedChain::getPriority)
                    .max(Comparator.naturalOrder())
                    .orElse(ChainPriority.NONE);

            for (PrioritizedChain chain : prioritizedChainList) {
                if (chain.getPriority().compareTo(bestPriority) >= 0) {
                    if (chain.getChain().getChildren().size() <= 0) {
                        // Mark it as completed if it has no state arguments left
                        if (chain.getChainState().size() <= 0) completedChains.add(chain);
                        continue;
                    }

                    chainMap.put(chain.getChain(), chain.chainState);
                }
            }
        }

        // Final cleaning of argument chains
        completedChains.sort((b, a) -> Integer.compare(a.priority.getOrdinal(), b.priority.getOrdinal()));

        Iterator<PrioritizedChain> priorityIterator = completedChains.iterator();
        ChainPriority priority = null;
        while (priorityIterator.hasNext()) {
            ChainPriority thisPriority = priorityIterator.next().getPriority();
            if (priority == null) priority = thisPriority;
            else if (priority != thisPriority) priorityIterator.remove();
        }

        if (completedChains.size() <= 0)
            throw new CommandArgumentException("Arguments not acceptable; see command help for more information.");
        else if (completedChains.size() > 1)
            throw new CommandArgumentException("CommandMessage argument disambiguation: " + completedChains.size() + " matches.");

        PrioritizedChain chain = completedChains.get(0);
        if (chain.getChain().getExecutor() == null) throw new CommandExecutionException("No handler for command.");
        chain.getChain().getExecutor().execute(sender, label, chain.getChainState().getParsedArguments().toArray());
    }

    private final class PrioritizedChain {
        private final CommandChain chain;
        private final ChainPriority priority;
        private final ChainState chainState;

        private PrioritizedChain(CommandChain chain, ChainPriority priority, ChainState chainState) {
            this.chain = chain;
            this.priority = priority;
            this.chainState = chainState;
        }

        public CommandChain getChain() {
            return chain;
        }

        public ChainPriority getPriority() {
            return priority;
        }

        public ChainState getChainState() {
            return chainState;
        }
    }

    private static String chainToHelpString(CommandChain chain) {
        CommandChain master = chain;
        if (master == null) return null;

        List<String> elements = new ArrayList<>();
        while (chain != null && chain.getArgument() != null) {
            elements.add(0, chain.getArgument().getHelpString());
            chain = chain.getParent();
        }

        String line = String.join(" ", elements);
        if (master.getDescription() == null)
            return line;
        else
            return line + ": " + master.getDescription();
    }
}
