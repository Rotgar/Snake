package View;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ScoreView {
	
	private StackPane stack; 
	private Label l1, l2, l3, l4;
	/**
	 * Height of score panel
	 */
	public static final int SCORE_HEIGHT = 50;
	/**
	 * Color of score label
	 */
	private final Color scoreColor = Color.YELLOW;
	/**
	 * Color of the score panel
	 */
	private final Color scoreFieldColor = Color.rgb(100, 60, 100);
	
	public ScoreView() {
		
		stack = new StackPane();
		stack.setStyle("-fx-background-color: "+MainView.SCENE_COLOR);
		Rectangle r = new Rectangle(MainView.WIDTH - (2*SCORE_HEIGHT), SCORE_HEIGHT);
	    r.setFill(scoreFieldColor);
	    
	    // triangles on the sides
	    double x = (double)MainView.WIDTH;
	    double y = (double)SCORE_HEIGHT;
	    double z = (double)MainView.HEIGHT;
	    
	    Polygon t1 = new Polygon();
        t1.getPoints().addAll(new Double[]{
            0.0, y+z,
            y, y+z,
            y, z });
        
        Polygon t2 = new Polygon();
        t2.getPoints().addAll(new Double[]{
            x, y+z,
            x-y, y+z,
            x-y, z });
	    
	    t1.setFill(scoreFieldColor);
	    t2.setFill(scoreFieldColor);
        
	    // setting the labels
		l1 = new Label("SCORE: ");
		l1.setFont(new Font(25));
		l1.setTextFill(scoreColor);
	
		l2 = new Label("0");
		l2.setFont(new Font(25));
		l2.setTextFill(scoreColor);
		
		l3 = new Label("Press SPACE to Pause");
		l3.setFont(new Font(13));
		l3.setTextFill(scoreColor);
		
		l4 = new Label("Press ESC to Exit");
		l4.setFont(new Font(13));
		l4.setTextFill(scoreColor);
		
		stack.getChildren().addAll(r, t1, t2, l1, l2, l3, l4);		
		stack.getChildren().get(1).setTranslateX(-(r.getWidth()/2+SCORE_HEIGHT/2));
		stack.getChildren().get(2).setTranslateX(r.getWidth()/2+SCORE_HEIGHT/2);
		stack.getChildren().get(3).setTranslateX(-30);
		stack.getChildren().get(4).setTranslateX(40);
		stack.getChildren().get(5).setTranslateX(150);		
		stack.getChildren().get(5).setTranslateY(15);		
		stack.getChildren().get(6).setTranslateX(-170);		
		stack.getChildren().get(6).setTranslateY(15);
	}
	
	/**
	 * Gets the actual score and displays it on the scene
	 * @param score in game
	 */
	public void addScore(int score) {
		l2.setText(Integer.toString(score));
	}
	
	/**
	 * Returns the stack that holds all elements to be displayed on the score panel
	 * @return the stack
	 */
	public StackPane getStack() {
		return stack;
	}	
}