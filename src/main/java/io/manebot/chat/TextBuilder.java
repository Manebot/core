package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

public interface TextBuilder {

    /**
     * Gets the sender associated with this formatted message.
     * @return ChatSender instance.
     */
    Chat getChat();

    /**
     * Gets the text format instance being used in this message.
     * @return TextFormat instance.
     */
    default TextFormat getFormat() {
        return getChat().getFormat();
    }

    /**
     * Finds if the formatted message has content in its buffer.
     * @return true if the formatted message has content, false otherwise.
     */
    boolean hasContent();

    /**
     * Adds a new line to the message output.
     * @return continued FormattedMessage instance.
     */
    TextBuilder newLine();

    /**
     * Appends a user mention to the formatted message.
     * @param user user to mention.
     * @return continued FormattedMessage instance.
     */
    TextBuilder appendMention(PlatformUser user);

    /**
     * Appends a chat mention to the formatted message.
     * @param chat chat to mention.
     * @return continued FormattedMessage instance.
     */
    TextBuilder appendMention(Chat chat);

    /**
     * Appends unformatted, unescaped text to the message.
     * @param message text to directly append to the message.
     * @return continued FormattedMessage instance.
     */
    TextBuilder appendRaw(String message);

    /**
     * Appends unformatted, escaped text to the message.
     * @param message text to escape and append to the message.
     * @return continued FormattedMessage instance.
     */
    TextBuilder append(String message);

    /**
     * Appends a formatted URL to this message.
     * @param url URL to append to the message.
     * @return continued FormattedMessage instance.
     */
    default TextBuilder appendUrl(String url) {
        return append(url);
    }

    /**
     * Appends a formatted URL to this message.
     * @param url URL to append to the message.
     * @return continued FormattedMessage instance.
     */
    default TextBuilder appendUrl(URL url) {
        return appendUrl(url.toExternalForm());
    }

    /**
     * Appends formatted text to the message.
     * @param message text to escape and append to the message.
     * @param styles styles to apply to the specified text.
     * @return continued FormattedMessage instance.
     */
    TextBuilder append(String message, EnumSet<TextStyle> styles);

    /**
     * Builds the formatted message.
     * @return formatted message.
     */
    String build();

}
