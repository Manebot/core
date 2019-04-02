package io.manebot.database.model;

import io.manebot.plugin.PluginRegistration;

import javax.persistence.*;
import java.sql.SQLException;

@javax.persistence.Entity
@Table(
        indexes = {
                @Index(columnList = "pluginId,name", unique = true)
        },
        uniqueConstraints = {@UniqueConstraint(columnNames ={"pluginId","name"})}
)
public class PluginProperty extends TimedRow implements io.manebot.plugin.PluginProperty {
    @Transient
    private final io.manebot.database.Database database;
    public PluginProperty(io.manebot.database.Database database) {
        this.database = database;
    }
    public PluginProperty(io.manebot.database.Database database, Plugin plugin, String name, String value) {
        this(database);

        this.plugin = plugin;
        this.name = name;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private int pluginConfigurationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pluginId")
    private Plugin plugin;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(nullable = true)
    private String value;

    public int getPluginConfigurationId() {
        return pluginConfigurationId;
    }

    public PluginRegistration getPluginRegistration() {
        return plugin.getRegistration();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        try {
            this.value = database.executeTransaction(s -> {
                PluginProperty property = s.find(PluginProperty.class, getPluginConfigurationId());
                return property.value = value;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(pluginConfigurationId);
    }

    public void remove() {
        try {
            database.executeTransaction(s -> {
                s.remove(PluginProperty.this);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
