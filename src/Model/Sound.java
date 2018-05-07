package Model;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Sound {
	
	private Media sound;
	private MediaPlayer audio;

	/**
	 * The default sound constructor. Initializes the media objects
	 * @throws URISyntaxException if bad file path
	 */
	public Sound() throws URISyntaxException {
		
		// getting the music file from Ressources directory
		sound = new Media(getClass().getResource("/Ressources/Music.mp3").toURI().toString());
		// initializing media player with the music
		audio = new MediaPlayer(sound);
	}
	
	/**
	 * Return the object that handles the music
	 * @return The MediaPlayer object audio
	 */
	public MediaPlayer getAudio() {
		return audio;
	}
}
