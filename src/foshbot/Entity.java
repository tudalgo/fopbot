package foshbot;

import foshbot.anim.Drawable;

public abstract class Entity {

    protected int x;
    protected int y;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Drawable d);

    /**
     * Update internal state
     *
     * @param dt Times passed since last update
     * @return True iff done updating
     */
    public boolean update(double dt) {
        return true;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
