package com.github.manevolent.jbot.plugin.java;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.bot.Bot;
import com.github.manevolent.jbot.command.CommandManager;
import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.event.EventListener;
import com.github.manevolent.jbot.event.EventManager;
import com.github.manevolent.jbot.plugin.Plugin;

import java.util.LinkedList;
import java.util.List;

public abstract class JavaPlugin implements Plugin {
    private boolean initialized = false;

    private PluginEventManager eventManager;
    private PluginCommandManager commandManager;

    private Bot bot;

    private final Artifact artifact;

    private final Object enableLock = new Object();
    private boolean enabled;

    protected JavaPlugin(Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * Gets the <b>Bot</b> instance associated with this plugin.
     * @return Bot instance.
     */
    protected final Bot getBot() {
        return bot;
    }

    protected final EventManager getEventManager() {
        return eventManager;
    }

    protected final CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Sets the <b>Bot</b> instance associated with this plugin.
     * @param bot Bot instance.
     */
    public final void initialize(Bot bot, CommandManager commandManager, EventManager eventManager)
            throws IllegalStateException {
        synchronized (this) {
            if (initialized) throw new IllegalStateException();

            this.bot = bot;
            this.commandManager = new PluginCommandManager(commandManager);
            this.eventManager = new PluginEventManager(eventManager);

            this.initialized = true;
        }
    }

    @Override
    public final String getName() {
        return artifact.getManifest().getArtifactId().toLowerCase();
    }

    @Override
    public final Artifact getArtifact() {
        return artifact;
    }

    @Override
    public final boolean setEnabled(boolean enabled) {
        synchronized (enableLock) {
            if (this.enabled != enabled) {
                if (enabled) {
                    onEnable();
                    this.enabled = true;
                    onEnabled();
                } else {
                    onDisable();
                    commandManager.destroy();
                    eventManager.destroy();
                    this.enabled = false;
                    onDisabled();
                }

                return true;
            }



            return false;
        }
    }

    protected void onEnable() {}
    protected void onEnabled() {}
    protected void onDisable() {}
    protected void onDisabled() {}

    private class PluginEventManager implements EventManager {
        private final Object registrationLock = new Object();
        private final EventManager eventManager;
        private final List<EventListener> registeredListeners = new LinkedList<>();

        private PluginEventManager(EventManager eventManager) {
            this.eventManager = eventManager;
        }

        @Override
        public void registerListener(EventListener listener) {
            synchronized (registrationLock) {
                eventManager.registerListener(listener);
                registeredListeners.add(listener);
            }
        }

        @Override
        public void unregisterListener(EventListener listener) {
            synchronized (registrationLock) {
                if (!registeredListeners.contains(listener)) return;

                eventManager.unregisterListener(listener);
                registeredListeners.remove(listener);
            }
        }
        private void destroy() {
            synchronized (registrationLock) {
                registeredListeners.forEach(this::unregisterListener);
            }
        }
    }

    private class PluginCommandManager extends CommandManager {
        private final Object registrationLock = new Object();
        private final CommandManager commandManager;
        private final List<String> registeredLabels = new LinkedList<>();

        private PluginCommandManager(CommandManager commandManager) {
            this.commandManager = commandManager;
        }

        @Override
        public Registration registerExecutor(String label, CommandExecutor executor) {
            synchronized (registrationLock) {
                if (registeredLabels.contains(label))
                    throw new IllegalArgumentException(label);

                Registration registration = commandManager.registerExecutor(label, executor);
                registeredLabels.add(label);
                return new PluginRegistration(executor, registration, label);
            }
        }

        @Override
        public void unregisterExecutor(String label) {
            synchronized (registrationLock) {
                if (!registeredLabels.contains(label))
                    throw new IllegalArgumentException(label);

                commandManager.unregisterExecutor(label);
                registeredLabels.remove(label);
            }
        }

        private void destroy() {
            synchronized (registrationLock) {
                registeredLabels.forEach(this::unregisterExecutor);
            }
        }

        private class PluginRegistration extends Registration {
            private final Registration registration;

            public PluginRegistration(CommandExecutor executor, Registration registration, String label) {
                super(executor, label);
                this.registration = registration;
            }

            @Override
            public Registration alias(String alias) {
                synchronized (registrationLock) {
                    registration.alias(alias);
                    registeredLabels.add(alias);
                }

                return this;
            }
        }
    }
}
