package io.manebot.entity;

public interface EntityManager {

    /**
     * Gets an entity by its ID.
     * @param id Entity ID.
     * @return Entity instance.
     */
    Entity getEntityById(String id);

}
