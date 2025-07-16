package fopbot;

/**
 * Represents a coin entity placed on the field in the virtual world.
 *
 * <p>Coins can be collected by other entities (e.g. robots), and each coin
 * may represent a specific count or value.
 */
public class Coin extends FieldEntity {

    /**
     * The default count for a coin entity if not specified.
     */
    private static final int DEFAULT_COUNT = 1;

    /**
     * The number of coins represented by this entity.
     */
    private int count;

    /**
     * Constructs a {@code Coin} with a default count of {@value DEFAULT_COUNT} at the given coordinates.
     *
     * @param x the x-coordinate of the coin
     * @param y the y-coordinate of the coin
     */
    public Coin(final int x, final int y) {
        super(x, y);
        this.count = DEFAULT_COUNT;
    }

    /**
     * Constructs a {@code Coin} with the specified count at the given coordinates.
     *
     * @param x     the x-coordinate of the coin
     * @param y     the y-coordinate of the coin
     * @param count the number of coins represented
     */
    public Coin(final int x, final int y, final int count) {
        super(x, y);
        this.count = count;
    }

    /**
     * Returns the number of coins represented by this entity.
     *
     * @return the coin count of this entity
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of coins represented by this entity.
     *
     * @param count the new coin count of this entity
     */
    public void setCount(final int count) {
        this.count = count;
    }
}
