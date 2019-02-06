package com.github.manevolent.jbot.conversation;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.entity.EntityType;

public interface Conversation extends EntityType {

    String getId();

    Chat getChat();

}
