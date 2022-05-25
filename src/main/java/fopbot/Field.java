package fopbot;

import java.util.LinkedList;
import java.util.List;

/**
 * A single field in a 2D world where field entities can be placed.
 *
 * @see FieldEntity
 */
public class Field {

    /**
     * Contains all entities that are on this field.
     */
    private final List<FieldEntity> entities;

    /**
     * Constructs and initializes a field with no entities on it.
     */
    public Field() {
        entities = new LinkedList<>();
    }

    /**
     * Constructs and initializes a field with the specified entities on it.
     *
     * @param entities the  entities that are on the newly constructed field
     */
    public Field(List<FieldEntity> entities) {
        this.entities = entities;
    }

    /**
     * Returns the entities that are on this field.
     *
     * @return the entities that are on this field
     */
    public List<FieldEntity> getEntities() {
        return entities;
    }
}
