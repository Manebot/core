package com.github.manevolent.jbot.event.command;

import com.github.manevolent.jbot.chat.ChatMessage;
import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.executor.CommandExecutor;

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
