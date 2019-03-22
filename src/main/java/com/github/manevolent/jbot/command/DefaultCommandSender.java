package com.github.manevolent.jbot.command;

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

    @Override
    public <T> CommandListResponse<T> list(
            Function<CommandListResponse.Builder<T>, CommandListResponse<T>> function
    ) {
        CommandListResponse.Builder<T> builder;

        if (getConversation().getChat().canSendRichMessages()) {
            builder = new CommandListResponse.Builder<T>() {
                @Override
                public CommandListResponse<T> build() {
                    return new DefaultRichCommandListResponse<>(
                            DefaultCommandSender.this,
                            getTotalElements(),
                            getPage(),
                            getElementsPerPage(),
                            createListAccessor(),
                            getResponder()
                    );
                }
            };
        } else {
            builder = new CommandListResponse.Builder<T>() {
                @Override
                public CommandListResponse<T> build() {
                    return new DefaultBasicCommandListResponse<>(
                            DefaultCommandSender.this,
                            getTotalElements(),
                            getPage(),
                            getElementsPerPage(),
                            createListAccessor(),
                            getResponder()
                    );
                }
            };
        }

        return function.apply(builder);
    }

    @Override
    public CommandDetailsResponse details(Function<CommandDetailsResponse.Builder, CommandDetailsResponse> function) {
        CommandDetailsResponse.Builder builder;

        if (getConversation().getChat().canSendRichMessages()) {
            builder = new CommandDetailsResponse.Builder() {
                @Override
                public CommandDetailsResponse build() {
                    return new DefaultRichCommandDetailsResponse(
                            DefaultCommandSender.this,
                            getName(),
                            getKey(),
                            getItems()
                    );
                }
            };
        } else {
            builder = new CommandDetailsResponse.Builder() {
                @Override
                public CommandDetailsResponse build() {
                    return new DefaultBasicCommandDetailsResponse(
                            DefaultCommandSender.this,
                            getName(),
                            getKey(),
                            getItems()
                    );
                }
            };
        }

        return function.apply(builder);
    }

}
