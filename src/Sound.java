import java.io.File;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.application.Application.launch;

public class Sound extends Application{


        public void start (Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        //Initialising path of the media file, replace this with your file path
//        File file = new File();
//        String absolutePath = file.getAbsolutePath();

        //Instantiating Media class
        Media media = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());

        //Instantiating MediaPlayer class
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        //by setting this property to true, the audio will be played
        mediaPlayer.setAutoPlay(true);
        primaryStage.setTitle("Playing Audio");
        primaryStage.show();
    }


    public static void main (String[]args){
        launch(args);
    }

}
