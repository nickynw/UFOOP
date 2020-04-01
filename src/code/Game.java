package code;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends MediaHolder {
    private int beamCount;
    private File directory;

    //Settings
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;
    private static final int moveRadius = 150;
    private static final int mouseRadius = 100;

    //Global variables used in game
    private Mode mode = Mode.STARTSCREEN;
    private Difficulty difficulty = Difficulty.NONE;
    private boolean beaming;
    private int score;
    List<Building> frozenBuildings = new ArrayList<>();
    List<Sphere> particles = new ArrayList<>();
    private long setMills = System.currentTimeMillis();
    private int seconds = 60;
    private int health = 360;
    private int highscore;
    private int time = 0;
    private int keyInt1 = 60, keyInt2 = 70, keyInt3 = 30, direction3 = -1, direction2 = 20, direction1 = 1;

    //Materials
    PhongMaterial red = new PhongMaterial(Color.RED);
    PhongMaterial orange = new PhongMaterial(Color.ORANGE);
    PhongMaterial yellow = new PhongMaterial(Color.YELLOW);

    //gameRoot objects
    Player player = new Player(WIDTH, HEIGHT, moveRadius, mouseRadius);
    Planet planet = new Planet(WIDTH, HEIGHT);
    Pointer pointer = new Pointer(WIDTH, HEIGHT, moveRadius, mouseRadius);
    Satellite satellite = new Satellite(WIDTH, HEIGHT, 150);
    LabelsMaster labelsObject = new LabelsMaster();
    List<Mode> modes = new ArrayList<>();

    //Class scene/stage-related objects and variables
    private Scene scene;
    private Group root = new Group();
    private Stage stage;
    double rootStartx = root.getTranslateX();
    double rootStarty = root.getTranslateY();
    double rootStartz = root.getTranslateZ();

    public Game(Scene scene, Stage primaryStage, File directory, int highscore) {
        super();
        // Initialise game
        mediaPlayer0.play();
        this.stage = primaryStage;
        this.directory = directory;
        this.highscore = highscore;
        this.scene = scene;
        initialiseModes();
        ImageView background = initialiseBackground();
        for(int i = 0; i <60; i ++){ particles.add(new Sphere(1)); }
        root.getChildren().addAll(background, planet.group(), player.group(), satellite.get(), pointer.group());
        root.getChildren().addAll(labelsObject.gameLabels(), labelsObject.startLabels());
        root.getChildren().addAll(particles);
        events();
        initialiseStartScreen();
        scene.setRoot(root);
    }

    private void initialiseModes(){
        if(modes.size()!=0) modes.clear();
        modes.add(Mode.RIPPLE);
        modes.add(Mode.SWEEPER);
        modes.add(Mode.BACTERIA);
        modes.add(Mode.SLAMDISCO);
    }

    private ImageView initialiseBackground() {
        Image back1 = new Image("/images/background.png", true);
        ImageView background = new ImageView(back1);
        translateAbs(background, 0, -0, 200);
        return background;
    }

    private void events() {
        //cyclic events
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(60),
                ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        //mouse events
        scene.setOnMouseMoved(event -> {
            if(mode!=Mode.GAMEOVER) {
                player.onMouseMovement(event.getSceneX(), event.getSceneY());
                pointer.onMouseMovement(event.getSceneX(), event.getSceneY());
            }
        });
        scene.setOnMouseDragged(event -> {
            if(mode!=Mode.GAMEOVER) {
                player.onMouseMovement(event.getSceneX(), event.getSceneY());
                pointer.onMouseMovement(event.getSceneX(), event.getSceneY());
            }
        });
        scene.setOnMousePressed(event -> {
            beaming = player.showBeam(event.getSceneX(), event.getSceneY());
        });
        scene.setOnMouseReleased(event -> {
            player.releaseBeam();
            beaming = false;
        });
        //for testing to reset
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent key) {
                if (key.getCode() == KeyCode.R) {
                    modeSwitch(Mode.RESET);
                }
            }
        });
    }

    private void tick(){
        //Global updates
        if(health<360 && health>1) health++;
        time++;
        seconds = 60-secondsPassed();
        labelsObject.updateLabels(mode,seconds, health, difficulty, highscore, score);
        labelsObject.moveSpeechLabel(player.group().getTranslateX(), player.group().getTranslateY(), player.group().getTranslateZ());
        updateBeam();
        //Game changers
        if(seconds < 1 && mode!=Mode.STARTSCREEN) modeSwitch(Mode.RESET);
        if(health < 1 ) modeSwitch(Mode.GAMEOVER);
        //Game updates
        if(mode == Mode.SWEEPER){ sweeper(); }
        if(mode == Mode.RIPPLE){ ripple(); }
        if(mode == Mode.SLAMDISCO){ slamdisco(); }
        if(mode == Mode.STARTSCREEN){ startScreen(); }
        if(mode == Mode.BACTERIA){ bacteria(); }
        if(mode == Mode.RESET) { reset(); }
        if(mode == Mode.GAMEOVER) { gameOver(); }
        if(mode == Mode.FINISH) { finish(); }
    }

    private void updateBeam() {
        if(beaming) beamCount++;
        if(beamCount>5){
            beamCount=0;
            laserSound.play();
        }
    }

    //initialises all modes, setting time to 0, and getting the planet spin speed based on difficulty/mode
    private void modeSwitch(Mode newMode){
        if(mode != newMode){
            setMills = System.currentTimeMillis();
            mode = newMode;
            time = 0;
            seconds = 60;
            planet.setSpinSpeed(mode.spinSpeed(difficulty));
            labelsObject.gameVisibility();
            if(mode == Mode.STARTSCREEN) initialiseStartScreen();
            if(mode == Mode.GAMEOVER) initialiseGameOver();
            if(mode == Mode.FINISH) initialiseFinish();
        }
    }

    /******************************** START SCREEN *****************/

    private void initialiseStartScreen() {
        initialiseModes();
        mediaPlayer0.play();
        score = 0;
        health = 360;
        labelsObject.startVisibility();
        player.group().setVisible(true);
        pointer.group().setVisible(true);
        difficulty = difficulty.NONE;
        for (Building building : planet.buildings()) building.box().setDrawMode(DrawMode.LINE);
    }

    private void startScreen(){
        labelsObject.bulgeLabels(time, 40, 2);
        if(beaming==true){
            mediaPlayer0.stop();
            modeSwitch(Mode.RESET);
        }
    }

    /***************************RESET*******************************/

    private void reset(){
        labelsObject.bulgeLabels(time, 20, 1);
        if(time==0){
            stopMusic();
            if(difficulty==difficulty.NONE){ mediaPlayer1.play(); }
            if(difficulty==difficulty.EASY){ mediaPlayer2.play(); }
            if(difficulty==difficulty.MEDIUM){ mediaPlayer3.play(); }
            if(difficulty==difficulty.HARD){ mediaPlayer4.play(); }
        }
        for(Building building: planet.buildings()){
            building.alter();
            building.setRise(false);
        }
        if (time < planet.buildings().size() - 1) {
            planet.buildings.get(time).reset();
            planet.buildings.get(planet.buildings().size()-1-time).reset();
        }
        if (time == (planet.buildings().size()-1)/2){
            if(difficulty==difficulty.NONE) difficulty = difficulty.EASY;
            else if(difficulty==difficulty.EASY) difficulty = difficulty.MEDIUM;
            else if(difficulty==difficulty.MEDIUM) difficulty = difficulty.HARD;
            else if(difficulty==difficulty.HARD) difficulty = difficulty.VERYHARD;
            else if(difficulty==difficulty.VERYHARD){
                modeSwitch(mode.FINISH);
                return;
            }
            modeSwitch(randomElement(modes));
        }
    }

    /********************** GAME OVER **************************/

    private void initialiseGameOver() {
        health = 0;
        stopMusic();
        labelsObject.gameOverVisibility();
        player.group().setVisible(false);
        pointer.group().setVisible(false);
        defeatSound.play();
        writeHighscore();
        for (Building building : planet.buildings()) {
            building.setRise(false);
        }
    }

    private void gameOver(){
        labelsObject.bulgeLabels(time, 40, 3);
        for(Building building: planet.buildings()){ building.alter();}
        createParticle();
        if (time < planet.buildings().size() - 1) {
            planet.buildings.get(time).reset();
            planet.buildings.get(planet.buildings().size()-1-time).reset();
            planet.buildings.get(time).setDrawMode(DrawMode.LINE);
            planet.buildings.get(planet.buildings().size()-1-time).setDrawMode(DrawMode.LINE);
        }
        if(time > 100){
            playerSafe();
            modeSwitch(mode.STARTSCREEN);
        }
    }

    private void initialiseFinish() {
        stopMusic();
        victorySound.play();
        labelsObject.gameOverVisibility();
        writeHighscore();
        for (Building building : planet.buildings()) {
            building.setRise(false);
        }
    }

    private void finish(){
        labelsObject.bulgeLabels(time, 40, 3);
        for(Building building: planet.buildings()){ building.alter();}
        if (time < planet.buildings().size() - 1) {
            planet.buildings.get(time).reset();
            planet.buildings.get(planet.buildings().size()-1-time).reset();
            planet.buildings.get(time).setDrawMode(DrawMode.LINE);
            planet.buildings.get(planet.buildings().size()-1-time).setDrawMode(DrawMode.LINE);
        }
        if(time > 100){
            playerSafe();
            modeSwitch(mode.STARTSCREEN);
        }
    }


    private void stopMusic(){
        mediaPlayer1.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();
        mediaPlayer4.stop();
    }

    private void writeHighscore() {
        if (score > highscore || score == highscore) {
            highscore = score;
            try {
                String file_name = directory + "\\" + "Highscore.txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(file_name));
                writer.write(highscore+"");
                writer.close();
            } catch (IOException E) { }
        }
    }

    /************GAME MODES***************/

    /*NOTE: 0 is the RIGHT side  angle 180 <-    (   )  -> angle 0
                                 angle 90 is in front, angle 270 is behind. */


    private void ripple() {
        boolean collision = false;
        for (Building building : planet.buildings()) {
            if(collision(building)) collision = true;
            building.alter();
            building.setSpeed(5);
            building.waveBehaviour();
        }
        //raise key planets plus their neighours
        planet.buildings().get(keyInt1).setRise(true);
        planet.buildings().get(keyInt2).setRise(true);
        if( planet.buildings().get(keyInt3).special()) planet.buildings().get(keyInt3).reset();
        planet.buildings().get(keyInt3).setRise(true);
        if(keyInt1 >21) planet.buildings().get(keyInt1 -20).setRise(true);
        if(keyInt1 <137) planet.buildings().get(keyInt1 +20).setRise(true);
        if(keyInt2 >1) planet.buildings().get(keyInt2-1).setRise(true);
        if(keyInt2 <156) planet.buildings().get(keyInt2+1).setRise(true);
        //initiate movement
        if(time % mode.timeSpeed(difficulty) == 0) {
            keyInt3 += direction3;
            keyInt2 += direction2;
            keyInt1 += direction1;
        }
        if(time % 100 == 0) keyInt1 = randomInt(30, 130);
        if(time % 85 == 0)  keyInt2 = randomInt(30, 130);
        if(time % 70 == 0)  keyInt3 = randomInt(30, 130);
        //change direction(1)
        if(keyInt1 >150) direction1 = -1;
        if(keyInt1 <2) direction1 = +1;
        //change direction(2)
        if(keyInt2 > 155){
            keyInt2 = 158-randomInt(1,9);
            direction2 = -20;
        }
        if(keyInt2 < 2){
            keyInt2 = randomInt(1,9);
            direction2 = +20;
        }
        //change direction(3)
        if(keyInt3 >150) direction3 = -1;
        if(keyInt3 <3) direction3 = +1;
        if(collision) playerHurt();
        else playerSafe();
    }

    private void bacteria() {
        boolean collision = false;
        //random chance of buildings rising or falling (2/5 chance)
        if (time >= mode.timeSpeed(difficulty)) {
            time = 0;
            for (Building building : planet.buildings()) {
                int r = randomInt(0, 4);
                if(r == 0 || r == 1){
                    building.setRise(true);
                } else building.setRise(false);
            }
        }
        for (Building building : planet.buildings()) {
            if(collision(building)) collision = true;
            building.alter();
            building.bacteriaBehaviour(randomInt(0, 60));
        }
        if(collision) playerHurt();
        else playerSafe();
    }

    private void sweeper(){
        boolean collision = false;
        int random[] = {0, 0};
        int frozenCount = 0;
        //1) make buildings shimmer, and choose ones to 'freeze' high
        for(Building building: planet.buildings()){
            if(collision(building)) collision = true;
            double rotation = building.YROTATION(planet.YROTATION());
            building.alter();
            building.sweeperBehaviour(rotation);
            if (time >= mode.timeSpeed(difficulty) && rotation > 261 && rotation < 279 && building.special() == false) {
                frozenBuildings.add(building);
            }
        }
        //2) freeze the buildings that were chosen except for 2 of them
        random[0] = randomInt(0, frozenBuildings.size() - 1);
        random[1] = randomInt(0, frozenBuildings.size()-1);
        for (int i = 0; i < frozenBuildings.size(); i++) {
            if (i != random[0] && i!= random[1]){
                if(frozenCount < 8) frozenBuildings.get(i).setFreeze(true, 8);
                frozenCount++;
            }
        }
        if(frozenCount>=7){
            frozenBuildings.get(3).setFreeze(false, 8);
            frozenBuildings.get(3).reset();
        }
        frozenBuildings.removeAll(frozenBuildings);
        //update collision and timer
        if(collision) playerHurt();
        else playerSafe();
        if (time >= mode.timeSpeed(difficulty)) time = 0;
    }

    private void slamdisco(){
        boolean collision = false;
        //Choose a bunch of buildings to randomly raise every so often
        if(time==20){
            int random[] = { randomInt(20, 130), randomInt(20, 130),
                    randomInt(20, 130), randomInt(20, 130) , randomInt(20, 130), randomInt(20, 130),
                    randomInt(20, 130), randomInt(20, 130), randomInt(20, 130), randomInt(20, 130)};
            for (int i = 0; i < planet.buildings().size(); i++){
                planet.buildings().get(i).setRise(false);
                for(int randomInt: random) if(i==randomInt) planet.buildings().get(i).setRise(true);
            }
        }
        if(time > 80) time = 0;
        for (Building building : planet.buildings()){
            if(collision(building)) collision = true;
            building.alter();
            building.setSpeed(5);
            if(time%5==0) building.slamdiscoBehaviour();
        }
        if(collision) playerHurt();
        else playerSafe();
    }

    /****************supporting functions*********************/

    public static int randomInt(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return (int)x;
    }


    public int secondsPassed() {
        long nowMillis = System.currentTimeMillis();
        return (int)((nowMillis - this.setMills) / 1000);
    }

    public Mode randomElement(List<Mode> list)
    {
        Mode mode = null;
        int randomIndex = randomInt(0, list.size()-1);
        while(mode==null){
            try{
                mode = modes.get(randomIndex);
                list.remove(randomIndex);
            } catch(Exception e) { }
        }
        return mode;
    }


    /************************COLLISIONS**************************/

    //returns true if building rotation is same as player, plus its height is extended
    private boolean collision(Building building){
        int buildingYRotation = (int)building.YROTATION(planet.YROTATION());
        int buildingXRotation = (int)building.XROTATION(planet.XROTATION())+180;
        int playerYRotation = (int)(-(player.YROTATION-90))%360;
        int playerXRotation = (int)player.XROTATION();
        //is player within the correct distance?
        if(Math.abs(buildingYRotation-playerYRotation)<13
                && Math.abs(buildingXRotation-playerXRotation)<13){
            //is the building high enough to hit?
            if(building.width()>100) return true;
            //or is the player shooting a low building?
            if(beaming == true && building.width()<100 && building.special()==false){
                score+=building.damage(difficulty);
                return false;
            }
        }
        return false;
    }

    //shakes the screen, shows damage particles, and makes lights flash red
    private void playerHurt(){
        if(score>0 && mode!=Mode.GAMEOVER) score-=1;
        health-=6;
        for(Box light: player.lights()) light.setMaterial(red);
        createParticle();
        labelsObject.changeLabelColour(true);
        if(!collisionSound.isPlaying()) collisionSound.play();
        //screenshake
        int movement[] = new int[]{randomInt(-3,3), randomInt(-3,3), randomInt(-3,3)};
        translateRel(root, movement[0], movement[1], movement[2]);
    }

    //resets screen after shaking, makes particles invisible, makes lights flash yellow again
    private void playerSafe(){
        translateAbs(root, rootStartx, rootStarty, rootStartz);
        for(Box light: player.lights()) light.setMaterial(yellow);
        for(Sphere particle: particles) particle.setVisible(false);
        labelsObject.changeLabelColour(false);
    }

    private void createParticle(){
        for(Sphere particle: particles){
            particle.setVisible(true);
            translateAbs(particle, player.group().getTranslateX(), player.group().getTranslateY(), player.group().getTranslateZ());
            int colour = randomInt(0, 2);
            if(colour==0) particle.setMaterial(red);
            if(colour==1) particle.setMaterial(orange);
            if(colour==2) particle.setMaterial(yellow);
            translateRel(particle, randomInt(-15,15), randomInt(-15,15), randomInt(-15,15));
        }
    }

    /*****************TRANSLATIONS*******************************/

    private void translateAbs(Node node, double x, double y, double z){
        node.setTranslateX(x);
        node.setTranslateY(y);
        node.setTranslateZ(z);
    }

    private void translateRel(Node node, double x, double y, double z){
        node.setTranslateX(node.getTranslateX()+x);
        node.setTranslateY(node.getTranslateY()+y);
        node.setTranslateZ(node.getTranslateZ()+z);
    }
}

