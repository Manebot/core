package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.chat.Chat;
import com.google.common.collect.MapMaker;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractPlatformConnection implements PlatformConnection {
    private final Map<String, Chat> map = new MapMaker().weakValues().makeMap();

    /**
     * Clears the chat map, used to persist Chat objects in the Platform.
     */
    protected void clearChatMap() {
        map.clear();
    }

    protected abstract Chat loadChatById(String id);

    @Override
    public Chat getChatById(String id) {
        return map.computeIfAbsent(id, this::loadChatById);
    }

    @Override
    public Collection<Chat> getChats() {
        return Collections.unmodifiableCollection(map.values());
    }
}
