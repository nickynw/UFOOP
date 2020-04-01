package code;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

/*The player contains the cockpit of the UFO, the circular body (baseplate)
plus rotating rectangles that form the yellow/red lights on the side + the beam
which is usually invisible unless the mouse is held down, all as a group. */
public class Player extends Object3D {

    //Objects associated with player
    private Group group = new Group();
    private Cylinder body= new Cylinder(20,5,12);
    private Group plates = new Group();
    private List<Box> lights = new ArrayList<>();
    private Sphere head = new Sphere(10);
    private Group beam = new Group();

    public Player(int WIDTH, int HEIGHT, int moveRadius, int mouseRadius){
        super(WIDTH, HEIGHT, moveRadius, mouseRadius);
        //create body
        body.setMaterial(bodymetal);
        createPlates();
        createBeam();
        group.getChildren().addAll(body, plates, head, beam);
        Rotate f = new Rotate(90, Rotate.X_AXIS);
        body.getTransforms().add(f);
        new AnimationTimer() {
            public void handle(long now) {
                spinOnly(plates,1, Rotate.Z_AXIS);
            }
        }.start();
    }

    public Group group() { return group; }

    private void createPlates(){
        Cylinder baseplate = new Cylinder(15, 9, 6);
        Rotate b1 = new Rotate(90, Rotate.X_AXIS);
        baseplate.getTransforms().add(b1);
        baseplate.setMaterial(basemetal);
        Box light1 = newPlate(0);
        Box light2 = newPlate(120);
        Box light3 = newPlate(240);
        plates.getChildren().addAll(baseplate, light1, light2, light3);
        lights.add(light1);
        lights.add(light2);
        lights.add(light3);
    }

    private Box newPlate(double angle){
        Box plate = new Box(40, 10, 3);
        Rotate r = new Rotate(angle, Rotate.Z_AXIS);
        plate.getTransforms().add(r);
        plate.setMaterial(platemetal);
        return plate;
    }

    //to change colour of lights e.g. when player is hurt
    public List<Box> lights(){return lights;}

    public void onMouseMovement(double sceneX, double sceneY){
        moveWithMouse(sceneX, sceneY, moveRadius, group);
        rotateWithMouse(group);

    }

    public void createBeam(){
        List<Cylinder> beams = new ArrayList<>();
        int radius = 4;
        int distance = 0;
        for(int i = 0; i < 9; i++){
            radius+=1.5;
            distance-=6;
            Cylinder circle = new Cylinder(radius, 1, 12);
            Rotate f = new Rotate(90, Rotate.X_AXIS);
            Translate t = new Translate(0, distance, 0);
            circle.getTransforms().addAll(f, t);
            circle.setDrawMode(DrawMode.LINE);
            circle.setMaterial(beamcolour);
            beam.getChildren().add(circle);
            beams.add(circle);
            beam.setVisible(false);
        }
        new AnimationTimer() {
            public void handle(long now) {
                spinWithRotation(beam,4,Rotate.Z_AXIS);
                for(Cylinder beam: beams){
                    beam.setRadius(beam.getRadius()+0.2);
                    if(beam.getRadius()>=12) beam.setRadius(4);
                    else beam.setMaterial(beamcolour);
                }
            }
        }.start();

    }

    public boolean showBeam(double sceneX, double sceneY){
        if(!isInRadius(sceneX, sceneY)){
            beam.setVisible(false);
            return false;
        } else beam.setVisible(true);
        return true;
    }

    public void releaseBeam(){ beam.setVisible(false); }

}
