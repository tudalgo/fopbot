package fopbot;

/**
 * A field entity that represents coins on a graphical user interface.
 */
public class Coin extends FieldEntity {

    /**
     * The number of coins that this coin entity currently owns.
     */
    private int count;

    /**
     * Constructs and initializes a single coin at the specified {@code (x,y)} location.
     *
     * @param x the X coordinate of the newly constructed coin
     * @param y the Y coordinate of the newly constructed coin
     */
    public Coin(int x, int y) {
        super(x, y);
        count = 1;
    }

    /**
     * Constructs and initializes the coins at the specified {@code (x,y)} location.
     *
     * @param x     the X coordinate of the newly constructed coin
     * @param y     the Y coordinate of the newly constructed coin
     * @param count the number of coins that should be constructed
     */
    public Coin(int x, int y, int count) {
        super(x, y);
        this.count = count;
    }

    /**
     * Returns the current number of coins of this {@code Coin} entity.
     *
     * @return the current number of coins of this {@code Coin} entity
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the current number of coins of this coin entity to the specified number of coins.
     *
     * @param count the new number of coins for this coin entity
     */
    public void setCount(int count) {
        this.count = count;
    }
}
