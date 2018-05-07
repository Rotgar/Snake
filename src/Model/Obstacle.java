package Model;

import javafx.scene.paint.Color;

public class Obstacle extends GameObject{
	
	/**
	 * Color of obstacles
	 */
	public static final Color OBSTACLE_COLOR = Color.BROWN;
	
	/**
	 * How many obstacles from beginning
	 */
	public final static int OBSTACLES_START_NUMBER = 3;
	
	public Obstacle(int x, int y) {
		super(x, y);
	}

}
