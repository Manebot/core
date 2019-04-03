package io.manebot.command.response;

import io.manebot.chat.ChatSender;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CommandDetailsResponse extends CommandResponse {
    private final String objectName;
    private final String objectKey;
    private final Collection<Item> items;

    public CommandDetailsResponse(ChatSender sender, String objectName, String objectKey, Collection<Item> items) {
        super(sender);
        this.objectName = objectName;
        this.objectKey = objectKey;
        this.items = items;
    }

    public Collection<Item> getItems() {
        return Collections.unmodifiableCollection(items);
    }

    public String getObjectName() {
        return objectName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public interface Item {
        String getKey();
        String getValue();
    }

    private static class DefaultItem implements Item {
        private final String key, value;

        private DefaultItem(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static class ArrayItem implements Item {
        private final String key;
        private final Collection<String> strings;

        private ArrayItem(String key, Collection<? extends Object> objects) {
            this.key = key;
            this.strings =
                    objects.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            if (strings.size() <= 0) return "(none)";
            else return "[" + String.join(", ", strings) + "]";
        }
    }

    public static abstract class Builder {
        private final Collection<Item> items = new LinkedList<>();
        private String name, key;

        public Builder() { }

        public String getName() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public String getKey() {
            return key;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder item(Item item) {
            if (item == null) return this;
            items.add(item);
            return this;
        }

        public Builder item(String key, String value) {
            if (value == null) return this;
            items.add(new DefaultItem(key, value));
            return this;
        }

        public Builder item(String key, Collection<? extends Object> collection) {
            if (collection == null) return this;
            items.add(new ArrayItem(key, collection));
            return this;
        }

        public Collection<Item> getItems() {
            return Collections.unmodifiableCollection(items);
        }

        public abstract CommandDetailsResponse build();
    }
}
