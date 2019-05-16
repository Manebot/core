package io.manebot.database.model;

import io.manebot.security.Grant;
import io.manebot.security.GrantedPermission;
import io.manebot.virtual.Virtual;
import com.google.common.collect.MapMaker;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "entityTypeId"),
                @Index(columnList = "created"),
                @Index(columnList = "updated")
        }
)
public class Entity extends TimedRow implements io.manebot.entity.Entity {
    @Transient
    private final Map<String, io.manebot.property.Property> propertyMap = new MapMaker().weakValues().makeMap();

    @Transient
    private final io.manebot.database.Database database;
    public Entity(io.manebot.database.Database database) {
        this.database = database;
    }

    public Entity(io.manebot.database.Database database, EntityType type) {
        this(database);

        this.entityType = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int entityId;

    @Column(nullable = false, name = "entityTypeId")
    @Enumerated(EnumType.ORDINAL)
    private EntityType entityType;

    public int getEntityId() {
        return entityId;
    }

    @Override
    public String getName() {
        return String.format("entity/%d", getEntityId());
    }

    @Override
    public Collection<io.manebot.property.Property> getProperties() {
        return Collections.unmodifiableCollection(database.execute(s -> {
            return s.createQuery(
                    "SELECT p FROM " + io.manebot.database.model.Property.class.getName() + " p " +
                            "inner join p.entity e " +
                            "where e.entityId = :entityId",
                    io.manebot.database.model.Property.class
            ).setParameter("entityId", entityId).getResultList()
                    .stream()
                    .map(databaseType -> getProperty(databaseType.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }));
    }

    @Override
    public io.manebot.property.Property getProperty(String node) {
        return propertyMap.computeIfAbsent(node, VirtualProperty::new);
    }

    @Override
    public Permission getPermission(String node) {
        Objects.requireNonNull(node);
        if (node.length() <= 0) return null;

        String[] nodeComponents = node.split("\\.");
        List<String> testNodes = new ArrayList<>(nodeComponents.length + 1);
        List<String> buffer = new ArrayList<>();
        testNodes.add("*");
        for (String component : nodeComponents) {
            buffer.add(component);

            if (buffer.size() < nodeComponents.length)
                testNodes.add(String.join(".", buffer) + ".*");
        }
        testNodes.add(node);

        return database.execute(s -> {
            return s.createQuery(
                    "SELECT p FROM " + Permission.class.getName() + " p " +
                            "inner join p.entity e " +
                            "where e.entityId = :entityId and p.node IN :nodes " +
                            "order by length(p.node) desc", /* most specific */
                    Permission.class
            )
                    .setMaxResults(1)
                    .setParameter("entityId", entityId)
                    .setParameter("nodes", testNodes)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        });
    }

    @Override
    public GrantedPermission setPermission(String node, Grant grant) throws SecurityException {
        io.manebot.security.Permission.checkPermission(node);

        Permission permission = getPermission(node);

        if (permission == null) {
            try {
                permission = database.executeTransaction(tran -> {
                    Permission newPermission = new Permission(
                            database,
                            this,
                            (User) Virtual.getInstance().currentProcess().getUser(),
                            node,
                            grant == Grant.ALLOW
                    );

                    tran.persist(newPermission);

                    return newPermission;
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (permission.getGrant() != grant) {
            permission.setGrant(grant);
        }

        return permission;
    }

    @Override
    public void removePermission(String s) {
        Permission permission = getPermission(s);
        if (permission == null) throw new IllegalArgumentException("Permission not found");
        permission.remove();
    }

    @Override
    public Collection<GrantedPermission> getPermissions() {
        return database.execute(s -> {
            return s.createQuery(
                    "SELECT p FROM " + Permission.class.getName() + " p " +
                            "inner join p.entity e " +
                            "where e.entityId = :entityId",
                    Permission.class
            )
                    .setParameter("entityId", entityId)
                    .getResultList()
                    .stream()
                    .map(x -> (GrantedPermission) x)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(entityId);
    }

    private class VirtualProperty extends BinaryProperty implements io.manebot.property.Property {
        private static final String ENCODING = "UTF-16";
        private final String name;
        private final Object accessLock = new Object();

        private io.manebot.database.model.Property property;

        private VirtualProperty(String name) {
            this.name = name;
            this.property = getProperty();
        }

        private io.manebot.database.model.Property getProperty() {
            if (property == null)
                property = database.execute(s -> {
                    return s.createQuery(
                            "SELECT p FROM " + io.manebot.database.model.Property.class.getName() + " p " +
                                    "inner join p.entity e " +
                                    "where e.entityId = :entityId and p.name = :name",
                            io.manebot.database.model.Property.class
                    )
                            .setMaxResults(1)
                            .setParameter("entityId", entityId)
                            .setParameter("name", name)
                            .getResultList()
                            .stream()
                            .findFirst()
                            .orElse(null);
                });

            return property;
        }

        private byte[] getValue() {
            synchronized (accessLock) {
                if (property == null) {
                    return null;
                } else
                    return property.getValue();
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void unset() {
            synchronized (accessLock) {
                if (property == null) return;

                try {
                    this.property = database.executeTransaction(s -> {
                        io.manebot.database.model.Property attachedProperty =
                                s.find(io.manebot.database.model.Property.class,
                                        property.getPropertyId());

                        if (attachedProperty != null)
                            s.remove(attachedProperty);

                        return null;
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean isNull() {
            return getValue() == null;
        }

        @Override
        public int size() {
            byte[] value = getValue();
            if (value == null) return -1;
            return value.length;
        }

        @Override
        public int write(byte[] bytes, int offs, int len) {
            byte[] b;

            if (offs == 0 && len == bytes.length)
                b = bytes;
            else {
                b = new byte[len];
                System.arraycopy(bytes, 0, b, offs, len);
            }

            synchronized (accessLock) {
                if (property == null) {
                    try {
                        this.property = property = database.executeTransaction(s -> {
                            Entity entity = s.find(Entity.class, getEntityId());

                            io.manebot.database.model.Property newProperty =
                                    new io.manebot.database.model.Property(
                                            database,
                                            entity,
                                            name
                                    );

                            s.persist(newProperty);

                            return newProperty;
                        });
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    property.setValue(b);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return len;
        }

        @Override
        public int read(byte[] bytes, int offs, int len) {
            byte[] b = getValue();
            if (b == null) return 0;
            System.arraycopy(b, 0, bytes, offs, len);
            return len;
        }
    }
}
