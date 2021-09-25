package fopbot.global;

import fopbot.World;
import fopbot.impl.Grid;

public interface GlobalRunner {

  World createWorld(Grid grid);
}
