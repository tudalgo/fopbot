package fopbot.global;


import fopbot.Direction;
import fopbot.global.World;
import fopbot.global.Robot;

public class FillRectangle {

	public static void main(String[] args) {
		new World(12, 10);

		Robot rex = new Robot(1, 9, Direction.EAST, 90);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				rex.putCoin();
				rex.move();
			}
			rex.turnLeft();
			rex.move();
			rex.turnLeft();
			for (int k = 0; k < 10; k++) {
				rex.move();
			}
			rex.turnLeft();
			rex.turnLeft();
		}
	}

}
