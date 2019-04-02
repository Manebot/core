package io.manebot.command;

import io.manebot.command.exception.CommandExecutionException;
import io.manebot.user.User;

import java.util.concurrent.Future;

public interface CommandDispatcher {

    /**
     * Gets a command shell for a specific user.
     * @param user user to get a shell for.
     * @return command shell instance.
     */
    CommandShell getShell(User user);

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
    Future<Boolean> executeAsync(CommandMessage commandMessage);

}