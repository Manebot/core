package com.github.manevolent.jbot.security;

import com.github.manevolent.jbot.entity.Entity;
import com.github.manevolent.jbot.user.User;

import java.util.Date;

public interface GrantedPermission {

    Entity getEntity();

    Permission getPermission();

    Grant getGrant();

    User getGranter();

    Date getDate();

}
