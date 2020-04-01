package code;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.Group;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.paint.PhongMaterial;

/* The planet class holds the key component of the game, the planet
itself, which has a list of buildings that can be accessed that
are generated in a grid format. The planet can also spin, and the
spinning motion speed and axes can be set with public access.
 */

public class Planet {
    private int WIDTH, HEIGHT;
    private final static int planetSize = 100;
    private double SPINSPEED = 0.3;
    private double XAXIS = 0, YAXIS = 0.5, ZAXIS = 0;

    Group group = new Group();
    Sphere sphere = new Sphere(planetSize);
    Rotate r;
    Transform t = new Rotate();
    List<Building> buildings = new ArrayList<>();
    private double YROTATION;
    private double XROTATION;

    public Planet(int WIDTH, int HEIGHT){
        sphere.setDrawMode(DrawMode.LINE);
        sphere.setMaterial(new PhongMaterial(Color.DARKCYAN));
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initialiseBuildings();
        sphere.translateXProperty().set(WIDTH/2);
        sphere.translateYProperty().set(HEIGHT/2);
        group.getChildren().addAll(sphere);
        for(Building building: buildings) group.getChildren().add(building.box());
        new AnimationTimer() {
            public void handle(long now) {
                spin(SPINSPEED);
            }
        }.start();
    }

    /*****************getters*************************/

    public Group group() { return group;}

    public List<Building> buildings() { return buildings ;}

    public double YROTATION(){ return YROTATION;}

    public double XROTATION(){ return XROTATION;}

    /*****************setters and functions**********/

    public void setSpinSpeed(double SPINSPEED){ this.SPINSPEED = SPINSPEED; }

    //this is in the future if I wanted to spin the planet on more than one axis
    public void setSpinAxis(double x, double y, double z){
        XAXIS = x;
        YAXIS = y;
        ZAXIS = z;
    }

    private void initialiseBuildings(){
        buildings.add(new Building(-5,0, WIDTH, HEIGHT));
        for(int i = 0; i < 8; i++) buildings.add(new Building(-4, i*2.4, WIDTH, HEIGHT));
        //majority of middle rings (20 around)
        for(int j = -3; j < 4; j++){
            for(int i = 0; i < 20; i++) buildings.add(new Building(j, i, WIDTH, HEIGHT));
        }
        for(int i = 0; i < 8; i++) buildings.add(new Building(4, i*2.4, WIDTH, HEIGHT));
        buildings.add(new Building(5,0, WIDTH, HEIGHT));
    }


    public void spin(double angle) {
        r = new Rotate(angle, WIDTH/2, HEIGHT/2, 0);
        r.setAxis(new Point3D( XAXIS,YAXIS,ZAXIS));
        YROTATION+=SPINSPEED;
        if(YROTATION > 360){
            YROTATION -= 360;
        }
        if(XROTATION > 360){
            XROTATION -=360;
        }
        this.t = t.createConcatenation(r);
        group.getTransforms().clear();
        group.getTransforms().addAll(t);
    }

}