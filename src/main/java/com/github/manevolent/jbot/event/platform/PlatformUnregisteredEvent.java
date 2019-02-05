package com.github.manevolent.jbot.event.platform;

import com.github.manevolent.jbot.platform.Platform;

public class PlatformUnregisteredEvent extends PlatformEvent {
    public PlatformUnregisteredEvent(Object sender, Platform platform) {
        super(sender, platform);
    }
}
