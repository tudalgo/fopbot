package fopbot;

public class Wall extends FieldEntity {

    private final boolean horizontal;

    /**
     * Creates a new horizontal/vertical wall at field (x,y)
     */
    public Wall(int x, int y, boolean horizontal) {
        super(x, y);
        this.horizontal = horizontal;
    }

    protected boolean isHorizontal() {
        return horizontal;
    }
}
