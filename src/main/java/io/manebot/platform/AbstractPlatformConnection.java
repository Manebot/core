package io.manebot.platform;

import io.manebot.chat.Chat;
import com.google.common.collect.MapMaker;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Describes an abstract platform connection, a helper class to cover holding instantiations for chats and users in
 * the system in weak reference caches, a memory-tolerant behavior that ensures one-reference for a platform connection.
 */
public abstract class AbstractPlatformConnection implements PlatformConnection {
    private final Map<String, Chat> chatMap = new MapMaker().weakValues().makeMap();
    private final Map<String, PlatformUser> userMap = new MapMaker().weakValues().makeMap();

    /**
     * Clears the chat map, used to persist Chat objects in the Platform.
     */
    protected void clearChatMap() {
        chatMap.clear();
    }

    /**
     * Clears the user map, used to persist PlatformUser objects in the Platform.
     */
    protected void clearUserMap() {
        userMap.clear();
    }

    protected abstract PlatformUser loadUserById(String id);

    /**
     * Loads a chat by its ID.
     * @param id Chat ID to load.
     * @return Chat instance loaded, null otherwise.
     */
    protected abstract Chat loadChatById(String id);

    // Default behavior here
    @Override
    public void disconnect() {
        clearChatMap();
        clearUserMap();
    }

    @Override
    public Chat getChat(String id) {
        return chatMap.computeIfAbsent(id, this::loadChatById);
    }

    @Override
    public Collection<Chat> getChats() {
        return Collections.unmodifiableCollection(
                getChatIds().stream()
                        .map(this::getChat)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public PlatformUser getPlatformUser(String id) {
        return userMap.computeIfAbsent(id, this::loadUserById);
    }

    @Override
    public Collection<PlatformUser> getPlatformUsers() {
        return Collections.unmodifiableCollection(
                getPlatformUserIds()
                        .stream()
                        .map(this::getPlatformUser)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }
}
