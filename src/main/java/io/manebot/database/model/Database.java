package io.manebot.database.model;

import javax.persistence.*;

@javax.persistence.Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "pluginId", "name" })
        },
        indexes = {
                @Index(columnList = "name", unique = false),
                @Index(columnList = "pluginId,name", unique = true)
        }
)
public class Database extends TimedRow {
    @Transient
    private final io.manebot.database.Database database;
    public Database(io.manebot.database.Database database) {
        this.database = database;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "databaseId")
    private int databaseId;

    @Column(length = 64, nullable = false)
    private String name;

    @ManyToOne(optional = true)
    @JoinColumn(name = "pluginId")
    private Plugin plugin;

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(databaseId);
    }
}
