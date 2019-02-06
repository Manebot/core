package com.github.manevolent.jbot.database;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;
import com.mongodb.client.MongoCollection;

import java.util.Date;

public interface Database {

    String getName();

    ArtifactIdentifier getSubject();

    Date getCreated();

    boolean hasCollection(String name);

    <T> MongoCollection<T> getCollection(String name, Class<T> type);

    MongoCollection createCollection(String name);

}
