package com.github.manevolent.jbot.chat;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class DefaultRichChatMessage extends DefaultChatMessage implements RichChatMessage {
    private final String title, footer;
    private final ImageElement thumbnail;
    private final Color color;
    private final Collection<Element> body;

    public DefaultRichChatMessage(
            ChatSender sender,
            String message, Date date,
            String title, String footer,
            ImageElement thumbnail,
            Color color,
            Collection<Element> body
    ) {
        super(sender, message, date);

        this.title = title;
        this.footer = footer;
        this.thumbnail = thumbnail;
        this.color = color;
        this.body = body;
    }

    public DefaultRichChatMessage(
            String message, Date date,
            String title, String footer,
            ImageElement thumbnail,
            Color color,
            Collection<Element> body
    ) {
        this(null, message, date, title, footer, thumbnail, color, body);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Collection<Element> getBody() {
        return body;
    }

    @Override
    public String getFooter() {
        return footer;
    }

    @Override
    public ImageElement getThumbnail() {
        return thumbnail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Color color = null;
        private String title = null, footer = null, description = null;
        private Date date = null;
        private ImageElement thumbnail = null;
        private Collection<Element> body = Collections.emptyList();

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder footer(String footer) {
            this.footer = footer;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder thumbnail(ImageElement thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder body(Collection<Element> body) {
            this.body = Collections.unmodifiableCollection(body);
            return this;
        }

        public DefaultRichChatMessage build() {
            return new DefaultRichChatMessage(
                    null,
                    description,
                    date,
                    title,
                    footer,
                    thumbnail,
                    color,
                    body
            );
        }
    }
}
