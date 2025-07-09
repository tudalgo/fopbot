package fopbot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A 2D field in a virtual world that can contain various entities ({@link FieldEntity}).
 */
public class Field {

    /**
     * The virtual world this field belongs to.
     */
    private final @NotNull KarelWorld world;

    /**
     * The x-coordinate of this field in the virtual world.
     */
    private final int x;

    /**
     * The y-coordinate of this field in the virtual world.
     */
    private final int y;

    /**
     * The entities currently placed on this field.
     */
    private final @NotNull List<FieldEntity> entities;

    /**
     * A supplier that provides the background color of this field.
     */
    private Supplier<@Nullable Color> fieldColorSupplier = () -> null;

    /**
     * Constructs a field at the given coordinates in the specified world.
     *
     * @param world the world this field belongs to
     * @param x     the x-coordinate of this field
     * @param y     the y-coordinate of this field
     */
    public Field(final @NotNull KarelWorld world, final int x, final int y) {
        this.entities = new LinkedList<>();
        this.world = world;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a field with predefined entities at the given coordinates in the specified world.
     *
     * @param world    the world this field belongs to
     * @param x        the x-coordinate of this field
     * @param y        the y-coordinate of this field
     * @param entities a list of entities to be placed on the field
     */
    public Field(final @NotNull KarelWorld world, final int x, final int y, final @NotNull List<FieldEntity> entities) {
        this(world, x, y);
        this.entities.addAll(entities);
    }

    /**
     * Returns the world this field belongs to.
     *
     * @return the world instance
     */
    public @NotNull KarelWorld getWorld() {
        return world;
    }

    /**
     * Returns the x-coordinate of this field.
     *
     * @return the x-coordinate
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
     * Returns the list of entities currently on this field.
     *
     * @return the list of entities
     */
    public @NotNull List<FieldEntity> getEntities() {
        return entities;
    }

    /**
     * Returns the current background color of this field.
     *
     * @return the field color, or {@code null} if none is set
     */
    public @Nullable Color getFieldColor() {
        return fieldColorSupplier.get();
    }

    /**
     * Sets a background color for this field.
     *
     * @param fieldColor the color to be used as background
     */
    public void setFieldColor(final @Nullable Color fieldColor) {
        setFieldColor(() -> fieldColor);
    }

    /**
     * Sets a background color for this field.
     *
     * @param fieldColorSupplier a supplier that returns the color for the background
     */
    public void setFieldColor(final Supplier<@Nullable Color> fieldColorSupplier) {
        this.fieldColorSupplier = fieldColorSupplier;
        Optional.ofNullable(world.getGuiPanel()).ifPresent(GuiPanel::updateGui);
    }

    /**
     * Checks whether this field contains any entity of the given class type.
     *
     * @param clazz the class to check for
     *
     * @return {@code true} if an entity of the given type is present
     */
    public boolean containsEntity(@NotNull Class<? extends FieldEntity> clazz) {
        return entities.stream().anyMatch(clazz::isInstance);
    }

    /**
     * Checks whether this field contains the specified entity.
     *
     * @param entity the entity to check
     *
     * @return {@code true} if the entity is on this field
     */
    public boolean containsEntity(@NotNull FieldEntity entity) {
        return entities.contains(entity);
    }

    /**
     * Removes the specified entity from this field.
     *
     * @param entity the entity to remove
     */
    public void removeEntity(@NotNull FieldEntity entity) {
        entities.remove(entity);
    }

    /**
     * Removes the first entity on this field that is an instance of the given class.
     *
     * @param clazz the class of the entity to remove
     */
    public void removeEntity(@NotNull Class<? extends FieldEntity> clazz) {
        var it = entities.iterator();
        while (it.hasNext()) {
            if (clazz.isInstance(it.next())) {
                it.remove();
                break;
            }
        }
    }
}
