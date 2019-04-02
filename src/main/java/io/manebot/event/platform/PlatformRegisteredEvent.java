package io.manebot.event.platform;

import io.manebot.platform.Platform;

public class PlatformRegisteredEvent extends PlatformEvent {
    public PlatformRegisteredEvent(Object sender, Platform platform) {
        super(sender, platform);
    }
}
