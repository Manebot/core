package io.manebot.command.response;

import io.manebot.chat.ChatSender;

import java.util.Collection;

public class DefaultRichCommandDetailsResponse extends CommandDetailsResponse {
    public DefaultRichCommandDetailsResponse(ChatSender sender,
                                             String objectName, String objectKey,
                                             Collection<Item> items) {
        super(sender, objectName, objectKey, items);
    }

    @Override
    public void send() {
        getSender().getChat().sendMessage(builder -> {
            if (builder.getChat().getFormat().shouldMention(getSender().getPlatformUser()))
                builder.message(textBuilder -> textBuilder.appendMention(getSender().getPlatformUser()));

            builder.embed(embedBuilder -> {
                if (getObjectName() != null && getObjectKey() != null) {
                    embedBuilder.title(getObjectName() + " \"" + getObjectKey() + "\" details");
                } else if (getObjectName() != null) {
                    embedBuilder.title(getObjectName() + " details");
                } else if (getObjectKey() != null) {
                    embedBuilder.title("\"" + getObjectKey() + "\" details");
                }

                for (Item item : getItems())
                    embedBuilder.field(item.getKey(), item.getValue());
            });
        });
    }
}