package io.manebot.command.response;

import io.manebot.chat.ChatSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.Collection;

public class DefaultBasicCommandDetailsResponse extends CommandDetailsResponse {
    public DefaultBasicCommandDetailsResponse(ChatSender sender,
                                              String objectName, String objectKey,
                                              Collection<Item> items) {
        super(sender, objectName, objectKey, items);
    }

    @Override
    public void send() throws CommandExecutionException {
        if (getObjectName() != null && getObjectKey() != null) {
            getSender().sendMessage(getObjectName() + " \"" + getObjectKey() + "\" details:");
        } else if (getObjectName() != null) {
            getSender().sendMessage(getObjectName() + " details:");
        } else if (getObjectKey() != null) {
            getSender().sendMessage("\"" + getObjectKey() + "\" details:");
        }

        for (Item item : getItems())
            getSender().sendMessage(" " + item.getKey() + ": " + item.getValue());
    }
}