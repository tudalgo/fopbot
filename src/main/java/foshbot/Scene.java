package foshbot;

import foshbot.impl.Grid;

public interface Scene {

    Grid getGrid();

    void init(World world);

    void run(World world);
}
