package com.github.manevolent.jbot.command;


import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.chat.ChatSender;
import com.github.manevolent.jbot.chat.DefaultChatSender;
import com.github.manevolent.jbot.command.response.CommandDetailsResponse;
import com.github.manevolent.jbot.command.response.CommandListResponse;
import com.github.manevolent.jbot.conversation.Conversation;
import com.github.manevolent.jbot.user.User;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public abstract class CommandSender extends DefaultChatSender {
    public CommandSender(String username, String displayName, Chat chat) {
        super(username, displayName, chat);
    }

    public CommandSender(String username, Chat chat) {
        super(username, chat);
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

    /**
     * Creates a list response to send to the command sender, opportunistically formatting as rich content.
     * @param function Function providing a command response object from a builder.
     * @param <T> List item type.
     * @return CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    public abstract <T> CommandListResponse<T> list(
           Function<CommandListResponse.Builder<T>, CommandListResponse<T>> function
    );

    /**
     * Creates a details response to send to the command sender, opportunistically formatting as rich content.
     *
     * Details are formatted as such:
     *
     *      ObjectName "ObjectKey" details:
     *       Key: value
     *       Key: [value1,value2,value3]
     *
     * @param function Function providing a command response object from a builder.
     * @return CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    public abstract CommandDetailsResponse details(
            Function<CommandDetailsResponse.Builder, CommandDetailsResponse> function
    );

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
