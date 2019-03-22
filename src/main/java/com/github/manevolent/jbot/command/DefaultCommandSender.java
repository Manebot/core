package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.chat.DefaultChatSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;
import com.github.manevolent.jbot.command.response.*;
import com.github.manevolent.jbot.conversation.Conversation;
import com.github.manevolent.jbot.user.User;

import java.util.function.Function;

public class DefaultCommandSender extends CommandSender {
    private final Conversation conversation;
    private final User user;

    public DefaultCommandSender(Conversation conversation, User user) {
        super(user.getUsername(), user.getDisplayName(), conversation.getChat());

        this.conversation = conversation;
        this.user = user;
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
