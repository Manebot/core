package io.manebot.event.platform;

import io.manebot.platform.Platform;

public class PlatformUnregisteredEvent extends PlatformEvent {
    public PlatformUnregisteredEvent(Object sender, Platform platform) {
        super(sender, platform);
    }
}
