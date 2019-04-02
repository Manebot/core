package io.manebot.database.model;

import io.manebot.platform.PlatformRegistration;
import io.manebot.user.User;
import io.manebot.user.UserAssociation;

import javax.persistence.*;
import java.util.Collection;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "id", unique = true),
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"id"})}
)
public class Platform extends TimedRow implements io.manebot.platform.Platform {
    /**
     * Platform connection
     */
    @Transient
    private PlatformRegistration registration;

    @Transient
    private final io.manebot.database.Database database;
    public Platform(io.manebot.database.Database database) {
        this.database = database;
    }

    public Platform(io.manebot.database.Database database, String id) {
        this(database);

        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int platformId;

    @Column(length = 64, nullable = false)
    private String id;

    public int getPlatformId() {
        return platformId;
    }

    @Override
    public PlatformRegistration getRegistration() {
        return registration;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public UserAssociation getUserAssocation(String id) {
        return database.execute(s -> {
            return s.createQuery(
                    "SELECT x FROM " + io.manebot.database.model.UserAssociation.class.getName() + " x " +
                            "inner join x.platform p " +
                            "where p.id = :platformId and x.id = :userId",
                    io.manebot.database.model.UserAssociation.class
            )
                    .setMaxResults(1)
                    .setParameter("platformId", getId())
                    .setParameter("userId", id)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public Collection<UserAssociation> getUserAssociations(User user) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public Collection<UserAssociation> getUserAssociations() {
        throw new UnsupportedOperationException(); // TODO
    }

    public io.manebot.plugin.Plugin getPlugin() {
        return this.registration == null ? null : this.registration.getPlugin();
    }

    public void setRegistration(PlatformRegistration registration) {
        this.registration = registration;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(platformId);
    }
}
