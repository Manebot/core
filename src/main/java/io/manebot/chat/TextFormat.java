package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import java.util.EnumSet;

public interface TextFormat {

    TextFormat BASIC = new TextFormat() {};

    default boolean shouldMention(PlatformUser user) {
        return this != BASIC;
    }

    default String mention(Chat target) {
        return target.getName();
    }

    default String mention(PlatformUser user) {
        return user.getNickname();
    }

    default String format(String string, EnumSet<TextStyle> styles) {
        return string;
    }

    default String escape(String string) {
        return string;
    }

}
