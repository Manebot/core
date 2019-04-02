package io.manebot.event.command;

import io.manebot.command.executor.CommandExecutor;
import io.manebot.event.Event;

public abstract class CommandEvent extends Event {
    private final CommandExecutor executor;

    public CommandEvent(Object sender, CommandExecutor executor) {
        super(sender);

        this.executor = executor;
    }

    public CommandExecutor getCommandExecutor() {
        return executor;
    }
}
