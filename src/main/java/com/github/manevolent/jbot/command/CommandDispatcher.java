package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.concurrent.Future;

public interface CommandDispatcher {

    /**
     * Dispatches a command.
     * @param commandMessage Command message to execute.
     * @throws CommandExecutionException
     */
    void execute(CommandMessage commandMessage) throws CommandExecutionException;

    /**
     * Dispatches a command asynchronously.
     * @param commandMessage Command message to execute.
     * @return Future instance to track execution.
     */
    Future<?> executeAsync(CommandMessage commandMessage);

}