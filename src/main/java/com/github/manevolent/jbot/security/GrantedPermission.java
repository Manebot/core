package com.github.manevolent.jbot.security;

public class GrantedPermission {
    private final Permission permission;
    private final Grant grant;

    public GrantedPermission(Permission permission, Grant grant) {
        this.permission = permission;
        this.grant = grant;
    }

    public Permission getPermission() {
        return permission;
    }

    public Grant getGrant() {
        return grant;
    }
}
