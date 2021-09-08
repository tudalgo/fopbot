package foshbot.impl;

import foshbot.Entity;

public class CoinStack extends Entity {

    public int numberOfCoins;

    public CoinStack(int x, int y) {
        super(x, y);
        this.numberOfCoins = 0;
    }
}
