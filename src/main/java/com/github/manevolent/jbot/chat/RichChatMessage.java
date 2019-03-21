package com.github.manevolent.jbot.chat;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Collection;

public interface RichChatMessage extends ChatMessage {

    Color getColor();

    String getTitle();

    Collection<Element> getBody();

    String getFooter();

    ImageElement getThumbnail();

    abstract class Element {}

    abstract class TextElement extends Element {
        abstract public String getText();
    }

    abstract class ImageElement extends Element {}

    abstract class ScaffoldElement extends Element {}

    class DivElement extends ScaffoldElement {
        private final Collection<Element> elements;

        public DivElement(Collection<Element> elements) {
            this.elements = elements;
        }

        public Collection<Element> getElements() {
            return elements;
        }
    }

    class RemoteImage extends ImageElement {
        private final URL url;

        public RemoteImage(URL url) {
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }
    }

    class LocalImage extends ImageElement {
        private final BufferedImage image;

        public LocalImage(BufferedImage image) {
            this.image = image;
        }

        public BufferedImage getImage() {
            return image;
        }
    }

    class Mention extends TextElement {
        private final String id;

        public Mention(String id) {
            this.id = id;
        }

        @Override
        public String getText() {
            return id;
        }
    }

    class PlainText extends TextElement {
        private final String text;

        public PlainText(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }
    }

    class URLText extends TextElement {
        private final URL url;

        public URLText(URL url) {
            this.url = url;
        }

        public URL getURL() {
            return url;
        }

        @Override
        public String getText() {
            return getURL().toExternalForm();
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

    class FormattedText extends TextElement {
        private final String text;

        public FormattedText(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }
    }
}
