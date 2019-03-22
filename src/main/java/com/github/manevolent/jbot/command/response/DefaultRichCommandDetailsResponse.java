package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.chat.DefaultRichChatMessage;
import com.github.manevolent.jbot.chat.RichChatMessage;
import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.Collection;
import java.util.LinkedList;

public class DefaultRichCommandDetailsResponse extends CommandDetailsResponse {
    public DefaultRichCommandDetailsResponse(CommandSender sender,
                                             String objectName, String objectKey,
                                             Collection<Item> items) {
        super(sender, objectName, objectKey, items);
    }

    @Override
    public void send() throws CommandExecutionException {
        DefaultRichChatMessage.Builder builder = DefaultRichChatMessage.builder();

        if (getObjectName() != null && getObjectKey() != null) {
            builder.title(getObjectName() + " \"" + getObjectKey() + "\" details");
        } else if (getObjectName() != null) {
            builder.title(getObjectName() + " details");
        } else if (getObjectKey() != null) {
            builder.title("\"" + getObjectKey() + "\" details");
        }

        Collection<RichChatMessage.Element> body = new LinkedList<>();
        for (Item item : getItems())
            body.add(new RichChatMessage.Field(item.getKey(), item.getValue()));

        builder.body(body);
        getSender().sendMessage(builder.build());
    }
}