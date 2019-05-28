package io.manebot.database.model;

import io.manebot.cache.CachedValue;
import io.manebot.chat.*;
import io.manebot.command.exception.CommandExecutionException;
import io.manebot.lambda.ThrowingConsumer;
import io.manebot.platform.Platform;
import io.manebot.platform.PlatformUser;
import io.manebot.security.Grant;
import io.manebot.security.GrantedPermission;
import io.manebot.security.Permission;
import io.manebot.user.UserGroupMembership;
import io.manebot.user.UserPrompt;
import io.manebot.user.UserType;
import io.manebot.virtual.Virtual;
import io.manebot.virtual.VirtualProcess;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "username", unique = true),
                @Index(columnList = "displayName"),
                @Index(columnList = "created"),
                @Index(columnList = "updated")
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"username"})}
)
public class User extends TimedRow implements io.manebot.user.User {
    @Transient
    private final Object promptLock = new Object();
    @Transient
    private Prompt prompt;

    @Transient
    private final CachedValue<io.manebot.database.model.UserBan> banCachedValue =
            new CachedValue<>(1000L, new Supplier<io.manebot.database.model.UserBan>() {
                @Override
                public io.manebot.database.model.UserBan get() {
                    return database.execute(s -> {
                        return s.createQuery(
                                "SELECT x FROM " +
                                        io.manebot.database.model.UserBan.class.getName() + " x " +
                                        "inner join x.user u " +
                                        "where u.userId = :userId and x.end > :time and x.pardoned = false " +
                                        "order by x.end desc",
                                io.manebot.database.model.UserBan.class
                        )
                                .setParameter("userId", getUserId())
                                .setParameter("time", (int) (System.currentTimeMillis() / 1000L))
                                .setMaxResults(1)
                                .getResultList()
                                .stream().findFirst().orElse(null);
                    });
                }
            });

    @Transient
    private final io.manebot.database.Database database;
    public User(io.manebot.database.Database database) {
        this.database = database;
    }

    public User(io.manebot.database.Database database,
                Entity entity,
                String username,
                UserType type) {
        this(database);

        this.entity = entity;
        this.username = username;
        this.userType = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int userId;

    @Column(nullable = false, name = "userTypeId")
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;

    @Column(length = 64, nullable = false)
    private String username;

    @Column(length = 24, nullable = true)
    private String displayName;

    @Column(nullable = true)
    private Integer lastSeen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entityId")
    private Entity entity;

    @Override
    public String getDisplayName() {
        return displayName == null || displayName.length() <= 0 ? username : displayName;
    }

    @Override
    public Date getRegisteredDate() {
        return getCreatedDate();
    }

    @Override
    public Date getLastSeenDate() {
        return lastSeen == null ? null : new Date((long) lastSeen * 1000L);
    }

    @Override
    public void setLastSeenDate(Date date) {
        setLastSeen((int) (date.getTime() / 1000));
    }

    @Override
    public Collection<UserGroupMembership> getMembership() {
        return Collections.unmodifiableCollection(database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + UserGroup.class.getName() + " x " +
                            "inner join x.user u " +
                            "where u.userId = :userId",
                    UserGroup.class
            ).setParameter("userId", getUserId()).getResultList();
        }));
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalArgumentException {
        try {
            this.displayName = database.executeTransaction(s -> {
                User user = s.find(User.class, getUserId());
                user.displayName = displayName;
                return displayName;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void setLastSeen(int lastSeen) {
        try {
            database.executeTransaction(s -> {
                User user = s.find(User.class, getUserId());
                user.lastSeen = lastSeen;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public io.manebot.entity.Entity getEntity() {
        return entity;
    }

    // Override for efficiency
    @Override
    public UserAssociation getUserAssociation(Platform platform, String id) {
        return database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + UserAssociation.class.getName() + " x " +
                            "inner join x.platform p " +
                            "where p.id = :platformId and x.id = :userId",
                    UserAssociation.class
            )
                    .setMaxResults(1)
                    .setParameter("platformId", platform.getId())
                    .setParameter("userId", id)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        });
    }

    public Collection<io.manebot.user.UserAssociation> getAssociations() {
        return Collections.unmodifiableCollection(database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + UserAssociation.class.getName() + " x " +
                            "inner join x.user u " +
                            "where u.userId = :userId",
                    UserAssociation.class
            ).setParameter("userId", getUserId()).getResultList();
        }));
    }

    @Override
    public Collection<io.manebot.user.UserBan> getBans() {
        return Collections.unmodifiableCollection(database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + io.manebot.database.model.UserBan.class.getName() + " x " +
                            "inner join x.user u " +
                            "where u.userId = :userId",
                    io.manebot.database.model.UserBan.class
            ).setParameter("userId", getUserId()).getResultList();
        }));
    }

    @Override
    public Collection<io.manebot.user.UserBan> getIssuedBans() {
        return Collections.unmodifiableCollection(database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + io.manebot.database.model.UserBan.class.getName() + " x " +
                            "inner join x.banningUser u " +
                            "where u.userId = :userId",
                    io.manebot.database.model.UserBan.class
            ).setParameter("userId", getUserId()).getResultList();
        }));
    }

    @Override
    public io.manebot.user.UserBan getBan() {
        // Cache this value so spamming doesn't spam queries
        return banCachedValue.get();
    }

    @Override
    public io.manebot.user.UserBan ban(String reason, Date date) throws SecurityException {
        io.manebot.security.Permission.checkPermission("system.user.ban");

        if (Virtual.getInstance().currentProcess().getUser() == this)
            throw new SecurityException("Cannot ban own user");

        if (getType() == UserType.SYSTEM &&
                Virtual.getInstance().currentProcess().getUser().getType() != UserType.SYSTEM)
            throw new SecurityException("Cannot ban system user");

        if (getBan() != null)
            throw new IllegalArgumentException("User is already banned");

        User banningUser = (User) Virtual.getInstance().currentProcess().getUser();

        try {
            return database.executeTransaction(s -> {
                User userAttached = s.find(User.class, getUserId());
                User banningUserAttached = s.find(User.class, banningUser.getUserId());

                io.manebot.database.model.UserBan userBan =
                        new io.manebot.database.model.UserBan(
                                database,
                                userAttached,
                                banningUserAttached,
                                (int) (date.getTime() / 1000),
                                reason
                        );

                s.persist(userBan);

                // Unset ban cached value
                banCachedValue.unset();

                return userBan;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public io.manebot.user.UserBan ban(String reason) throws SecurityException {
        Date oneMonthAgo = new Date(System.currentTimeMillis() - (2419200000L));
        long bans = getBans().stream().filter(ban -> ban.getDate().after(oneMonthAgo)).count();

        return ban(reason, new Date(System.currentTimeMillis() + (300 * (10 ^ (bans)) * 1_000)));
    }

    @Override
    public UserAssociation createAssociation(
            Platform platform,
            String platformUserId
    ) {
        UserAssociation association =
                getUserAssociation(platform, platformUserId);

        if (association != null) return association;

        try {
            association = database.executeTransaction(s -> {
                UserAssociation newAssocation =
                        new UserAssociation(
                                database,
                                (io.manebot.database.model.Platform) platform,
                                platformUserId,
                                User.this
                        );

                s.persist(newAssocation);

                return newAssocation;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return association;
    }

    @Override
    public boolean removeAssociation(Platform platform, String platformUserId) {
        UserAssociation association =
                getUserAssociation(platform, platformUserId);

        if (association != null) {
            try {
                database.executeTransaction(s -> {
                    s.remove(association);
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return true;
        } else return false;
    }

    @Override
    public UserType getType() {
        return userType;
    }

    @Override
    public boolean setType(UserType userType) {
        if (this.userType == userType) return false;

        try {
            return database.executeTransaction(s -> {
                User user = s.find(User.class, getUserId());
                user.userType = userType;
                return true;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ChatMessage> broadcastMessage(Function<ChatSender, Collection<ChatMessage>> receiver) {
        return getAssociations().stream()
                .map(io.manebot.user.UserAssociation::getPlatformUser)
                .filter(Objects::nonNull)
                .map(platformUser -> {
                    Chat privateChat = platformUser.getPrivateChat();
                    if (privateChat == null) return null;
                    return platformUser.createSender(privateChat);
                })
                .filter(Objects::nonNull)
                .flatMap(sender -> receiver.apply(sender).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Prompt getPrompt() {
        Prompt prompt = this.prompt;
        if (prompt != null && prompt.hasExpired())
            return null;
        else
            return prompt;
    }

    @Override
    public UserPrompt prompt(Consumer<UserPrompt.Builder> consumer) throws IllegalStateException {
        synchronized (promptLock) {
            Prompt prompt = getPrompt(); // should return null if prompt is expired or completed
            if (prompt != null) throw new IllegalStateException("A user prompt is already pending.");

            PromptBuilder builder = new PromptBuilder();
            consumer.accept(builder);
            Prompt generatedPrompt = builder.build();

            Collection<ChatMessage> messages = broadcastMessage((receiver) -> {
                Chat chat = receiver.getChat();
                PlatformUser user = receiver.getPlatformUser();

                if (receiver.canSendEmbeds()) {
                    return receiver.sendMessage(messageBuilder -> {
                        if (chat.getFormat().shouldMention(user))
                            messageBuilder.message(textBuilder -> textBuilder.appendMention(user));

                        messageBuilder.embed(embedBuilder ->
                                embedBuilder
                                        .title(generatedPrompt.getName())
                                        .description(generatedPrompt.getDescription().andThen(textBuilder -> {
                                            if (textBuilder.hasContent())
                                                textBuilder.newLine();

                                            textBuilder
                                                    .append("To confirm this prompt, run the \"")
                                                    .append("confirm", EnumSet.of(TextStyle.ITALICS))
                                                    .append("\"")
                                                    .append(" command.");
                                        }))
                                .footer(textBuilder -> {
                                    io.manebot.user.User generator = generatedPrompt.getUser();
                                    String username = generator != null ?
                                            generator.getDisplayName() :
                                            "an anonymous user";

                                    textBuilder
                                            .append("Generated by ")
                                            .append(username, EnumSet.of(TextStyle.BOLD));
                                })
                        );
                    });
                } else {
                    return receiver.sendFormattedMessage(textBuilder -> {
                        if (textBuilder.getFormat().shouldMention(user))
                            textBuilder.appendMention(user).newLine();

                        textBuilder.append("A new confirmation is pending for your user:").newLine();
                        textBuilder.append(generatedPrompt.getName()).newLine();

                        if (generatedPrompt.getDescription() != null)
                            generatedPrompt.getDescription().andThen(TextBuilder::newLine).accept(
                                    textBuilder.append(" ")
                            );

                        io.manebot.user.User generator = generatedPrompt.getUser();
                        String username = generator != null ?
                                generator.getDisplayName() :
                                "an anonymous user";

                        textBuilder
                                .append(" (Generated by ")
                                .append(username, EnumSet.of(TextStyle.BOLD))
                                .append(")").newLine();

                        textBuilder
                                .append("To confirm this prompt, run the \"")
                                .append("confirm", EnumSet.of(TextStyle.ITALICS))
                                .append("\"")
                                .append(" command.").newLine();
                    });
                }
            });

            if (messages.size() <= 0)
                throw new IllegalStateException(getDisplayName() + " could not be contacted.");

            return this.prompt = generatedPrompt;
        }
    }

    @Override
    public boolean hasPermission(Permission permission, Grant fallback) {
        // If the user is a system user, all permissions are ignored.
        if (getType() == UserType.SYSTEM) return true;

        // Get existing permission (always shortcuts fallback)
        GrantedPermission existing = getEntity().getPermission(permission);
        if (existing != null) return existing.getGrant() == Grant.ALLOW;

        // Look through groups with explicit DENY flattening
        Collection<Grant> groupGrants = getGroups().stream()
                .map(group -> {
                    GrantedPermission grantedPermission = group.getEntity().getPermission(permission);
                    if (grantedPermission == null) return null;
                    return grantedPermission.getGrant();
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // DENY first, then ALLOW
        if (groupGrants.contains(Grant.DENY)) return false;
        else if (groupGrants.contains(Grant.ALLOW)) return true;

        // Fallback, no explicit permissions were supplied.
        return fallback == Grant.ALLOW;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    private class Prompt implements UserPrompt {
        private final io.manebot.user.User caller;
        private final String name;
        private final Consumer<TextBuilder> descriptionBuilder;
        private final ThrowingConsumer<UserPrompt, CommandExecutionException> callback;
        private final long expires = System.nanoTime() + (60L * 1000L * 1000L * 1000L);

        private boolean completed = false;

        private Prompt(io.manebot.user.User caller,
                       String name,
                       Consumer<TextBuilder> descriptionBuilder,
                       ThrowingConsumer<UserPrompt, CommandExecutionException> callback) {
            this.caller = caller;
            this.name = name;
            this.descriptionBuilder = descriptionBuilder;
            this.callback = callback;
        }

        @Override
        public io.manebot.user.User getUser() {
            return User.this;
        }

        @Override
        public io.manebot.user.User getCaller() {
            return caller;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Consumer<TextBuilder> getDescription() {
            return descriptionBuilder;
        }

        @Override
        public void complete() throws CommandExecutionException {
            try {
                callback.acceptChecked(this);
            } finally {
                completed = true;
            }
        }

        public boolean hasExpired() {
            return completed || System.nanoTime() >= expires;
        }
    }

    private class PromptBuilder implements UserPrompt.Builder {
        private final io.manebot.user.User caller;

        private String name;
        private Consumer<TextBuilder> descriptionBuilder = textBuilder -> { };
        private ThrowingConsumer<UserPrompt, CommandExecutionException> callback;

        private PromptBuilder() {
            VirtualProcess process = Virtual.getInstance().currentProcess();
            if (process != null)
                caller = process.getUser();
            else
                caller = null;
        }

        @Override
        public io.manebot.user.User getUser() {
            return User.this;
        }

        @Override
        public io.manebot.user.User getCaller() {
            return caller;
        }

        @Override
        public UserPrompt.Builder setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public UserPrompt.Builder setDescription(Consumer<TextBuilder> textBuilder) {
            this.descriptionBuilder = textBuilder;
            return this;
        }

        @Override
        public UserPrompt.Builder setCallback(ThrowingConsumer<UserPrompt, CommandExecutionException> callback) {
            this.callback = callback;
            return this;
        }

        private Prompt build() {
            if (name == null) throw new NullPointerException("name");
            if (callback == null) throw new NullPointerException("callback");

            return new Prompt(caller, name, descriptionBuilder, callback);
        }
    }
}
