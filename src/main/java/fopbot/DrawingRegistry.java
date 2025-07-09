package fopbot;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A registry that maps {@link FieldEntity} types to their corresponding {@link Drawable} implementations.
 * It also defines the order in which field entities should be drawn using a {@link Comparator}.
 *
 * <p>The drawing order is determined by the comparator: entities with lower values are drawn first (i.e., appear below),
 * and entities with higher values are drawn later (i.e., appear on top).
 *
 * <p>The default drawing order is: Wall &lt; Robot &lt; Coin &lt; Block.
 *
 * @param drawings a map of entity classes to their drawing logic
 * @param order    the comparator used to define the rendering priority of field entities
 */
public record DrawingRegistry(
    @NotNull Map<Class<? extends FieldEntity>, Drawable<?>> drawings,
    @NotNull Comparator<? super FieldEntity> order
) {

    /**
     * A fallback drawing implementation used when no specific drawable is registered for an entity class.
     */
    public static final @NotNull Drawable<FieldEntity> FALLBACK_DRAWING = new DefaultDrawing();

    /**
     * A default registry containing the standard entity-to-drawing mappings and drawing order.
     */
    public static final @NotNull DrawingRegistry DEFAULT = DrawingRegistry.builder()
        .addAll(
            Map.ofEntries(
                Map.entry(Block.class, new BlockDrawing()),
                Map.entry(Coin.class, new CoinDrawing()),
                Map.entry(Robot.class, new RobotDrawing()),
                Map.entry(Wall.class, new WallDrawing())
            )
        ).build(Comparator.comparingInt(DrawingRegistry::getDrawingOrder));

    /**
     * Creates a new drawing registry by copying an existing one.
     *
     * @param parent the registry to copy
     */
    public DrawingRegistry(final @NotNull DrawingRegistry parent) {
        this(new HashMap<>(parent.drawings), parent.order);
    }

    /**
     * Determines the drawing priority for a given {@link FieldEntity}.
     * Lower values are drawn first (appear behind others).
     *
     * @param entity the field entity to retrieve its drawing priority
     *
     * @return an integer representing the drawing priority
     */
    private static int getDrawingOrder(final @NotNull FieldEntity entity) {
        return switch (entity) {
            case final Wall w -> 0;
            case final Robot r -> 1;
            case final Coin c -> 2;
            case final Block b -> 3;
            default -> Integer.MAX_VALUE;
        };
    }

    /**
     * Creates a new {@link DrawingRegistryBuilder} initialized with the drawings from the given parent registry.
     *
     * @param parent the existing {@link DrawingRegistry} to initialize from
     *
     * @return a new {@link DrawingRegistryBuilder} pre-populated with the parent's drawings
     */

    public static DrawingRegistryBuilder builder(final @NotNull DrawingRegistry parent) {
        return new DrawingRegistryBuilder(parent.drawings);
    }

    /**
     * Creates a new empty {@link DrawingRegistryBuilder}.
     *
     * @return a new empty {@link DrawingRegistryBuilder}
     */
    public static DrawingRegistryBuilder builder() {
        return new DrawingRegistryBuilder();
    }

    /**
     * Returns the {@link Drawable} associated with the given entity class. If no exact match is found, it checks
     * superclasses recursively.
     * Falls back to {@link #FALLBACK_DRAWING} if nothing is found.
     *
     * @param clazz the class of the field entity
     *
     * @return the corresponding drawable
     */
    public Drawable<?> getDrawing(final @NotNull Class<? extends FieldEntity> clazz) {
        if (drawings.containsKey(clazz)) {
            return drawings.get(clazz);
        }

        // In case we need alternative drawings
        for (Class<?> parent = clazz.getSuperclass(); parent != null; parent = parent.getSuperclass()) {
            if (drawings.containsKey(parent)) {
                return drawings.get(parent);
            }
        }

        // If we do not found any drawing
        return FALLBACK_DRAWING;
    }

    /**
     * A default drawing for field entities that were not added or found in the {@link DrawingRegistry}.
     */
    private static class DefaultDrawing implements Drawable<FieldEntity> {

        @Override
        public void draw(final @NotNull Graphics g, final @NotNull DrawingContext<? extends FieldEntity> context) {
            final Color oldColor = g.getColor();
            final ColorProfile colorProfile = context.colorProfile();
            final Point upperLeftCorner = context.upperLeftCorner();

            g.setColor(Color.BLACK);
            final int size = colorProfile.fieldInnerSize() - colorProfile.fieldInnerOffset() * 2;
            g.fillRect(
                scale(upperLeftCorner.x, context),
                scale(upperLeftCorner.y, context),
                scale(size, context),
                scale(size, context)
            );

            g.setColor(oldColor);
        }
    }

    /**
     * A builder for creating instances of {@link DrawingRegistry}.
     *
     * <p>This builder allows you to add drawables to the registry, which are associated with the {@link FieldEntity} classes
     * they belong to. You can add individual drawables or a collection of them, and then build the {@link DrawingRegistry}
     * using the {@link #build(Comparator)} method, which requires a custom comparator for sorting the field entities.
     */
    public static class DrawingRegistryBuilder {

        /**
         * A map that associates {@link FieldEntity} classes with their corresponding {@link Drawable}.
         */
        private final @NotNull Map<Class<? extends FieldEntity>, Drawable<?>> drawings;

        /**
         * Constructs a builder with the provided initial collection of drawables associated with the classes
         * they belong to.
         *
         * @param drawings a map of {@link FieldEntity} subclasses to their corresponding {@link Drawable} instances
         */
        public DrawingRegistryBuilder(final @NotNull Map<Class<? extends FieldEntity>, Drawable<?>> drawings) {
            this.drawings = drawings;
        }

        /**
         * Constructs a builder with an empty collection of drawables.
         */
        public DrawingRegistryBuilder() {
            this(new HashMap<>());
        }

        /**
         * Adds a drawable to the builder for a specific {@link FieldEntity} subclass.
         *
         * @param clazz   the {@link FieldEntity} subclass for which the drawable is to be associated
         * @param drawing the {@link Drawable} instance to be associated with the provided class
         *
         * @return this builder instance after adding the drawable
         */
        public DrawingRegistryBuilder add(final @NotNull Class<? extends FieldEntity> clazz, final Drawable<?> drawing) {
            drawings.put(clazz, drawing);
            return this;
        }

        /**
         * Adds multiple drawables to the builder for different {@link FieldEntity} subclasses.
         *
         * @param drawings a map of {@link FieldEntity} subclasses to their corresponding {@link Drawable} instances
         *
         * @return this builder instance after adding the drawables
         */
        public DrawingRegistryBuilder addAll(final @NotNull Map<Class<? extends FieldEntity>, Drawable<?>> drawings) {
            this.drawings.putAll(drawings);
            return this;
        }

        /**
         * Builds a new {@link DrawingRegistry} instance using the provided comparator to define the drawing order
         * of {@link FieldEntity} instances.
         *
         * @param order a comparator used to determine the drawing order of field entities
         *
         * @return a new {@link DrawingRegistry} instance containing the drawables and their drawing order
         */
        public DrawingRegistry build(final @NotNull Comparator<? super FieldEntity> order) {
            return new DrawingRegistry(drawings, order);
        }
    }
}
