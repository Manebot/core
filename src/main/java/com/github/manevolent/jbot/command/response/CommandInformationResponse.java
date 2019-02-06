package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandInformationResponse extends CommandResponse {
    private final String objectName;
    private final String objectId;
    private final List<Map.Entry<String, String>> properties = new LinkedList<>();

    public CommandInformationResponse(String objectName, String objectId) {
        this.objectName = objectName;
        this.objectId = objectId;
    }

    @Override
    public void respond(CommandSender sender) throws CommandExecutionException {
        sender.sendMessage(objectName + " \"" + objectId + "\" information:");

        if (properties.size() <= 0) {
            sender.sendMessage(" (No information)");
        } else {
            for (Map.Entry<String, String> propertyEntry : properties) {
                sender.sendMessage(" " + propertyEntry.getKey().trim() + ": " + propertyEntry.getValue().trim());
            }
        }
    }

    public CommandInformationResponse add(final String key, final String value) {
        properties.add(new Map.Entry<String, String>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String setValue(String value) {
                throw new UnsupportedOperationException();
            }
        });

        return this;
    }
}
