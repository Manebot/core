package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.chat.ChatMessage;
import com.github.manevolent.jbot.chat.ReceivedChatMessage;

import java.util.Date;

public class CommandMessage implements ReceivedChatMessage {
    private final ReceivedChatMessage chatMessage;
    private final CommandSender sender;

    public CommandMessage(ReceivedChatMessage chatMessage, CommandSender sender) {
        this.chatMessage = chatMessage;
        this.sender = sender;
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    public void delete() throws UnsupportedOperationException {
        chatMessage.delete();
    }

    @Override
    public void edit(ChatMessage message) throws UnsupportedOperationException {
        chatMessage.edit(message);
    }

    @Override
    public String getMessage() {
        return chatMessage.getMessage();
    }

    @Override
    public Date getDate() {
        return chatMessage.getDate();
    }
}
