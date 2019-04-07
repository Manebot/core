package io.manebot.command.response;

import io.manebot.chat.ChatMessage;
import io.manebot.chat.ChatSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.Collection;
import java.util.LinkedList;

public class DefaultBasicCommandDetailsResponse extends CommandDetailsResponse {
    public DefaultBasicCommandDetailsResponse(ChatSender sender,
                                              String objectName, String objectKey,
                                              Collection<Item> items) {
        super(sender, objectName, objectKey, items);
    }

    @Override
    public Collection<ChatMessage> send() throws CommandExecutionException {
        if (getObjectName() != null && getObjectKey() != null) {
            getSender().sendMessage(getObjectName() + " \"" + getObjectKey() + "\" details:");
        } else if (getObjectName() != null) {
            getSender().sendMessage(getObjectName() + " details:");
        } else if (getObjectKey() != null) {
            getSender().sendMessage("\"" + getObjectKey() + "\" details:");
        }

        Collection<ChatMessage> chatMessages = new LinkedList<>();

        for (Item item : getItems())
            chatMessages.addAll(getSender().sendMessage(" " + item.getKey() + ": " + item.getValue()));

        return chatMessages;
    }
}