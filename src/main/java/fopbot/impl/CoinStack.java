package fopbot.impl;

import fopbot.Entity;

public class CoinStack extends Entity {

  private int numberOfCoins;

  public CoinStack(int x, int y) {
    super(x, y);
    this.numberOfCoins = 0;
  }

  public void putCoins(int numberOfCoins) {
    this.numberOfCoins += numberOfCoins;
  }

  public void pickCoins(int numberOfCoins) {
    this.numberOfCoins -= numberOfCoins;
  }

  public int getNumberOfCoins() {
    return numberOfCoins;
  }
}
