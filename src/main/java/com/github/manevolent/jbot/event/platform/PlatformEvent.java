package com.github.manevolent.jbot.event.platform;

import com.github.manevolent.jbot.event.Event;
import com.github.manevolent.jbot.platform.Platform;

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
