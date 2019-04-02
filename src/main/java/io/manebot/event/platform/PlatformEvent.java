package io.manebot.event.platform;

import io.manebot.event.Event;
import io.manebot.platform.Platform;

public abstract class PlatformEvent extends Event {
    private final Platform platform;

    public PlatformEvent(Object sender, Platform platform) {
        super(sender);

        this.platform = platform;
    }

    public Platform getPlatform() {
        return platform;
    }
}
