package fopbot;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFrame;

public class KarelWorld {

	private static boolean doScreenshots = false;
	private static boolean saveStates = true;

	private LinkedList<LinkedList<FieldEntity>> entityStates;

	private int height;
	private int width;
	private int delay = 100;
	private int robotCount;

	private JFrame guiFrame;
	private GuiPanel guiGp;

	private LinkedList<FieldEntity>[][] entities;

	private HashMap<Class<? extends Robot>, HashMap<String, Image[]>> robotImages;
	private HashMap<String, HashMap<String, Image[]>> robotImagesById;

	/**
	 * Creates a new world with the given width and height
	 * 
	 * @param width
	 * @param height
	 */
	@SuppressWarnings("unchecked")
	public KarelWorld(int width, int height) {
		if (width < 1 || height < 1) {
			throw new RuntimeException("Invalid world size: " + width + "x" + height);
		}

		this.height = height;
		this.width = width;

		robotImages = new HashMap<Class<? extends Robot>, HashMap<String, Image[]>>();
		setAndLoadRobotImages(Robot.class, getClass().getResourceAsStream("/res/trianglebot.png"),
				getClass().getResourceAsStream("/res/trianglebot.png"), 0, 0);
		robotImagesById = new HashMap<String, HashMap<String, Image[]>>();

		LinkedList<FieldEntity> lst = new LinkedList<FieldEntity>();
		entities = (LinkedList<FieldEntity>[][]) Array.newInstance(lst.getClass(), width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				entities[i][j] = new LinkedList<FieldEntity>();
			}
		}
	}

	/**
	 * @return the worlds height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the worlds width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the delay a robot has to wait after each action
	 * 
	 * @param delay (in ms)
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the current delay (in ms)
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * @param x
	 * @param y
	 * @return a list of all field entities at field (x,y)
	 */
	protected LinkedList<FieldEntity> getFieldEntities(int x, int y) {
		return entities[x][y];
	}

	/**
	 * @return a list of all field entities in the world
	 */
	public LinkedList<FieldEntity> getAllFieldEntities() {
		LinkedList<FieldEntity> all = new LinkedList<FieldEntity>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				LinkedList<FieldEntity> field = entities[i][j];
				if (field.size() > 0) {
					all.addAll(field);
				}
			}
		}
		return all;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if a block is in field (x,y)
	 */
	protected boolean isBlockInField(int x, int y) {
		for (FieldEntity ce : entities[x][y]) {
			if (ce instanceof Block) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param horizontal
	 * @return true if a wall is in field (x,y) and if wall.isHorizontal() ==
	 *         horizontal
	 */
	protected boolean isWallInField(int x, int y, boolean horizontal) {
		for (FieldEntity ce : entities[x][y]) {
			if (ce instanceof Wall) {
				Wall w = (Wall) ce;
				if (w.isHorizontal() == horizontal) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if at least one coin is in field (x,y)
	 */
	protected boolean isCoinInField(int x, int y) {
		for (FieldEntity ce : entities[x][y]) {
			if (ce instanceof Coin) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param r
	 * @return true if another robot is in field (x,y)
	 */
	protected boolean isAnotherRobotInField(int x, int y, Robot r) {
		for (FieldEntity ce : entities[x][y]) {
			if (ce instanceof Robot) {
				Robot a = (Robot) ce;
				if (a != r) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @return Tries to remove one coin from the field (x,y) and returns true if a
	 *         coin got removed
	 */
	protected boolean pickCoin(int x, int y) {
		LinkedList<FieldEntity> field = entities[x][y];
		for (FieldEntity ce : field) {
			if (ce instanceof Coin) {
				// if coins already placed in this field, decrease number
				Coin c = (Coin) ce;
				if (c.getCount() > 1) {
					c.setCount(c.getCount() - 1);
				} else {
					field.remove(ce);
				}
				triggerUpdate();
				return true;
			}
		}
		return false;
	}

	/**
	 * Puts down N coins at field (x,y)
	 * 
	 * @param x
	 * @param y
	 * @param numberOfCoins
	 */
	public void putCoins(int x, int y, int numberOfCoins) {
		checkXCoordinate(x);
		checkYCoordinate(y);
		checkNumberOfCoins(numberOfCoins);
		if (numberOfCoins < 1) {
			throw new RuntimeException("Number of coins must be greater than 0!");
		}

		LinkedList<FieldEntity> field = entities[x][y];
		for (FieldEntity ce : field) {
			if (ce instanceof Coin) {
				// if coins already placed in this field, increase number
				Coin c = (Coin) ce;
				c.setCount(c.getCount() + numberOfCoins);
				triggerUpdate();
				return;
			}
		}
		// else place first coin
		Coin c = new Coin(x, y, numberOfCoins);
		field.add(c);
		triggerUpdate();
	}

	/**
	 * Updates the entity array to be in sync with the robots x- and y-Coordinates
	 * 
	 * @param r
	 * @param oldX
	 * @param oldY
	 */
	protected void updateRobotField(Robot r, int oldX, int oldY) {
		for (int i = 0; i < entities[oldX][oldY].size(); i++) {
			if (entities[oldX][oldY].get(i) == r) {
				entities[oldX][oldY].remove(i);
				entities[r.getX()][r.getY()].add(r);
				return;
			}
		}
	}

	/**
	 * Places a block at field (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public void placeBlock(int x, int y) {
		checkXCoordinate(x);
		checkYCoordinate(y);

		LinkedList<FieldEntity> field = entities[x][y];
		for (FieldEntity ce : field) {
			if (ce instanceof Block) {
				return;
			}
		}
		Block b = new Block(x, y);
		field.add(b);
		triggerUpdate();
	}

	/**
	 * Adds a robot to the world
	 * 
	 * @param r
	 */
	public void addRobot(Robot r) {
		LinkedList<FieldEntity> field = entities[r.getX()][r.getY()];
		field.add(r);
		r.setId(Integer.toString(robotCount));
		robotCount++;
		triggerUpdate();
		sleep();
	}

	/**
	 * Places a wall at field (x,y) with (wall.getHorizontal() == horizontal) ==
	 * true
	 * 
	 * @param x
	 * @param y
	 * @param horizontal
	 */
	private void placeWall(int x, int y, boolean horizontal) {
		checkXCoordinate(x);
		checkYCoordinate(y);

		LinkedList<FieldEntity> field = entities[x][y];
		for (FieldEntity ce : field) {
			if (ce instanceof Wall) {
				Wall cew = (Wall) ce;
				if (cew.isHorizontal() == horizontal) {
					return;
				}
			}
		}
		Wall w = new Wall(x, y, horizontal);
		field.add(w);
		triggerUpdate();
	}

	/**
	 * Places a horizontal wall at field (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public void placeHorizontalWall(int x, int y) {
		placeWall(x, y, true);
	}

	/**
	 * Places a vertical wall at field (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public void placeVerticalWall(int x, int y) {
		placeWall(x, y, false);
	}

	/**
	 * Sleep for delay ms
	 */
	protected void sleep() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return all entity states
	 */
	public LinkedList<LinkedList<FieldEntity>> getEntityStates() {
		return entityStates;
	}

	/**
	 * Saves the current entity state
	 */
	private void saveEntityState() {
		if (entityStates == null) {
			entityStates = new LinkedList<LinkedList<FieldEntity>>();
		}
		// copy entities
		LinkedList<FieldEntity> allEntities = getAllFieldEntities();
		LinkedList<FieldEntity> allEntitiesCopy = new LinkedList<FieldEntity>();
		for (FieldEntity fe : allEntities) {
			if (fe instanceof Robot) {
				Robot r = (Robot) fe;
				Robot copy = new Robot(true, r.getX(), r.getY(), r.getDirection(), r.getNumberOfCoins());
				copy.setId(String.valueOf(r.hashCode()));
				allEntitiesCopy.add(copy);
			}

			if (fe instanceof Coin) {
				Coin c = (Coin) fe;
				Coin copy = new Coin(c.getX(), c.getY(), c.getCount());
				allEntitiesCopy.add(copy);
			}

			if (fe instanceof Block) {
				Block b = (Block) fe;
				Block copy = new Block(b.getX(), b.getY());
				allEntitiesCopy.add(copy);
			}

			if (fe instanceof Wall) {
				Wall w = (Wall) fe;
				Wall copy = new Wall(w.getX(), w.getY(), w.isHorizontal());
				allEntitiesCopy.add(copy);
			}
		}
		entityStates.add(allEntitiesCopy);
	}

	/**
	 * Triggers an update of the gui's window
	 */
	protected void triggerUpdate() {
		if (saveStates) {
			saveEntityState();
		}

		updateGui();
	}

	/**
	 * Updates the gui's window
	 */
	protected void updateGui() {
		if (isVisible() == false) {
			return;
		}

		if (doScreenshots) {
			guiGp.saveStateAsPng();
		}

		guiGp.updateGui();
	}

	/**
	 * Reset the world (remove all entities)
	 */
	public void reset() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				entities[i][j].clear();
			}
		}
		triggerUpdate();
	}

	/**
	 * If true, show the world's gui
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {

		if (visible == true && guiFrame == null) {
			guiFrame = new JFrame("FopBot");
			guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			if (guiGp == null) {
				guiGp = new GuiPanel(this);
			}
			guiFrame.add(guiGp);
			guiFrame.pack();
			guiFrame.setVisible(true);
			triggerUpdate();
		} else {

			if (visible == false && guiFrame != null && guiFrame.isVisible()) {
				guiFrame.setVisible(false);
				guiFrame.dispose();
				guiFrame = null;
				guiGp = null;
			}

		}
	}

	/**
	 * @return true if the gui is visible
	 */
	public boolean isVisible() {
		return guiFrame != null && guiFrame.isVisible();
	}

	/**
	 * Loads robot images for a specific robot class
	 * 
	 * @param robotClass
	 * @param turnedOn
	 * @param turnedOff
	 * @param rotationOffsetOn
	 * @param rotationOffsetOff
	 */
	public void setAndLoadRobotImages(Class<? extends Robot> robotClass, InputStream turnedOn, InputStream turnedOff,
			int rotationOffsetOn, int rotationOffsetOff) {
		Image[] turnedOnImages = null;
		Image[] turnedOffImages = null;

		try {
			turnedOnImages = PaintUtils.loadScaleRotateFieldImage(turnedOn, rotationOffsetOn);
		} catch (IOException e) {
			System.err.println("Could not load robot image! " + turnedOn);
		}

		try {
			turnedOffImages = PaintUtils.loadScaleRotateFieldImage(turnedOff, rotationOffsetOff);
		} catch (IOException e) {
			System.err.println("Could not load robot image! " + turnedOff);
		}

		HashMap<String, Image[]> imageMap = new HashMap<String, Image[]>();
		imageMap.put("on", turnedOnImages);
		imageMap.put("off", turnedOffImages);

		robotImages.put(robotClass, imageMap);
	}

	/**
	 * Loads robot images for a specific image id
	 * 
	 * @param imageId
	 * @param turnedOn
	 * @param turnedOff
	 * @param rotationOffsetOn
	 * @param rotationOffsetOff
	 */
	public void setAndLoadRobotImagesById(String imageId, InputStream turnedOn, InputStream turnedOff,
			int rotationOffsetOn, int rotationOffsetOff) {
		Image[] turnedOnImages = null;
		Image[] turnedOffImages = null;

		try {
			turnedOnImages = PaintUtils.loadScaleRotateFieldImage(turnedOn, rotationOffsetOn);
		} catch (IOException e) {
			System.err.println("Could not load robot image! " + turnedOn);
		}

		try {
			turnedOffImages = PaintUtils.loadScaleRotateFieldImage(turnedOff, rotationOffsetOff);
		} catch (IOException e) {
			System.err.println("Could not load robot image! " + turnedOff);
		}

		HashMap<String, Image[]> imageMap = new HashMap<String, Image[]>();
		imageMap.put("on", turnedOnImages);
		imageMap.put("off", turnedOffImages);

		robotImagesById.put(imageId, imageMap);
	}

	/**
	 * @param robotClass
	 * @return a robot image map for a specific robot class
	 */
	protected HashMap<String, Image[]> getRobotImageMap(Class<? extends Robot> robotClass) {
		return robotImages.get(robotClass);
	}

	/**
	 * @param robotId
	 * @return a robot image map for a specific image id
	 */
	protected HashMap<String, Image[]> getRobotImageMapById(String imageId) {
		return robotImagesById.get(imageId);
	}

	/**
	 * Validates a given x-coordinate
	 * 
	 * @param x
	 */
	protected void checkXCoordinate(int x) {
		if (x > World.getWidth() - 1 || x < 0) {
			throw new RuntimeException("Invalid x-coordinate: " + x);
		}
	}

	/**
	 * Validates a given y-coordinate
	 * 
	 * @param y
	 */
	protected void checkYCoordinate(int y) {
		if (y > World.getHeight() - 1 || y < 0) {
			throw new RuntimeException("Invalid y-coordinate: " + y);
		}
	}

	/**
	 * Validates numberOfCoins
	 * 
	 * @param numberOfCoins
	 */
	protected void checkNumberOfCoins(int numberOfCoins) {
		if (numberOfCoins < 0) {
			throw new RuntimeException("Number of coins must be greater than -1!");
		}
	}

	public GuiPanel getGuiPanel() {
		return guiGp;
	}

	public void setGuiPanel(GuiPanel guiPanel) {
		this.guiGp = guiPanel;
	}

}
