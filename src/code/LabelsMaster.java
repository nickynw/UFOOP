package code;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/*This class keeps track of all the various labels in the game, rather than
having them bulk up the main Game class, any changes can be kept track of here
if i want a label to do something new at some point in the game. This links
heavily with the 'Mode' Enum. Some labels are visible during modes e.g. Start
screen, whereas they are not visible during the game, and vice versa*/
public class LabelsMaster {
    //Labels
    Label timeLabel = new Label(), scoreLabel = new Label(), titleLabel = new Label("U.F.O.O.P");
    Label subtitleLabel = new Label("Unidentified. Flying. Object. Orientated. Programming.");
    Label modeLabel = new Label(), difficultyLabel = new Label(), startLabel = new Label("Click sphere to begin.");
    Label adviceLabel = new Label("Goal: Avoid obstacles by moving the ship with your mouse,\n" +
            "click to fire your beam, destroy the planet to earn points!");
    Arc healthDisplay = new Arc(335,100,70,70,0,360);
    private Label speechLabel = new Label("Hello");
    Label highscoreLabel = new Label();
    Group gameLabels = new Group(), startLabels = new Group();
    public Font smallFont, mediumFont, largeFont, hugeFont;
    private double startz = gameLabels.getTranslateZ();

    public LabelsMaster(){
        initialiseFonts();
        initialiseLabels();
    }

    public Group gameLabels(){return gameLabels;}

    public Group startLabels(){return startLabels;}

    public void moveSpeechLabel(double x, double y, double z) {
        speechLabel.setTranslateX(x - 10);
        speechLabel.setTranslateY(y - 20);
        speechLabel.setTranslateZ(z - 10);
    }

    private void initialiseFonts() {
        try {
            smallFont = Font.loadFont(new FileInputStream(new File("src/fonts/Fleftex_M.ttf")), 13);
            mediumFont = Font.loadFont(new FileInputStream(new File("src/fonts/Fleftex_M.ttf")), 24);
            largeFont = Font.loadFont(new FileInputStream(new File("src/fonts/Fleftex_M.ttf")), 40);
            hugeFont = Font.loadFont(new FileInputStream(new File("src/fonts/Fleftex_M.ttf")), 60);
        } catch (FileNotFoundException e) {
            smallFont = Font.font("Verdana", 13);
            mediumFont = Font.font("Verdana", 24);
            largeFont = Font.font("Verdana", 40);
            hugeFont = Font.font("Verdana", 60);
        }
    }

    private void initialiseLabels() {
        speechLabel.setFont(Font.font("Verdana", 8));
        speechLabel.setTextFill(Color.SILVER);
        initialiseLabel(timeLabel, 315,75, largeFont, Color.ANTIQUEWHITE);
        initialiseLabel(scoreLabel, 245, 500, mediumFont, Color.ANTIQUEWHITE);
        initialiseLabel(modeLabel, 20, 280, mediumFont, Color.SILVER);
        initialiseLabel(difficultyLabel, 530, 280, mediumFont, Color.SILVER);
        initialiseLabel(titleLabel,120,50,hugeFont, Color.SILVER);
        initialiseLabel(startLabel,125,435,mediumFont, Color.SILVER);
        initialiseLabel(subtitleLabel, 50, 130, smallFont, Color.ANTIQUEWHITE);
        initialiseLabel(highscoreLabel, 205, 542, mediumFont, Color.SLATEGRAY);
        initialiseLabel(adviceLabel, 25, 585, smallFont, Color.SLATEGRAY);
        healthDisplay.setFill(Color.CORNFLOWERBLUE);
        healthDisplay.setType(ArcType.ROUND);
        healthDisplay.setVisible(false);
        gameLabels.getChildren().addAll(healthDisplay, timeLabel, scoreLabel,
                modeLabel, difficultyLabel, highscoreLabel, speechLabel);
        startLabels.getChildren().addAll( titleLabel, startLabel, subtitleLabel, adviceLabel);
    }

    public void startVisibility(){
        scoreLabel.setVisible(false);
        startLabels.setVisible(true);
        healthDisplay.setVisible(false);
        timeLabel.setTranslateY(75);
    }

    public void gameVisibility(){
        scoreLabel.setVisible(true);
        startLabels.setVisible(false);
        healthDisplay.setVisible(true);
        timeLabel.setTranslateY(75);
        resetZ(timeLabel, difficultyLabel, modeLabel, titleLabel, startLabel);
    }

    public void gameOverVisibility(){
        changeLabelColour(false);
        translate(timeLabel, 187, 130, -100);
    }

    public void updateLabels(Mode mode, int seconds, int health, Difficulty difficulty, int highscore, int score){
        //set Timelabel position
        if(mode==Mode.RESET) timeLabel.setTranslateX(265);
        else if(mode==Mode.GAMEOVER) timeLabel.setTranslateX(187);
        else if (seconds < 10) timeLabel.setTranslateX(324);
        else if (mode==Mode.FINISH) timeLabel.setTranslateX(130);
        else timeLabel.setTranslateX(308);
        //hide/show score
        boolean newhighscore = false;
        if(score >= highscore && mode==Mode.GAMEOVER) newhighscore = true;
        //update labels
        difficultyLabel.setText(mode.difficultyString(difficulty, newhighscore));
        modeLabel.setText(mode.modeString(newhighscore));
        healthDisplay.setLength(health);
        timeLabel.setText(mode.timeString(seconds));
        highscoreLabel.setText("Highscore: " + highscore);
        scoreLabel.setText("Score: "+score);
    }

    public void bulgeLabels(int time, int limit, int amount){
        if(time % limit < limit/2) bulge(-amount, timeLabel, difficultyLabel, modeLabel, titleLabel, startLabel);
        else bulge(+amount, timeLabel, difficultyLabel, modeLabel, titleLabel, startLabel);
    }

    public void changeLabelColour(boolean hit){
        if(hit){
            timeLabel.setTextFill(Color.RED);
            scoreLabel.setTextFill(Color.RED);
            healthDisplay.setFill(Color.DARKRED);
            modeLabel.setTextFill(Color.RED);
            difficultyLabel.setTextFill(Color.RED);
        } else {
            timeLabel.setTextFill(Color.ANTIQUEWHITE);
            scoreLabel.setTextFill(Color.ANTIQUEWHITE);
            healthDisplay.setFill(Color.CORNFLOWERBLUE);
            modeLabel.setTextFill(Color.SILVER);
            difficultyLabel.setTextFill(Color.SILVER);
        }
    }

    private void initialiseLabel(Label label, int x, int y, Font font, Color color){
        translate(label, x, y, 0);
        label.setFont(font);
        label.setTextFill(color);
    }

    private void translate(Node node, double x, double y, double z){
        node.setTranslateX(x);
        node.setTranslateY(y);
        node.setTranslateZ(z);
    }


    private void bulge(int z, Label... labels){
        for(Label label: labels) label.setTranslateZ(label.getTranslateZ()+z);
    }

    private void resetZ(Label... labels){
        for(Label label: labels) label.setTranslateZ(0);
    }

}
