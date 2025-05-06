package fopbot;

import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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

    // Lambda that provides color
    private Supplier<@Nullable Color> fieldColorSupplier = () -> null;

    private final KarelWorld world;

    private final int x;

    private final int y;

    /**
     * Constructs a field with the given coordinate and no entities on it.
     *
     * @param world the world
     * @param x     the x-coordinate
     * @param y     the x-coordinate
     */
    public Field(final KarelWorld world, final int x, final int y) {
        entities = new LinkedList<>();
        this.world = world;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a field with the given coordinate and the specified entities on it.
     *
     * @param world    the world
     * @param x        the x-coordinate
     * @param y        the x-coordinate
     * @param entities the entities
     */
    public Field(final KarelWorld world, final int x, final int y, final List<FieldEntity> entities) {
        this(world, x, y);
        this.entities.addAll(entities);
    }

    /**
     * Returns the entities that are on this field.
     *
     * @return the entities that are on this field
     */
    public List<FieldEntity> getEntities() {
        return entities;
    }

    /**
     * Returns {@code true} if this field contains an instance of the specified {@link FieldEntity} class.
     *
     * @param clazz the class of the {@link FieldEntity} to check for
     *
     * @return {@code true} if an entity of the specified class is present; {@code false} otherwise
     */
    public boolean contains(Class<? extends FieldEntity> clazz) {
        return entities.stream().anyMatch(clazz::isInstance);
    }

    /**
     * Returns {@code true} if this field contains the specified {@link FieldEntity}.
     *
     * @param entity the {@link FieldEntity} to check for
     *
     * @return {@code true} if the entity is present; {@code false} otherwise
     */
    public boolean contains(FieldEntity entity) {
        return entities.contains(entity);
    }

    /**
     * Removes the specified {@link FieldEntity} from this field.
     *
     * @param entity the {@link FieldEntity} to remove
     */
    public void removeEntity(FieldEntity entity) {
        entities.remove(entity);
    }

    /**
     * Removes the first instance of the specified {@link FieldEntity} class from this field.
     *
     * @param clazz the class of the {@link FieldEntity} to remove
     */
    public void removeEntity(Class<? extends FieldEntity> clazz) {
        var it = entities.iterator();
        while (it.hasNext()) {
            if (clazz.isInstance(it.next())) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Sets the background color of this {@link Field} to the color provided by the specified
     * {@link Supplier}.
     * <p>If the specified {@link Supplier} returns {@code null}, the background color of this {@link Field}
     * will be reset to the default color.</p>
     * <p>Use this method to set the background color of this {@link Field} dynamically.</p>
     *
     * @param fieldColorSupplier the {@link Supplier} that provides the background color of this {@link Field}
     *                           or {@code null} to reset the background color to the default color
     */
    public void setFieldColor(final Supplier<@Nullable Color> fieldColorSupplier) {
        this.fieldColorSupplier = fieldColorSupplier;
        Optional.ofNullable(world.getGuiPanel()).ifPresent(GuiPanel::updateGui);
    }

    /**
     * Sets the background color of this {@link Field} to the specified color.
     * <p>If the specified color is {@code null}, the background color of this {@link Field}
     * will be reset to the default color.</p>
     *
     * @param fieldColor the new background color of this {@link Field}
     */
    public void setFieldColor(final @Nullable Color fieldColor) {
        setFieldColor(() -> fieldColor);
    }

    /**
     * Returns the background color of this {@link Field}.
     *
     * @return the background color of this {@link Field}
     */
    public @Nullable Color getFieldColor() {
        return fieldColorSupplier.get();
    }

    /**
     * Returns the x-coordinate of this field.
     *
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this field.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the world of this field.
     *
     * @return the world
     */
    public KarelWorld getWorld() {
        return world;
    }
}
