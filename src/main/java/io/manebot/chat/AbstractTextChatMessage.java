package io.manebot.chat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractTextChatMessage extends AbstractChatMessage {
    private final String message;

    public AbstractTextChatMessage(ChatSender sender, String message) {
        super(sender, Calendar.getInstance().getTime());
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Collection<ChatEmbed> getEmbeds() {
        return Collections.emptyList();
    }
}
