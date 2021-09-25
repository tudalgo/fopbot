package fopbot.global;

import fopbot.Direction;
import fopbot.global.Robot;

public class Chess {

	public static void main(String[] args) {
		Robot chess = new Robot(1, 9, Direction.EAST, 32 );

    for (int i = 0; i < 4; i++) {
		     for (int j = 0; j < 8; j++) {
		          if ( ( chess.getX() + chess.getY() ) % 2 == 0 )
		               chess.putCoin();
		          chess.move();
		     }
		     chess.turnLeft();
		     chess.move();
		     chess.turnLeft();
		     chess.move();
		     for (int k = 0; k < 8; k++) {
		          if ( ( chess.getX() + chess.getY() ) % 2 == 0 )
		               chess.putCoin();
		          chess.move();
		     }
		     chess.turnLeft();
		     chess.turnLeft();
		     chess.turnLeft();
		     chess.move();
		     chess.turnLeft();
		     chess.turnLeft();
		     chess.turnLeft();
		     chess.move();

		}


	}

}
