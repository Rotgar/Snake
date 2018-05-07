package Model;

import javafx.scene.paint.Color;

public class BodyPart extends GameObject{

	/**
	 * Color of the snake's body
	 */
	public static final Color BODY_COLOR = Color.DARKBLUE;
	/**
	 * Color of snake's body when in super state
	 */
	public static final Color SUPER_BODY_COLOR = Color.DEEPSKYBLUE;	
	/**
	 * Color of snake's head
	 */
	public static final Color HEAD_COLOR = Color.ROYALBLUE;	
	
	public BodyPart(int x, int y) {
		super(x, y);
	}
}