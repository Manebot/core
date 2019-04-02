package io.manebot.command;

import io.manebot.chat.ChatEmbed;
import io.manebot.chat.ChatMessage;
import io.manebot.platform.PlatformUser;

import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

public class CommandMessage implements ChatMessage {
    private final ChatMessage chatMessage;
    private final CommandSender sender;

    public CommandMessage(ChatMessage chatMessage, CommandSender sender) {
        this.chatMessage = chatMessage;
        this.sender = sender;
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    public void delete() throws UnsupportedOperationException {
        chatMessage.delete();
    }

    @Override
    public ChatMessage edit(String message) {
        return chatMessage.edit(message);
    }

    @Override
    public ChatMessage edit(Consumer<Builder> function) {
        return chatMessage.edit(function);
    }

    @Override
    public boolean wasEdited() {
        return chatMessage.wasEdited();
    }

    @Override
    public Date getEditedDate() {
        return chatMessage.getEditedDate();
    }

    @Override
    public String getMessage() {
        return chatMessage.getMessage();
    }

    @Override
    public Collection<ChatEmbed> getEmbeds() {
        return chatMessage.getEmbeds();
    }

    @Override
    public Date getDate() {
        return chatMessage.getDate();
    }

    @Override
    public Collection<PlatformUser> getMentions() {
        return chatMessage.getMentions();
    }

    @Override
    public boolean doesMentionSelf() {
        return chatMessage.doesMentionSelf();
    }
}
