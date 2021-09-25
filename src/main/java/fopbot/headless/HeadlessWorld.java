package fopbot.headless;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;
import fopbot.impl.AbstractWorld;
import fopbot.impl.Block;
import fopbot.impl.CoinStack;
import fopbot.impl.Grid;

public class HeadlessWorld extends AbstractWorld {

  public HeadlessWorld(Grid grid) {
    super(grid);
  }

  @Override
  protected CoinStack newCoinStack(int x, int y) {
    return new CoinStack(x, y);
  }

  @Override
  public void putBlock(int x, int y) {
    getEntities(x, y).add(new Block(x, y));
  }

  @Override
  public Robot newRobot(int x, int y, Direction dir, int numberOfCoins) {
    return new HeadlessRobot(x, y, dir, numberOfCoins, this);
  }

  @Override
  public void start() {}

  @Override
  public boolean isRunning() {
    return true;
  }

  @Override
  public void stop() {}
}
