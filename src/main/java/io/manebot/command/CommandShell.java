package io.manebot.command;

import io.manebot.command.exception.CommandExecutionException;
import io.manebot.user.User;

import java.util.concurrent.Future;

public interface CommandShell {

    User getUser();

    void execute(CommandMessage message) throws CommandExecutionException;

    Future<Boolean> executeAsync(CommandMessage message);

    boolean isOpen();

    void ensureOpen();

}
