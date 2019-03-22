package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.chat.ChatSender;
import com.github.manevolent.jbot.user.User;
import com.github.manevolent.jbot.user.UserAssociation;

import java.util.Collection;
import java.util.TimeZone;

/**
 * Describes a user on a Platform.  These are not Bot users, instead, they are platform-specific and subject to sudden
 * disconnect as they become unreachable for a variety of platform-specific reasons.
 */
public interface PlatformUser {

    /**
     * Gets the platform associated with this user.
     * @return Platform instance.
     */
    Platform getPlatform();

    /**
     * Gets the platform connection associated with this user.
     * @return Platform connection instance.
     */
    default PlatformConnection getConnection() {
        return getPlatform().getConnection();
    }

    /**
     * Gets the Id of this user on this platform.
     * @return Platform Id.
     */
    String getId();

    /**
     * Gets the nickname of this user on this platform.
     * @return user nickname.
     */
    default String getNickname() {
        return getId();
    }

    /**
     * Sets the nickname of this user on this platform.
     * @param nickname user nickname.
     */
    default void setNickname(String nickname) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Finds if this user is the bot's own user on this platform, as it appears to other users on the platform.
     * @return true if the user is the bot itself, false otherwise.
     */
    boolean isSelf();

    /**
     * Finds if the user is ignored on the platform.
     * @return true if the user is being ignored, false otherwise.
     */
    default boolean isIgnored() {
        return false;
    }

    /**
     * Ignores or un-ignores a user.
     * @param ignored true if the user should be ignored, false otherwise.
     * @throws UnsupportedOperationException
     */
    default void setIgnored(boolean ignored) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Finds if this user is visibly connected to the platform.
     * @return true if the user is connected to the platform, false otherwise or if the platform is offline.
     */
    default boolean isConnected() {
        return getPlatform().isConnected();
    }

    /**
     * Gets the user associated with this platform user.
     * @return
     */
    default User getAssociatedUser() {
        UserAssociation association = getPlatform().getUserAssocation(this);
        if (association == null) return null;
        else return association.getUser();
    }

    /**
     * Gets the time zone the user is assigned to, according to the platform.
     * @return
     */
    default TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    /**
     * Gets the chats that the user is a part of.
     * @return immutable collection of chats.
     */
    Collection<Chat> getChats();

    /**
     * Gets a direct private chat to the user.
     * @return private chat, null if none is found.
     */
    default Chat getPrivateChat() {
        return null;
    }

    /**
     * Finds the status of the platform user.
     * @return current status.
     */
    default Status getStatus() {
        return Status.UNKNOWN;
    }

    /**
     * Creates a sender for the user, using the specified chat as a target.
     * @param chat Chat to use for responding to messages.
     * @return ChatSender instance.
     */
    ChatSender createSender(Chat chat);

    enum Status {
        ONLINE,
        OFFLINE,
        AWAY,
        DO_NOT_DISTURB,
        UNKNOWN
    }

}