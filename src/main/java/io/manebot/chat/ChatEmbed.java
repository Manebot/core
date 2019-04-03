package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

public interface ChatEmbed {

    /**
     * Gets the user-defined RGB color of this chat message.
     * @return ChatMessage color.
     */
    Color getColor();

    /**
     * Gets the user-defined title of this chat message.
     * @return title.
     */
    String getTitle();

    /**
     * Gets the description of this embed.
     * @return embed description.
     */
    String getDescription();

    /**
     * Gets the fields in this embed.
     * @return field list.
     */
    Collection<Field> getFields();

    /**
     * Gets the footer of this chat message.
     * @return footer string.
     */
    String getFooter();

    /**
     * Gets the thumbnail set for this chat message
     * @return ImageElement representing the thumbnail of this image.
     */
    ImageElement getThumbnail();

    abstract class Element {}

    abstract class TextElement extends Element {
        abstract public String getText();
    }

    abstract class ImageElement extends Element {
        public abstract BufferedImage getImage() throws IOException;
    }

    class RemoteImage extends ImageElement {
        private final URL url;

        public RemoteImage(URL url) {
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }

        @Override
        public BufferedImage getImage() throws IOException {
            return ImageIO.read(getUrl());
        }
    }

    class LocalImage extends ImageElement {
        private final BufferedImage image;

        public LocalImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public BufferedImage getImage() {
            return image;
        }
    }

    class Field extends TextElement {
        private final String key, value;
        private final Boolean inline;

        public Field(String key, String value) {
            this.key = key;
            this.value = value;
            this.inline = null;
        }

        public Field(String key, String value, boolean inline) {
            this.key = key;
            this.value = value;
            this.inline = inline;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public Boolean isInline() {
            return inline;
        }

        @Override
        public String getText() {
            return getKey() + ": " + getValue();
        }
    }

    interface Builder {

        Chat getChat();

        /**
         * Sets the thumbnail of the chat message.
         * @param thumbnail Thumbnail.
         * @return Builder instance.
         */
        Builder thumbnail(ChatEmbed.ImageElement thumbnail);

        /**
         * Sets the raw title of this builder.
         * @param title title to set.
         * @return Builder instance.
         */
        Builder titleRaw(String title);

        /**
         * Sets the title of this builder.
         * @param title title to set.
         * @return Builder instance.
         */
        default Builder title(String title) {
            return title(builder -> builder.append(title));
        }

        /**
         * Sets the title of this builder.
         * @param textBuilder text builder to use when building a title.
         * @return Builder instance.
         */
        default Builder title(Consumer<TextBuilder> textBuilder) {
            TextBuilder builder = getChat().text();
            textBuilder.accept(builder);
            return titleRaw(builder.build());
        }

        /**
         * Sets the raw text message that this embed encapsulates.
         * @param message message to set.
         * @return Builder instance.
         */
        Builder descriptionRaw(String message);

        /**
         * Sets the text message that this embed encapsulates.
         * @param message message to get.
         * @return Builder instance.
         */
        default Builder description(String message) {
            return description(textBuilder -> textBuilder.append(message));
        }

        /**
         * Sets the text message that this embed encapsulates.
         * @param textBuilder message to set.
         * @return Builder instance.
         */
        default Builder description(Consumer<TextBuilder> textBuilder) {
            TextBuilder builder = getChat().text();
            textBuilder.accept(builder);
            return descriptionRaw(builder.build());
        }

        /**
         * Sets the raw footer of the embed.
         * @param footer raw footer to set.
         * @return Builder instance.
         */
        Builder footerRaw(String footer);

        /**
         * Sets the text message that this embed encapsulates.
         * @param message footer to set.
         * @return Builder instance.
         */
        default Builder footer(String message) {
            return footer(textBuilder -> textBuilder.append(message));
        }

        /**
         * Sets the footer of the embed.
         * @param textBuilder text builder to set footer from.
         * @return Builder instance.
         */
        default Builder footer(Consumer<TextBuilder> textBuilder) {
            TextBuilder builder = getChat().text();
            textBuilder.accept(builder);
            return footerRaw(builder.build());
        }

        /**
         * Sets the timestamp of this embed.
         * @param timestamp timestamp to set.
         * @return Builder instance.
         */
        Builder timestamp(Date timestamp);

        /**
         * Sets the timestamp of this embed.
         * @param instant timestamp to set.
         * @return Builder instance.
         */
        Builder timestamp(Instant instant);

        /**
         * Sets the color of this embed.
         * @param color color to set.
         * @return Builder instance.
         */
        Builder color(Color color);

        /**
         * Adds a field to the embed.
         * @param name field name to add.
         * @param value field value to add.
         * @return Builder instance.
         */
        default Builder field(String name, String value) {
            return field(name, textBuilder -> textBuilder.append(value));
        }

        /**
         * Adds a field to the embed.
         * @param name field name to add.
         * @param value field value to add.
         * @param inline inline
         * @return Builder instance.
         */
        default Builder field(String name, String value, boolean inline) {
            return field(name, textBuilder -> textBuilder.append(value), inline);
        }

        /**
         * Adds a field to the embed.
         * @param name field name to add.
         * @param value field value to add.
         * @return Builder instance.
         */
        default Builder field(String name, Consumer<TextBuilder> value) {
            TextBuilder builder = getChat().text();
            value.accept(builder);
            return fieldRaw(name, builder.build());
        }

        /**
         * Adds a field to the embed.
         * @param name field name to add.
         * @param value field value to add.
         * @param inline inline
         * @return Builder instance.
         */
        default Builder field(String name, Consumer<TextBuilder> value, boolean inline) {
            TextBuilder builder = getChat().text();
            value.accept(builder);
            return fieldRaw(name, builder.build(), inline);
        }

        /**
         * Adds a field to the embed.
         * @param name field name to add.
         * @param value raw field value to add.
         * @return Builder instance.
         */
        Builder fieldRaw(String name, String value);

        /**
         * Adds a field to the embed.
         * @param name field name to add.
         * @param value raw field value to add.
         * @param inline inline
         * @return Builder instance.
         */
        Builder fieldRaw(String name, String value, boolean inline);

    }
}
