package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.command.executor.AliasedCommandExecutor;
import com.github.manevolent.jbot.command.executor.CommandExecutor;

public abstract class CommandManager {

    /**
     * Registers a command executor to the system.
     *
     * In JBot, command format is as follows:
     * .label arguments
     * Where "label" is a static command label, such as "ping", and "arguments" is a space-delimited array of strings.
     *
     * @param label label to map an executor to.
     * @param executor CommandExecutor to fire for the given label.
     * @return Registration instance, an object which permits alias building.
     */
    public abstract Registration registerExecutor(String label, CommandExecutor executor);

    /**
     * Unregisters an existing command executor.
     * @param label label to unregister.
     */
    public abstract void unregisterExecutor(String label);

    /**
     * Gets a command executor by its label.
     * @param label label to find.
     * @return CommandExecutor instance if one is found, null otherwise.
     */
    public abstract CommandExecutor getExecutor(String label);

    /**
     * Command Registrations are a fluent way of aliasing commands.
     */
    public class Registration {
        private final CommandExecutor executor;
        private final String label;

        public Registration(CommandExecutor executor, String label) {
            this.executor = executor;
            this.label = label;
        }

        /**
         * CommandExecutor instance mapped to this Registration.
         * @return CommandExecutor instance.
         */
        public CommandExecutor getExecutor() {
            return executor;
        }

        /**
         * Gets the command label bound with this registration.
         * @return label.
         */
        public String getLabel() {
            return label;
        }

        /**
         * Creates an alias of the command described by this registration.
         * @param alias label alias to map.
         * @return Registration instance.
         */
        public Registration alias(String alias) {
            return registerExecutor(alias, new AliasedCommandExecutor(executor, label));
        }
    }
}
