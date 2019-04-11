package io.manebot.database.model;

import io.manebot.user.UserGroupMembership;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Date;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "userId,groupId", unique = true),
                @Index(columnList = "created"),
                @Index(columnList = "updated")
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"userId", "groupId"})}
)
public class UserGroup extends TimedRow implements UserGroupMembership {
    @Transient
    private final io.manebot.database.Database database;
    public UserGroup(io.manebot.database.Database database) {
        this.database = database;
    }
    public UserGroup(io.manebot.database.Database database,
                     User user,
                     Group group,
                     User addingUser) {
        this(database);

        this.user = user;
        this.group = group;
        this.addingUser = addingUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int xrefId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "groupId")
    private Group group;

    @ManyToOne(optional = true)
    @JoinColumn(name = "addingUserId")
    private User addingUser;

    @Column(nullable = false)
    private int created;

    @Column(nullable = true)
    private Integer updated;

    public int getUserGroupId() {
        return xrefId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    public User getAddingUser() {
        return addingUser;
    }

    @Override
    public Date getAddedDate() {
        return getCreatedDate();
    }

    @Override
    public void remove() throws SecurityException {
        try {
            database.executeTransaction(s -> {
                s.remove(UserGroup.this);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(xrefId);
    }
}
