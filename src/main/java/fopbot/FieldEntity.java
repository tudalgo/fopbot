package fopbot;

/**
 * Abstract parent class of every entity (robot, coin, block etc.) on the field
 */
public abstract class FieldEntity {

    private int x;
    private int y;

    public FieldEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
