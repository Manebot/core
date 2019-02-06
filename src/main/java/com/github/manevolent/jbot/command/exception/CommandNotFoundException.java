package com.github.manevolent.jbot.command.exception;

public class CommandNotFoundException extends CommandExecutionException {
    private final String command;

    public CommandNotFoundException(String command) {
        super(false, "\u2716 CommandMessage not recognized.");

        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
