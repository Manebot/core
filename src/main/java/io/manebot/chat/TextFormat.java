package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import java.util.EnumSet;

public interface TextFormat {

    TextFormat BASIC = new TextFormat() {
        @Override
        public boolean shouldMention(PlatformUser user) {
            return false;
        }
    };

    boolean shouldMention(PlatformUser user);

    default String mention(Chat target) {
        return target != null ? target.getName() : "(null)";
    }

    default String mention(PlatformUser user) {
        return user != null ? user.getNickname() : "(null)";
    }

    default String format(String string, EnumSet<TextStyle> styles) {
        return string;
    }

    default String escape(String string) {
        return string;
    }

}
