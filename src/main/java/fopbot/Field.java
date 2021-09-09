package fopbot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Field implements Iterable<FieldEntity> {
  private List<FieldEntity> entities;

  public Field() {
    entities = new LinkedList<>();
  }

  public Field(List<FieldEntity> entities) {
    this.entities = entities;
  }

  public List<FieldEntity> getEntities() {
    return entities;
  }

  public void setEntities(List<FieldEntity> entities) {
    this.entities = entities;
  }

  @Override
  public Iterator<FieldEntity> iterator() {
    return entities.iterator();
  }

  public int size() {
    return entities.size();
  }

  public FieldEntity get(int i) {
    return entities.get(i);
  }

  public void remove(int i) {
    entities.remove(i);
  }

  public void remove(FieldEntity entity) {
    entities.remove(entity);
  }

  public void add(FieldEntity entity) {
    entities.add(entity);
  }

  public void clear() {
    entities.clear();
  }
}
