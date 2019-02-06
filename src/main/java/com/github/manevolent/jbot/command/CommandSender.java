package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.user.User;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandSender {
    private final List<String> lines = new LinkedList<>();
    private final Object bufferLock = new Object();
    private volatile boolean buffered = false;

    public CommandSender getParent() {
        return null;
    }

    /**
     * Gets the display name of the command sender.
     */
    public abstract String getName();

    /**
     * Gets the date the command was received.
     */
    public abstract Date getDate();

    /**
     * Gets the conversation the message was sent in.
     */
    public abstract Chat getChat();

    /**
     * Gets the user this sender represents.
     * @return User.
     */
    public abstract User getUser();

    /**
     * Opens the command buffer.
     * @return true if the buffer was opened, false if no changes were made.
     */
    public boolean beginBuffer() {
        synchronized (bufferLock) {
            if (buffered) return false;
            else return buffered = true;
        }
    }

    /**
     * Adds a message to the command buffer or sends a message.
     * @param message message to add.
     */
    public void sendMessage(String message) {
        message = getName().trim() + " -> " + formatMessage(message);
        synchronized (bufferLock) {
            if (buffered) {

                lines.add(message);

            } else getChat().sendMessage(message);
        }
    }

    /**
     * Ends the command buffer.
     * @return number of lines sent.
     */
    public int endBuffer() {
        int c = 0;
        StringBuilder builder = new StringBuilder();

        synchronized (bufferLock) {
            if (!buffered) return 0;

            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                builder.append(iterator.next());
                if (iterator.hasNext()) builder.append("\n");
                c++;
                iterator.remove();
            }

            buffered = false;
        }

        String result = builder.toString();
        if (result.length() > 0) getChat().sendMessage(result);

        return c;
    }

    /**
     * Sends several messages to the remote.
     * @param messages Messages to send.
     */
    public void sendMessage(String... messages) {
        for (String s : messages) sendMessage(s);
    }

    /**
     * Flushes the buffer.
     */
    public void flush() {
        synchronized (bufferLock) {
            if (buffered) {
                endBuffer();
                beginBuffer();
            }
        }
    }

    public static final String formatMessage(String message) {
        return message
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ");
    }
}
