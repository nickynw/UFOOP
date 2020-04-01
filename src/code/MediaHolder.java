package code;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.nio.file.Paths;

//a simple class for keeping track of music and sound effects
public class MediaHolder {
    //Sounds
    AudioClip collisionSound = new AudioClip(Paths.get("src/sound_effects/collision.wav").toUri().toString());
    AudioClip defeatSound = new AudioClip(Paths.get("src/sound_effects/defeat.wav").toUri().toString());
    AudioClip victorySound = new AudioClip(Paths.get("src/sound_effects/victory.mp3").toUri().toString());
    AudioClip laserSound = new AudioClip(Paths.get("src/sound_effects/laser2.wav").toUri().toString());
    String musicFile0 = "src/music/Komiku_-_62_-_The_Challenge.mp3";
    javafx.scene.media.Media musicMedia0 = new javafx.scene.media.Media(new File(musicFile0).toURI().toString());
    MediaPlayer mediaPlayer0 = new MediaPlayer(musicMedia0);
    String musicFile1 = "src/music/Komiku_-_05_-_Beach.mp3";
    javafx.scene.media.Media musicMedia1 = new javafx.scene.media.Media(new File(musicFile1).toURI().toString());
    MediaPlayer mediaPlayer1 = new MediaPlayer(musicMedia1);
    String musicFile2 = "src/music/Monplaisir_-_02_-_Garage.mp3";
    javafx.scene.media.Media musicMedia2 = new javafx.scene.media.Media(new File(musicFile2).toURI().toString());
    MediaPlayer mediaPlayer2 = new MediaPlayer(musicMedia2);
    String musicFile3 = "src/music/Monplaisir_-_05_-_Level_2.mp3";
    javafx.scene.media.Media musicMedia3 = new javafx.scene.media.Media(new File(musicFile3).toURI().toString());
    MediaPlayer mediaPlayer3 = new MediaPlayer(musicMedia3);
    String musicFile4 = "src/music/Monplaisir_-_07_-_Level_4.mp3";
    javafx.scene.media.Media musicMedia4 = new javafx.scene.media.Media(new File(musicFile4).toURI().toString());
    MediaPlayer mediaPlayer4 = new MediaPlayer(musicMedia4);

}
