package io.manebot.command.executor.chained;

import io.manebot.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainState {
    private final CommandSender sender;
    private final List<String> arguments;
    private final List<Object> parsedArguments;
    private boolean completed = false;

    public ChainState(CommandSender sender, List<String> arguments, List<Object> parsedArguments) {
        this.sender = sender;
        this.arguments = arguments;
        this.parsedArguments = parsedArguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String next() {
        return size() <= 0 ? null : arguments.get(0);
    }

    public int size() {
        return arguments.size();
    }

    public ChainState clone() {
        return new ChainState(sender, new ArrayList<>(arguments), new ArrayList<>(parsedArguments));
    }

    public ChainState extend(int argumentsUsed, Object... argument) {
        List<String> newArguments = arguments;
        for (; newArguments.size() > 0 && argumentsUsed > 0; argumentsUsed --) {
            newArguments.remove(0);
        }

        List<Object> newParsedArguments = parsedArguments;
        newParsedArguments.addAll(Arrays.asList(argument));

        return new ChainState(sender, newArguments, newParsedArguments);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public List<Object> getParsedArguments() {
        return parsedArguments;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
