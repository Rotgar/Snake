package Main;

import Controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Mateusz Krawczyk
 * The Main class
 Hello sa
 */
public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
			
			// Controller object, default constructor sets up basic game parameters and the View and Model together
			Controller setUpGame = new Controller();
			// Getting the game stage from controller, which got it from MainView class
			primaryStage = setUpGame.getStage();
			// Show the stage and actual scenes
			primaryStage.show();
	}
	
	public static void main(String args[]){ 	
		  launch(args);
	}
}
