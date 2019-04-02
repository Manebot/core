package io.manebot.command;


import io.manebot.chat.Chat;
import io.manebot.chat.DefaultChatSender;
import io.manebot.conversation.Conversation;
import io.manebot.platform.PlatformUser;
import io.manebot.user.User;

public abstract class CommandSender extends DefaultChatSender {
    public CommandSender(PlatformUser user, Chat chat) {
        super(user, chat);
    }

    /**
     * Gets the parent to this command sender, such as for a sub-execution.
     * @return CommandSender instance.
     */
    public CommandSender getParent() {
        return null;
    }

    /**
     * Gets the conversation the message was sent in.
     */
    public abstract Conversation getConversation();

    /**
     * Gets the user this sender represents.
     * @return User.
     */
    public abstract User getUser();

    @Override
    public String getUsername() {
        return getUser().getUsername();
    }

    @Override
    public String getDisplayName() {
        String displayName = getUser().getDisplayName();
        return displayName == null ? getUsername() : displayName;
    }

    @Override
    public Chat getChat() {
        return getConversation().getChat();
    }
}
