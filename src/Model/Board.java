package Model;

import java.util.ArrayList;
import java.util.Random;

import Controller.Controller;
import View.MainView;
import View.ScoreView;
import javafx.animation.*;
import javafx.util.Duration;

public class Board {

	/**
	 * Number of GameObjects to store in X-axis
	 */
	private static final int BWIDTH = MainView.WIDTH/GameObject.SIZE;
	/**
	 * Number of GameObjects to store in Y-axis
	 */
	private static final int BHEIGHT = MainView.HEIGHT/GameObject.SIZE;
	/**
	 * List of fruits
	 */
	private ArrayList<Fruit> fruits; 
	/**
	 * Super fruit object
	 */
	private SuperFruit sFruit;
	/**
	 * List of obstacles
	 */
	private ArrayList<Obstacle> obstacles; // List of obstacles
	/**
	 * Score value
	 */
	private int score, highscore, fruitsEaten;
	/**
	 * Snake object
	 */
	private Snake snake; 
	/**
	 * Snake's head
	 */
	private BodyPart head; 
	/**
	 * Random number for generating points to place objects on them
	 */
	Random rand; 
	/**
	 * State of the game
	 */
	private GameState state;
	/**
	 *  Object of ScoreView to exchange informations about actual score
	 */
	private ScoreView scoreView; 
	/**
	 * Boolean value that tells if it's time to set a new super fruit on the board
	 */
	private boolean isSuper;
	/**
	 * Boolean value that tells if a new obstacle can be added
	 */
	private boolean addObstacle; 
	/**
	 * Number of possible obstacles on the field
	 */
	private int obstaclesNumber; 
	
	/**
	 * Boolean value set true if snake is in super mode after eating the super fruit
	 */
	private boolean superState;
	
	/**
	 * Timers for super fruit and it's effect
	 */
	private Timeline timeSuper, timeSFruit;
	
	/**
	 * Default constructor of board class to initialize starting variables
	 */
	public Board() { 
		
		scoreView = new ScoreView();
		fruits = new ArrayList<>();
		obstacles = new ArrayList<>();
		score = fruitsEaten = 0;
		snake = new Snake();
		rand = new Random();
		head = snake.getHead();
		state = GameState.Started;
		sFruit = null;
		isSuper = false;
		superState = false;
		addObstacle = true;
		obstaclesNumber = Obstacle.OBSTACLES_START_NUMBER;
		
	}
	
	/**
	 * Method to check if an collision occurred, either of the snake head with it's body or with an obstacle on the board
	 * @return Returns the finished state of game
	 */
	public GameState checkCollision() {
		
		int headX, headY, helpX, helpY;
		
		headX = head.getX();
		headY = head.getY();
		
		// checks if snake hit itself
		for(int i = 1; i < snake.getSize(); ++i) {
			
			helpX = snake.getBodyPart(i).getX();
			helpY = snake.getBodyPart(i).getY();
			
			if(helpX == headX && helpY == headY) {
				highscore = score;
				reset();
				return GameState.Finished;
			}
		}
		
		// checks if snake hit obstacle
		if(!superState) { // check when snake isn't immune - didn't eat super fruit
			
			for(int i = 0; i < obstacles.size(); ++i) {
				
				helpX = obstacles.get(i).getX();
				helpY = obstacles.get(i).getY();
				
				if(helpX == headX && helpY == headY) {
					highscore = score;
					reset();
					return GameState.Finished;
				}
			}
		}
		
		return Controller.getState();
	}
	
	/**
	 * Method called in controller to update the state of obstacles in the game
	 */
	public void updateObstacles() {
		
		if(fruitsEaten % 10 == 0 && fruitsEaten != 0 && !addObstacle) {	// place an obstacle each 10 points
			addObstacle = true; // Possible to add obstacle, also ensures only one is added
			obstaclesNumber++; // Now we can have one obstacle more on board
		}
		while(obstacles.size() < obstaclesNumber && addObstacle) {	//placing possible amount of obstacles
			placeObstacles();
		}
	}
	
	/**
	 * Looks for a point on the board to add new obstacle. Checks it to not collide with the snake body or fruits 
	 */
	private void placeObstacles() {
		
		int obstacleX = 0 , obstacleY = 0, helpX, helpY;	
		int headX = snake.getHead().getX(), headY = snake.getHead().getY();
		boolean collision = true, helpS, helpF;		//helpS if doesn't collide with snake, helpF for fruit

		while(collision) {
			
			helpS = helpF = false;
			obstacleX = (rand.nextInt(BWIDTH)*GameObject.SIZE)+GameObject.SIZE/2; // random point on board
			obstacleY = (rand.nextInt(BHEIGHT)*GameObject.SIZE)+GameObject.SIZE/2;
		
			for(int i = 0; i < snake.getSize(); ++i) {	// to not collide with snake
				
				helpX = snake.getBodyPart(i).getX();
				helpY = snake.getBodyPart(i).getY();
	
				// if collides, start while again and generate new point
				if(helpX == obstacleX && helpY == obstacleY) {
					break;
				}
				
				// leave 4 places in the row in front of head snake, so it doesn't bump up and block the road
				if(obstacleX == headX) { 
					if(Math.abs(obstacleY - headY) < 4 * GameObject.SIZE) {
						break;
					}
				}
				else if(obstacleY == headY) {
					if(Math.abs(obstacleX - headX) < 4 * GameObject.SIZE) {
						break;
					}
				}
				
				// if doesn't collide with any snake part, go over to check fruits
				if(i == snake.getSize() - 1) {
					helpS = true;
				}
			}
					
			if(helpS) {	// to not collide with fruits
				
				// if there are no fruits on the field
				if(fruits.size() == 0) {
					helpF = true;
				}
				else {
					
					for(int i = 0; i < fruits.size(); ++i) {
						
						helpX = fruits.get(i).getX();
						helpY = fruits.get(i).getY();
			
						// back to while to generate new point, and check everything again
						if(helpX == obstacleX && helpY == obstacleY) {
							break;
						}
						
						// doesn't collide with fruits
						if(i == fruits.size() - 1) {
							helpF = true;
						}	
					}
				}
				// if there's a super fruit on board, check to not collide with it
				if(isSuper) {
					if(obstacleX == sFruit.getX() && obstacleY == sFruit.getY()) {
						continue;
					}
				}
				
				// point for obstacle doesn't collide with any snake part or fruit
				if(helpF) {
					collision = false;
				}
			}
		}
		// add new obstacle
		addObstacle(obstacleX, obstacleY);
	}
	
	/**
	 * Add new obstacle to array
	 * @param X coordinate
	 * @param Y coordinate
	 */
	private void addObstacle(int X, int Y) {
		obstacles.add(new Obstacle(X, Y));
	}
	
	/**
	 * Method to check if snake ate a fruit
	 */
	public void checkEaten() {
		
		int headX, headY, foodX, foodY;
		headX = head.getX();
		headY = head.getY();
		// checks if it's the super fruit
		if(isSuper) {
		
			if(sFruit.getX() == headX && sFruit.getY() == headY) {
				removeSuperFruit();
				++fruitsEaten;
				score+=3;
				superState = true;
				timeSFruit.stop();
				if(timeSuper != null) {
					timeSuper.stop();
				}
				timeSuper = new Timeline(new KeyFrame(Duration.millis(SuperFruit.SUPER_STATE_TIME), lambda->superState=false));
				timeSuper.play();
				addObstacle = false; // unlock possibility to add another obstacle
				return;
			}
		}
		// check for a fruit on board 	
		for(int i = 0; i < fruits.size(); ++i){
			
			foodX = fruits.get(i).getX();
			foodY = fruits.get(i).getY();
			
			if(foodX == headX && foodY == headY) {
				
				removeFruit(i);
				addLength(); // adds body part to snake
				++fruitsEaten;
		
				if(superState) 
					score+=2;
				else
					++score;
		
				addObstacle = false;
			}		
		}
	}
	
	/**
	 * Method for updating fruits on board
	 */
	public void updateFruit() {
		
		int foodX = 0, foodY = 0, sFoodX = -1, sFoodY = -1; // foodX, foodY - coordinates for normal fruit, with s for super 
		int []place; // place on board, will hold X and Y
		
		if(fruits.size() <= 0) { // if there's no fruit
			
			if(fruitsEaten % 10 == 0 && fruitsEaten != 0 && !isSuper) { // adds super fruit
				isSuper = true;
				place = placeFruit();
				sFoodX = place[0];
				sFoodY = place[1];
			}
			
			do {
				place = placeFruit();
				foodX = place[0];
				foodY = place[1]; 	
			}while(foodX == sFoodX && foodY == sFoodY);
		
			addFruit(foodX, foodY, sFoodX, sFoodY);
		}
	}
	
	/**
	 * Method to place a fruit on the board
	 * @return Returns point(X,Y) on board
	 */
	private int[] placeFruit() {
		
		int []point = new int[2];
		
		int helpX, helpY, foodX = 0, foodY = 0;
		boolean helpS, helpO;	// for Snake and Obstacles
		boolean collision = true;

		while(collision) {
				
			helpS = helpO = false;
			foodX = (rand.nextInt(BWIDTH)*GameObject.SIZE)+GameObject.SIZE/2;
			foodY = (rand.nextInt(BHEIGHT)*GameObject.SIZE)+GameObject.SIZE/2;
				
			for(int i = 0; i < snake.getSize(); ++i) {
					
				helpX = snake.getBodyPart(i).getX();
				helpY = snake.getBodyPart(i).getY();
	
				if(helpX == foodX && helpY == foodY) {
					break;
				}
						
				if(i == snake.getSize() - 1) {
					helpS = true;
				}
			}
					
			if(helpS) {
				
				if(obstacles.size() == 0) {
					helpO = true;
				}
				else {
					
					for(int i = 0; i < obstacles.size(); ++i) {
					
						helpX = obstacles.get(i).getX();
						helpY = obstacles.get(i).getY();
			
						if(foodX == helpX && foodY == helpY) {
							break;
						}
								
						if(i == obstacles.size() - 1) {
							helpO = true;
						}
					}
				}
				if(helpO) {	
					collision = false;
				}	
			}		
		}
		point[0] = foodX;
		point[1] = foodY;
		return point;	
	}
	
	/**
	 * Method to generate a new fruit in the game(2 if it's time for the super-fruit)
	 * @param foodX X coordinate of normal fruit
	 * @param foodY	Y coordinate of normal fruit
	 * @param sFoodX X coordinate of super fruit(-1 by default)
	 * @param sFoodY Y coordinate of super fruit(-1 by default)
	 */
	public void addFruit(int foodX, int foodY, int sFoodX, int sFoodY) {
		
		if(sFoodX != -1 && sFoodY != -1) {	// check if a super fruits is added
			sFruit = new SuperFruit(sFoodX, sFoodY); // create new super fruit
			timeSFruit = new Timeline(new KeyFrame(Duration.millis(SuperFruit.ON_BOARD_TIME), lambda->removeSuperFruit()));
			timeSFruit.play();
		}
		fruits.add(new Fruit(foodX, foodY)); // add new fruit to fruit array
	}
	
	/**
	 * Method to remove a normal fruit from the board, after being eaten by snake
	 * @param i Position of the fruit in array list
	 */
	public void removeFruit(int i) {
		fruits.remove(i);
	}
	
	/**
	 * Method to remove super fruit(make it a null value) 
	 */
	public void removeSuperFruit() {
		isSuper = false;
		sFruit = null;
	}
	
	/**
	 * Method for calling another method from ScoreView to add a point to the score
	 */
	public void updateScore() {
		scoreView.addScore(score);
	}
	
	/**
	 * Add new part to snake's body after eating a fruit
	 */
	public void addLength() {
		BodyPart b1 = snake.getBodyPart(snake.getSize()-1), b2 = snake.getBodyPart(snake.getSize()-2);
		if(b1.getX() > b2.getX())
			snake.addBodyPart(b1.getX()+GameObject.SIZE, b1.getY());
		else if(b1.getX() < b2.getX())
			snake.addBodyPart(b1.getX()-GameObject.SIZE, b1.getY());
		else if(b1.getY() >= b2.getY())
			snake.addBodyPart(b1.getX(), b1.getY()+GameObject.SIZE);
		else if(b1.getY() >= b2.getY())
			snake.addBodyPart(b1.getX(), b1.getY()-GameObject.SIZE);
	}
	
	/**
	 * Resets basic values of the game after lose
	 */
	private void reset() {
		snake.setStart();
		obstacles.clear();
		fruits.clear();
		score = fruitsEaten = 0;
		addObstacle = true;
		superState = false;
		removeSuperFruit();
		obstaclesNumber = Obstacle.OBSTACLES_START_NUMBER;
	}
	
	/**
	 * Returns fruits
	 * @return Array with fruits
	 */
	public ArrayList<Fruit> getFruits(){
		return fruits;
	}
	
	/**
	 * Returns the super fruit
	 * @return Super fruit object
	 */
	public SuperFruit getSuperFruit() {
		return sFruit;
	}
	
	/**
	 * Returns obstacles
	 * @return Array with obstacles
	 */
	public ArrayList<Obstacle> getObstacles(){
		return obstacles;
	}
	
	/**
	 * Returns the snake
	 * @return Snake object
	 */
	public Snake getSnake() {
		return snake;
	}
	
	/**
	 * Returns the object representing ScoreView in Board class
	 * @return The ScoreView object
	 */
	public ScoreView getScoreView() {
		return scoreView;
	}
	
	/**
	 * Returns the actual score
	 * @return Value of score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Returns the highscore when game finished
	 * @return Value of final score
	 */
	public int getHighScore() {
		return highscore;
	}
	
	/**
	 * Returns the actual state of the game
	 * @return Value of GameState
	 */
	public GameState getState() {
		return state;
	}
	
	/**
	 * Returns true if snake is in super state or false if not
	 * @return Boolean true or false
	 */
	public boolean getSuperState() {
		return superState;
	}
}