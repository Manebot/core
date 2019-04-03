package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import java.util.EnumSet;

public class DefaultTextBuilder implements TextBuilder {
    private final Chat chat;
    private final TextFormat format;
    private final StringBuilder builder = new StringBuilder();

    public DefaultTextBuilder(Chat chat, TextFormat format) {
        this.chat = chat;
        this.format = format;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public TextFormat getFormat() {
        return format;
    }

    @Override
    public boolean hasContent() {
        return builder.length() > 0;
    }

    @Override
    public TextBuilder newLine() {
        return appendRaw("\n");
    }

    @Override
    public TextBuilder appendMention(PlatformUser user) {
        builder.append(getFormat().mention(user));
        return this;
    }

    @Override
    public TextBuilder appendMention(Chat chat) {
        builder.append(getFormat().mention(chat));
        return this;
    }

    @Override
    public TextBuilder appendRaw(String message) {
        builder.append(message);
        return this;
    }

    @Override
    public TextBuilder append(String message) {
        builder.append(getFormat().format(message, EnumSet.noneOf(TextStyle.class)));
        return this;
    }

    @Override
    public TextBuilder append(String message, EnumSet<TextStyle> styles) {
        builder.append(getFormat().format(message, styles));
        return this;
    }

    @Override
    public String build() {
        return builder.toString();
    }
}
