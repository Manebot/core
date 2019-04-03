package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import java.util.Date;
import java.util.function.Consumer;

public class BasicTextChatMessage extends AbstractTextChatMessage {
    public BasicTextChatMessage(ChatSender sender, String message) {
        super(sender, message);
    }

    @Override
    public void delete() throws UnsupportedOperationException {
       throw new UnsupportedOperationException();
    }

    @Override
    public ChatMessage edit(Consumer<ChatMessage.Builder> function) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean wasEdited() {
        return false;
    }

    @Override
    public Date getEditedDate() {
        return null;
    }

    public static class Builder implements ChatMessage.Builder {
        private final PlatformUser user;
        private final Chat chat;

        private String message;

        public Builder(PlatformUser user, Chat chat) {
            this.user = user;
            this.chat = chat;
        }

        @Override
        public PlatformUser getUser() {
            return user;
        }

        @Override
        public Chat getChat() {
            return chat;
        }

        @Override
        public ChatMessage.Builder rawMessage(String message) {
            this.message = message;
            return this;
        }

        @Override
        public Builder embed(Consumer<ChatEmbed.Builder> function) {
            throw new UnsupportedOperationException();
        }

        public String getMessage() {
            return message;
        }

        public BasicTextChatMessage build() {
            return new BasicTextChatMessage(new DefaultChatSender(user, chat), message);
        }
    }
}
