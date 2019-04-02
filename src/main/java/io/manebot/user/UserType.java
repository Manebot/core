package io.manebot.user;

public enum UserType {
    SYSTEM(true, false),
    COMMON(true, true),
    ANONYMOUS(false, false);

    private final boolean canLogin, canExecuteCommands;

    UserType(boolean canLogin, boolean canExecuteCommands) {
        this.canLogin = canLogin;
        this.canExecuteCommands = canExecuteCommands;
    }

    public boolean canLogin() {
        return canLogin;
    }

    public boolean canExecuteCommands() {
        return canExecuteCommands;
    }
}