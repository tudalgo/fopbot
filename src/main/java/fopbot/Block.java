package fopbot;


/**
 * Represents a non-passable block that fully occupies a field in the virtual world.
 *
 * <p>A {@code Block} prevents any entity from entering the field it occupies.</p>
 */

public class Block extends FieldEntity {

    /**
     * Constructs a new {@code Block} at the specified (x, y) coordinates.
     *
     * @param x the x-coordinate of the block
     * @param y the y-coordinate of the block
     */
    public Block(int x, int y) {
        super(x, y);
    }
}
