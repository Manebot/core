package io.manebot.database.model;

import io.manebot.security.Grant;
import io.manebot.security.GrantedPermission;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Date;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "entityId,node", unique = true),
                @Index(columnList = "node")
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"entityId","node"})}
)
public class Permission extends TimedRow implements GrantedPermission {
    @Transient
    private final io.manebot.database.Database database;
    public Permission(io.manebot.database.Database database) {
        this.database = database;
    }

    public Permission(io.manebot.database.Database database,
                      io.manebot.database.model.Entity entity,
                      User grantingUser,
                      String node,
                      boolean allow) {
        this(database);

        this.entity = entity;
        this.grantingUser = grantingUser;
        this.node = node;
        this.allow = allow;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int permissionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entityId")
    private io.manebot.database.model.Entity entity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grantingUserId")
    private User grantingUser;

    @Column(length = 64, nullable = false)
    private String node;

    @Column(nullable = false)
    private boolean allow;

    public int getPermissionId() {
        return permissionId;
    }

    public io.manebot.database.model.Entity getEntity() {
        return entity;
    }

    @Override
    public io.manebot.security.Permission getPermission() {
        return io.manebot.security.Permission.get(node);
    }

    @Override
    public Grant getGrant() {
        return Grant.fromValue(allow);
    }

    public void setGrant(Grant grant) {
        try {
            database.executeTransaction(s -> {
                Permission permission = s.find(Permission.class, getPermissionId());
                permission.allow = grant == Grant.ALLOW;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public io.manebot.user.User getGranter() {
        return grantingUser;
    }

    @Override
    public Date getDate() {
        return getCreatedDate();
    }

    @Override
    public void remove() throws SecurityException {
        try {
            database.executeTransaction(s -> {
                Permission permission = s.find(Permission.class, getPermissionId());
                s.remove(permission);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(permissionId);
    }
}
