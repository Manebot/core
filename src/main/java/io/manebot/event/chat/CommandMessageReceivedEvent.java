package io.manebot.event.chat;

import io.manebot.command.CommandMessage;

public class CommandMessageReceivedEvent extends ChatEvent {
    private final CommandMessage message;

    public CommandMessageReceivedEvent(Object sender, CommandMessage message) {
        super(sender, message.getSender().getChat());

        this.message = message;
    }

    public CommandMessage getMessage() {
        return message;
    }
}
