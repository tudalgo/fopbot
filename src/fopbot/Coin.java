package fopbot;

public class Coin extends FieldEntity {

	private int count;

	/**
	 * * Creates a new coin at field (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public Coin(int x, int y) {
		super(x, y);
		count = 1;
	}

	/**
	 * * Creates new coins at field (x,y)
	 * 
	 * @param x
	 * @param y
	 * @param count
	 */
	public Coin(int x, int y, int count) {
		super(x, y);
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
