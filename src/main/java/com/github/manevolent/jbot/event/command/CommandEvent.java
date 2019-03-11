package com.github.manevolent.jbot.event.command;

import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.event.Event;

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
