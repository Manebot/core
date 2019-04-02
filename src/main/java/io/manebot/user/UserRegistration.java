package io.manebot.user;

import io.manebot.chat.ChatMessage;
import io.manebot.command.exception.CommandExecutionException;
import io.manebot.platform.PlatformUser;

public interface UserRegistration {

    default boolean canRegister(PlatformUser platformUser) {
        return true;
    }

    UserAssociation register(ChatMessage message) throws CommandExecutionException;

}
