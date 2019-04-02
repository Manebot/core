package io.manebot.command.executor.routed;

import io.manebot.command.CommandSender;
import io.manebot.command.exception.CommandExecutionException;
import io.manebot.command.executor.CommandExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class RoutedCommandExecutor implements CommandExecutor {
    private final Map<String, CommandExecutor> commands = new HashMap<>();
    private CommandExecutor defaultRoute, nullRoute = null;

    public RoutedCommandExecutor() {}

    protected Route route(String label, CommandExecutor executor) {
        commands.put(label.toLowerCase().trim(), executor);
        return new Route(executor);
    }

    protected Route setDefaultRoute(CommandExecutor executor) {
        this.defaultRoute = executor;
        return new Route(executor);
    }

    protected Route setNullRoute(CommandExecutor executor) {
        this.nullRoute = executor;
        return new Route(executor);
    }

    private Execution getExecution(String subLabel, String[] args) throws CommandExecutionException {
        if (subLabel == null) {
            if (nullRoute != null)
                return new Execution(args, nullRoute);
            else
                return new Execution(args, defaultRoute); // Last attempt
        }

        Execution execution = new Execution(createSubArguments(args), commands.get(subLabel));

        if (execution.getExecutor() == null)
            execution = new Execution(args, defaultRoute);

        return execution;
    }

    @Override
    public List<String> getHelp(CommandSender sender, String label, String[] args) throws CommandExecutionException {
        String subLabel = args.length > 0 ? args[0].toLowerCase() : null;
        if (subLabel == null) {
            List<String> allHelpLines = new ArrayList<>();

            for (String subCommandLabel : this.commands.keySet()) {
                try {
                    allHelpLines.addAll(
                            this.commands.get(subCommandLabel)
                                    .getHelp(sender, subCommandLabel, createSubArguments(args))
                                    .stream()
                                    .map(x -> subCommandLabel + " " + x)
                                    .collect(Collectors.toList())
                    );
                } catch (CommandExecutionException ex) {
                    // Continue with faults
                    allHelpLines.add(subCommandLabel);
                }
            }

            return allHelpLines;
        }

        Execution execution = new Execution(createSubArguments(args), commands.get(subLabel));
        if (execution.getExecutor() == null) return new ArrayList<>();

        return execution.getExecutor().getHelp(sender, subLabel == null ? label : subLabel, createSubArguments(args))
                .stream()
                .map(x -> subLabel != null ? (subLabel + " " + x) : x)
                .collect(Collectors.toList());
    }

    @Override
    public final void execute(CommandSender sender, String label, String[] args) throws CommandExecutionException {
        String subLabel = args.length > 0 ? args[0].toLowerCase() : null;
        Execution execution = getExecution(subLabel, args);
        if (execution.getExecutor() == null) throw new CommandExecutionException("Cannot process; no default route provided.");

        // Execute
        execution.getExecutor().execute(sender, subLabel, execution.getArguments());
    }

    private static String[] createSubArguments(String[] arguments) {
        String[] subArgs = new String[Math.max(0, arguments.length -1)];

        if (arguments.length > 1) System.arraycopy(arguments, 1, subArgs, 0, subArgs.length);

        return subArgs;
    }

    private class Execution {
        private final String[] args;
        private final CommandExecutor executor;

        private Execution(String[] args, CommandExecutor executor) {
            this.args = args;
            this.executor = executor;
        }

        public String[] getArguments() {
            return args;
        }

        public CommandExecutor getExecutor() {
            return executor;
        }
    }

    public class Route {
        private final CommandExecutor executor;

        public Route(CommandExecutor executor) {
            this.executor = executor;
        }

        public Route alias(String alias) {
            return route(alias, executor);
        }

        public Route asDefaultRoute() {
            return setDefaultRoute(executor);
        }

        public Route asNullRoute() {
            return setNullRoute(executor);
        }
    }
}
