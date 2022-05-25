package fopbot;

import java.util.LinkedList;
import java.util.List;

public class Field {
    private final List<FieldEntity> entities;

    public Field() {
        entities = new LinkedList<>();
    }

    public Field(List<FieldEntity> entities) {
        this.entities = entities;
    }

    public List<FieldEntity> getEntities() {
        return entities;
    }
}
