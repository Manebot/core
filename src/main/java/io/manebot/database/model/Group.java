package io.manebot.database.model;

import io.manebot.user.UserGroup;
import io.manebot.user.UserGroupMembership;
import io.manebot.user.UserType;
import io.manebot.virtual.Virtual;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "groupId", unique = true),
                @Index(columnList = "name", unique = true),
                @Index(columnList = "owningUserId"),
                @Index(columnList = "entityId"),
                @Index(columnList = "created"),
                @Index(columnList = "updated")
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"name"})}
)
public class Group extends TimedRow implements UserGroup {
    @Transient
    private final io.manebot.database.Database database;
    public Group(io.manebot.database.Database database) {
        this.database = database;
    }
    public Group(io.manebot.database.Database database, io.manebot.database.model.Entity entity, String name, User owner) {
        this(database);

        this.entity = entity;
        this.name = name;
        this.owningUser = owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int groupId;

    @Column(length = 64, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owningUserId")
    private User owningUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entityId")
    private io.manebot.database.model.Entity entity;

    public io.manebot.database.model.Entity getEntity() {
        return entity;
    }

    public void setEntity(io.manebot.database.model.Entity entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    @Override
    public io.manebot.user.User getOwner() {
        return owningUser;
    }

    @Override
    public Collection<UserGroupMembership> getMembership() {
        return Collections.unmodifiableCollection(database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + io.manebot.database.model.UserGroup.class.getName() + " x " +
                            "inner join x.group g "+
                            "where g.groupId = :groupId",
                    io.manebot.database.model.UserGroup.class
            ).setParameter("groupId", getGroupId()).getResultList();
        }));
    }

    @Override
    public UserGroupMembership getMembership(io.manebot.user.User user) {
        return database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + io.manebot.database.model.UserGroup.class.getName() + " x " +
                            "inner join x.user u " +
                            "inner join x.group g " +
                            "where u.userId = :userId and g.groupId = :groupId",
                    io.manebot.database.model.UserGroup.class
            )
                    .setMaxResults(1)
                    .setParameter("userId", ((User)user).getUserId())
                    .setParameter("groupId", this.getGroupId())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public void addUser(io.manebot.user.User user) throws SecurityException {
        User addingUser = (User) Virtual.getInstance().currentUser();
        if (addingUser.getType() != UserType.SYSTEM && getOwner() != addingUser)
            throw new SecurityException("Cannot control group");

        try {
            database.executeTransaction(s -> {
                io.manebot.database.model.UserGroup userGroup =
                        new io.manebot.database.model.UserGroup(
                                database,
                                (User) user,
                                this,
                                addingUser
                        );

                s.persist(userGroup);

                return userGroup;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public void setOwner(io.manebot.user.User user) {
        User addingUser = (User) Virtual.getInstance().currentUser();
        if (addingUser.getType() != UserType.SYSTEM && getOwner() != addingUser)
            throw new SecurityException("Cannot control group");

        try {
            database.executeTransaction(s -> {
                Group group = s.find(Group.class, getGroupId());
                group.owningUser = owningUser;
                group.setUpdated(System.currentTimeMillis());
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(groupId);
    }
}
