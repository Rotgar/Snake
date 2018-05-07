package View;

import java.util.ArrayList;

import Controller.Controller;
import Model.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView{

	/**
	 * Width of the game board
	 */
	public final static int WIDTH = 600;
	/**
	 * Height of the game board
	 */
	public final static int HEIGHT = 600;
	/**
	 * Color of the scene
	 */
	public final static String SCENE_COLOR = "yellow";
	/**
	 * Snake object
	 */
	private Snake snake;
	/**
	 * Object to hold the actual scene
	 */
	private Scene scene;
	/**
	 * The stage
	 */
	private Stage stage;
	/**
	 * Actual state of the game
	 */
	private GameState state;
	/**
	 * Array to hold fruits passed by board class
	 */
	private ArrayList<Fruit> fruits;
	/**
	 * Object that holds the super fruit by board class
	 */
	private SuperFruit sFruit;
	/**
	 * Array of obstacles passed by board class
	 */
	private ArrayList<Obstacle> obstacles;
	/**
	 * Board object
	 */
	private Board board;
	private Group g;
	private Pane canvas;
	private GridPane grid;
	private StackPane stack;
	private Color bodyColor;
	/**
	 * Default constructor initializes the basic variables and objects
	 */
	public MainView() {
		
		board = new Board();
		snake = board.getSnake();
		fruits = board.getFruits();
		sFruit = board.getSuperFruit();
		obstacles = board.getObstacles();
		
		stage = new Stage();
		stage.setTitle("Snake");
		
		canvas = new Pane();
		canvas.setStyle("-fx-background-color: "+SCENE_COLOR);
        canvas.setPrefSize(WIDTH,HEIGHT);
        
		stack = new StackPane();
		grid = new GridPane();
		
	    g = new Group();
		scene = new Scene(g, WIDTH, HEIGHT + ScoreView.SCORE_HEIGHT);
		scene.setFill(Color.web(SCENE_COLOR));
		
		render();
	}
	
	/**
	 * The render method, that displays the graphics
	 */
	public void render() {
			
		this.state = Controller.getState(); // get the actual game state
		switch(state) { // checks for actual game state
			
			case Started:	// if game state is Started display the starting screen
				whenStarted();
				break;
			case Running:	
				whenRunning(); // if Running show the board, snake, objects, etc.
				break;
			case Paused: // if Paused show the pause screen
				whenPaused();
				break;
			case Finished: // if Finished show the ending game screen and display the score
				whenFinished();
				break;
			default:
				break;
		}
	} 
	
	/**
	 * Method to get the scene
	 * @return Returns the actual scene
	 */
	public Scene getScene() {
		return stage.getScene();
	}
	/**
	 * Method to get the stage
	 * @return Returns the game stage
	 */
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * Method for displaying starting screen
	 */
	private void whenStarted() {
		
		g = new Group();
		Text largeText = new Text(WIDTH/2 - 170, HEIGHT/2 - 30, "Snake Game");
		largeText.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 50));
		Text smallText = new Text(WIDTH/2 - 130, HEIGHT/2 + 20 , "Press ENTER to play");
		smallText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 25));
		smallText.setFill(Color.DARKGREEN);
		g.getChildren().addAll(smallText, largeText);
		scene.setRoot(g);
		stage.setScene(scene);
	}
	
	/**
	 * Main render method to display the actual running game  
	 */
	private void whenRunning() {
		
		grid.getChildren().clear(); // clear grid	
		canvas.getChildren().clear(); // clear canvas  
		stack = board.getScoreView().getStack(); // get the graphic panel created for score
		sFruit = board.getSuperFruit(); // get the super fruit, to display if not null
		
		int helpX, helpY, snakeY, snakeX; // variables for loops
		
		// snake's head to canvas
		Circle c = new Circle(snake.getHead().getX() , snake.getHead().getY(), GameObject.SIZE/2); 
		c.setFill(BodyPart.HEAD_COLOR);
		canvas.getChildren().add(c);
		
		// if snake is in super state set the right color
		if(board.getSuperState()) 
			bodyColor = BodyPart.SUPER_BODY_COLOR;
		else 
			bodyColor = BodyPart.BODY_COLOR;
		
		// snake's body to canvas
		for(int i = 1; i < snake.getSize(); ++i) {
			snakeX = snake.getBodyPart(i).getX();
			snakeY = snake.getBodyPart(i).getY();
			c = new Circle(snakeX , snakeY, GameObject.SIZE/2); 
			c.setFill(bodyColor);
			canvas.getChildren().add(c);
		}
		// loading fruits to canvas
		for(int i = 0; i < fruits.size(); ++i) {
			helpX = fruits.get(i).getX();
			helpY = fruits.get(i).getY();
			c = new Circle(helpX , helpY, GameObject.SIZE/2); 
			c.setFill(Fruit.FRUIT_COLOR);
			canvas.getChildren().add(c);
		}
		// loading the super fruit to canvas
		if(sFruit != null) {			
			c = new Circle(sFruit.getX() , sFruit.getY(), GameObject.SIZE/2); 
			c.setFill(SuperFruit.SUPER_FRUIT_COLOR);
			canvas.getChildren().add(c);
		}
		// loading obstacles to canvas
		for(int i = 0; i < obstacles.size(); ++i) {
			helpX = obstacles.get(i).getX();
			helpY = obstacles.get(i).getY();
			Rectangle r = new Rectangle(helpX - (GameObject.SIZE/2) , helpY - (GameObject.SIZE/2) , GameObject.SIZE, GameObject.SIZE); 
			r.setFill(Obstacle.OBSTACLE_COLOR);
			canvas.getChildren().add(r);
		}
		// adding canvas that holds the game objects, and stack that holds the score display, together
	
		grid.add(stack, 0, 1);
		grid.add(canvas, 0, 0);
		
		scene.setRoot(grid);		  
	    stage.setScene(scene);
	}

	/**
	 * Method invoked when game is paused
	 */
	private void whenPaused() {
		
		g = new Group();
		Circle c1, c2;
		Rectangle r;
		Text largeText, smallText, t1, t2, t3, t4;
		
		largeText = new Text(WIDTH/2 - 190, HEIGHT/2 - 30, "Game Paused");
		largeText.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 50));
		smallText = new Text(WIDTH/2 - 190, HEIGHT/2 + 30, "Press SPACE to resume");
		smallText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
		smallText.setFill(Color.DARKGREEN);
		
		c1 = new Circle(WIDTH/2 - 200, HEIGHT/2 + 150, GameObject.SIZE/2, Fruit.FRUIT_COLOR);
		c2 = new Circle(WIDTH/2 - 200, HEIGHT/2 + 190, GameObject.SIZE/2, SuperFruit.SUPER_FRUIT_COLOR);
		r = new Rectangle(WIDTH/2 - 210, HEIGHT/2 + 220, GameObject.SIZE, GameObject.SIZE);
		r.setFill(Obstacle.OBSTACLE_COLOR);
		
		t1 = new Text(WIDTH/2 - 180, HEIGHT/2 + 154, "- normal fruit for 1 point");
		t1.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 13));	
		t2 = new Text(WIDTH/2 - 180, HEIGHT/2 + 194, "- super fruit gives 3 points and puts Snake into super state");
		t2.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 13));				
		t3 = new Text(WIDTH/2 - 180, HEIGHT/2 + 234, "- obstacle, hitting it ends game");
		t3.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 13));
		t4 = new Text(WIDTH/2 - 270, HEIGHT/2 + 270, "Super state - normal fruits give 2 points and snake is immune to obstacles");
		t4.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 13));		
		
		g.getChildren().addAll(smallText, largeText, c1, c2, r, t1, t2, t3, t4);
		scene.setRoot(g);
		stage.setScene(scene);
	}
	
	/**
	 * Shows the finish screen when game is ended
	 */
	private void whenFinished() {
		
		g = new Group();
		Text largeText = new Text(WIDTH/2 - 220, HEIGHT/2 - 60, "Game Over");
		largeText.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 70));
		largeText.setFill(Color.RED);
		Text largeText2 = new Text(WIDTH/2 - 170, HEIGHT/2 + 20, "FINAL SCORE: " + board.getHighScore());
		largeText2.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 37));
		Text smallText = new Text(WIDTH/2 - 160, HEIGHT/2 +100, "Press ENTER to replay");
		smallText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 25));
		smallText.setFill(Color.DARKGREEN);
		Text smallText2 = new Text(WIDTH/2 - 130, HEIGHT/2 + 130 , " or ESCAPE to exit");
		smallText2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 25));
		smallText2.setFill(Color.DARKGREEN);
		g.getChildren().addAll(smallText, largeText2, smallText2, largeText);
		scene.setRoot(g);
		stage.setScene(scene);
	}
	
	/**
	 * Method to get, or rather pass the Snake object
	 * @return The snake object
	 */
	public Snake getSnake() {
		return snake;
	}

	/**
	 * Method to get, or rather pass the Board object
	 * @return Board object
	 */
	public Board getBoard() {
		return board;
	}
}
