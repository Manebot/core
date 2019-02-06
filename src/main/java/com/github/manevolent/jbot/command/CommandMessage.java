package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.chat.ChatMessage;

import java.util.Date;

public class CommandMessage implements ChatMessage {
    private final String message;
    private final CommandSender sender;
    private final Date date;

    public CommandMessage(String message, CommandSender sender, Date date) {
        this.message = message;
        this.sender = sender;
        this.date = date;
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Date getDate() {
        return date;
    }
}
