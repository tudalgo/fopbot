package foshbot.examples;

import foshbot.Direction;
import foshbot.Scene;
import foshbot.World;
import foshbot.impl.Grid;

public class LangtonsAntExample implements Scene {
    @Override
    public Grid getGrid() {
        return new Grid(100, 100);
    }

    @Override
    public void init(World world) {
    }

    @Override
    public void run(World world) {
        var ant = world.newRobot(world.getWidth() / 2, world.getHeight() / 2, Direction.NORTH, 999);

        while (ant.getNumberOfCoins() > 0 && ant.isFrontClear()) {
            if (ant.isNextToACoin()) {
                ant.pickCoin();
                ant.pickCoin();

                ant.turnLeft();
                ant.turnLeft();
                ant.turnLeft();
            } else {
                ant.putCoin();
                ant.putCoin();

                ant.turnLeft();

                ant.pickCoin();
                ant.pickCoin();

                ant.putCoin();
                ant.putCoin();
            }
            ant.move();
        }
    }
}
