package io.manebot.user;


import io.manebot.chat.TextBuilder;
import io.manebot.command.exception.CommandExecutionException;
import io.manebot.lambda.ThrowingConsumer;

import java.util.function.Consumer;

public interface UserPrompt {

    /**
     * Gets the user that this prompt is for.
     * @return user.
     */
    User getUser();

    /**
     * Gets the user that constructed this prompt.
     * @return caller.
     */
    User getCaller();

    /**
     * Gets the name of this prompt.
     * @return prompt name.
     */
    String getName();

    /**
     * Gets the description builder.
     * @return description builder.
     */
    Consumer<TextBuilder> getDescription();

    /**
     * Completes this prompt.
     * @throws CommandExecutionException if there was a problem fulfilling the prompt.
     */
    void complete() throws CommandExecutionException;

    interface Builder {

        /**
         * Gets the user that the prompt will be for.
         * @return user.
         */
        User getUser();

        /**
         * Gets the user that is constructing this prompt.
         * @return caller.
         */
        User getCaller();

        /**
         * Sets the name, or title, of the prompt.
         * @param name name, or title, of the prompt.
         * @return Builder instance.
         */
        Builder setName(String name);

        /**
         * Sets the description of the prompt.
         * @param description description of the prompt.
         * @return Builder instance.
         */
        default Builder setDescription(String description) {
            return setDescription(textBuilder -> textBuilder.append(description));
        }

        /**
         * Sets the description of the prompt.
         * @param textBuilder TextBuilder instance to format the description of the prompt.
         * @return Builder instance.
         */
        Builder setDescription(Consumer<TextBuilder> textBuilder);

        /**
         * Applies the designated callback to the prompt.
         * @param callback callback to use for the prompt.
         * @return Builder instance.
         */
        Builder setCallback(ThrowingConsumer<UserPrompt, CommandExecutionException> callback);

    }

}
