package io.manebot.event.command;

import io.manebot.chat.ChatMessage;
import io.manebot.command.CommandSender;
import io.manebot.command.executor.CommandExecutor;

public class CommandExecutionEvent extends CommandEvent {
    private final CommandSender commandSender;
    private final ChatMessage chatMessage;

    public CommandExecutionEvent(Object sender,
                                 CommandExecutor executor,
                                 CommandSender commandSender,
                                 ChatMessage chatMessage) {
        super(sender, executor);

        this.commandSender = commandSender;
        this.chatMessage = chatMessage;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
