package io.manebot.database.model;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Arrays;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "entityId,name", unique = true)
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"entityId","name"})}
)
public class Property extends TimedRow {
    @Transient
    private final io.manebot.database.Database database;
    public Property(io.manebot.database.Database database) {
        this.database = database;
    }

    public Property(io.manebot.database.Database database,
                    io.manebot.database.model.Entity entity,
                    String name) {
        this(database);

        this.entity = entity;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int propertyId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entityId")
    private io.manebot.database.model.Entity entity;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = true, length = 2048)
    private byte[] value;

    public String getName() {
        return name;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) throws SQLException {
        if (!Arrays.equals(this.value, value)) {
            this.value = database.executeTransaction(s -> {
                Property property = s.find(Property.class, getPropertyId());
                property.value = value;
                property.setUpdated(System.currentTimeMillis());
                return value;
            });
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(propertyId);
    }
}
