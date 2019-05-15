package io.manebot.platform;

import io.manebot.chat.Chat;
import com.google.common.collect.MapMaker;
import io.manebot.chat.Community;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Describes an abstract platform connection, a helper class to cover holding instantiations for chats and users in
 * the system in weak reference caches, a memory-tolerant behavior that ensures one-reference for a platform connection.
 */
public abstract class AbstractPlatformConnection implements PlatformConnection {
    private final Map<String, Chat> chatMap = new MapMaker().weakValues().makeMap();
    private final Map<String, PlatformUser> userMap = new MapMaker().weakValues().makeMap();
    private final Map<String, Community> communityMap = new MapMaker().weakValues().makeMap();

    /**
     * Clears the chat map, used to persist Chat objects in the Platform.
     */
    protected final void clearChatMap() {
        chatMap.clear();
    }

    /**
     * Clears the user map, used to persist PlatformUser objects in the Platform.
     */
    protected final void clearUserMap() {
        userMap.clear();
    }

    // Default behavior here
    @Override
    public void disconnect() {
        clearChatMap();
        clearUserMap();
    }

    /**
     * Loads a user by its ID.
     * @param id PlatformUser ID to load.
     * @return PlatformUser instance loaded, null otherwise.
     */
    protected abstract PlatformUser loadUserById(String id);

    /**
     * Loads a chat by its ID.
     * @param id Chat ID to load.
     * @return Chat instance loaded, null otherwise.
     */
    protected abstract Chat loadChatById(String id);

    /**
     * Loads a community by its ID.
     * @param id Community ID to load.
     * @return community instance loaded, null otherwise.
     */
    protected abstract Community loadCommunityById(String id);

    protected final PlatformUser getCachedUserById(String id) {
        return userMap.get(id);
    }

    protected final Chat getCachedChatById(String id) {
        return chatMap.get(id);
    }

    protected final Community getCachedCommunityById(String id) {
        return communityMap.get(id);
    }

    protected final PlatformUser getCachedUserById(String id, Function<String, PlatformUser> function) {
        return userMap.computeIfAbsent(id, function);
    }

    protected final Chat getCachedChatById(String id, Function<String, Chat> function) {
        return chatMap.computeIfAbsent(id, function);
    }

    protected final Community getCachedCommunityById(String id, Function<String, Community> function) {
        return communityMap.computeIfAbsent(id, function);
    }

    @Override
    public Chat getChat(String id) {
        return getCachedChatById(id, this::loadChatById);
    }

    @Override
    public PlatformUser getPlatformUser(String id) {
        return getCachedUserById(id, this::loadUserById);
    }

    @Override
    public Community getCommunity(String id) {
        return getCachedCommunityById(id, this::loadCommunityById);
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
