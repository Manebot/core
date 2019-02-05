package com.github.manevolent.jbot.event.platform;

import com.github.manevolent.jbot.platform.Platform;

public class PlatformRegisteredEvent extends PlatformEvent {
    public PlatformRegisteredEvent(Object sender, Platform platform) {
        super(sender, platform);
    }
}
