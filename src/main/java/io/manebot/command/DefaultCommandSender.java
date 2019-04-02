package io.manebot.command;

import io.manebot.command.response.*;
import io.manebot.conversation.Conversation;
import io.manebot.platform.PlatformUser;
import io.manebot.user.User;

public class DefaultCommandSender extends CommandSender {
    private final Conversation conversation;
    private final User user;

    public DefaultCommandSender(Conversation conversation, PlatformUser platformUser, User user) {
        super(platformUser, conversation.getChat());

        this.conversation = conversation;
        this.user = user;
    }

    @Override
    public String getUsername() {
        return getUser().getUsername();
    }

    @Override
    public String getDisplayName() {
        return getUser().getDisplayName();
    }

    @Override
    public Conversation getConversation() {
        return conversation;
    }

    @Override
    public User getUser() {
        return user;
    }

}
