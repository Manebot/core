package io.manebot.event;

public abstract class CheckedEvent extends Event {
    private boolean canceled = false;

    public CheckedEvent(Object sender) {
        super(sender);
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
