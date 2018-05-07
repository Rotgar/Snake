package Model;

import java.util.ArrayList;

import View.MainView;

public class Snake {
	
	/**
	 * Snake's starting size
	 */
	private static final int SIZE = 6;
	private BodyPart head;
	/**
	 * Array that holds the entire body
	 */
	private ArrayList<BodyPart> body;
	/**
	 * Snake's size variable and starting position of head 
	 */
	private int size, headX = MainView.WIDTH/2 + GameObject.SIZE/2, headY = MainView.HEIGHT/2 + GameObject.SIZE/2;

	public Snake() {
		
		body = new ArrayList<>();
		head = new BodyPart(headX, headY);
		size = 0;
		setStart();
	}
	
	/**
	 * Method to set snake at starting position, at start or restart of the game
	 */
	public void setStart() {
		
		// set starting position
		if(size == 0) {
			
			body.add(head);
			++size;
			
			for(int i = 1; i < SIZE; ++i) {
				addBodyPart(headX, headY + (i * GameObject.SIZE));
			}
		}
		//if game restart
		else {
			
			body.clear();
			head.setX(headX);
			head.setY(headY);
			size = 0;
			setStart();
		}
	}	
	
	
	/**
	 * Return snake's actual size
	 * @return size
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Return particular body part
	 * @param i - position of part in body array
	 * @return the body part
	 */
	public BodyPart getBodyPart(int i) {
		return body.get(i);
	}
	
	/**
	 * Returns the head of the snake as BodyPart object
	 * @return snake's head
	 */
	public BodyPart getHead() {
		return this.head;
	}
	
	/**
	 * Adds new body part at given point
	 * @param x - position
	 * @param y - position
	 */
	public void addBodyPart(int x, int y) {
		body.add(new BodyPart(x,y));
		++size;
	}
}