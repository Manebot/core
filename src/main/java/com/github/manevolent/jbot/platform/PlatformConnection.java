package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.user.User;
import com.github.manevolent.jbot.user.UserAssociation;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface PlatformConnection {

    /**
     * Gets a list of user associations for this platform.
     *
     * @return collection of user associations.
     */
    Collection<UserAssociation> getUserAssociations();

    /**
     * Gets a list of chats associated with this platform.
     * @return collection of Chat instances associated with this platform.
     */
    Collection<Chat> getChats();

    /**
     * Gets a chat by its user-specific ID.
     * @param id Chat ID to search for.
     * @return Chat instance if found, null otherwise.
     */
    default Chat getChatById(String id) {
        return getChats().stream()
                .filter(chat -> chat.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets an immutable collection of chats where the specified user is a member.
     *
     * @param member User member to search for.
     * @return Chat instances whose members contain the specified User.
     */
    default Collection<Chat> getChatsByMember(User member) {
        return Collections.unmodifiableCollection(
                getChats().stream()
                    .filter(chat -> chat.isMember(member))
                    .collect(Collectors.toList())
        );
    }

    /**
     * Gets a specific assocation for this platform.
     *
     * @param id Platform-specific user ID to search for.
     * @return user association if found on this platform, null otherwise.
     */
    default UserAssociation getUserAssocation(String id) {
        return getUserAssociations().stream()
                .filter(assoc -> assoc.getPlatformId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets a set of user associations for the specified user.
     *
     * @param user User to search for.
     * @return collection of user associations for the specified uesr.
     */
    default Collection<UserAssociation> getUserAssociations(User user) {
        return Collections.unmodifiableCollection(
                getUserAssociations().stream()
                .filter(assoc -> assoc.getUser().equals(user)).collect(Collectors.toList())
        );
    }

}
