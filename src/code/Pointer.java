package code;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/*The pointer is a 3D object following the mouse that is a group containing
rotating cyclinders and circles that sit on the surface of the planet sphere
to indicate the 'location' of the cursor (which is otherwise invisible). */
public class Pointer extends Object3D {

    //Objects associated with player
    private Group group = new Group();
    private Sphere pointer = new Sphere(12, 8);
    private Group pointGroup = new Group();

    public Pointer(int WIDTH, int HEIGHT, int moveRadius, int mouseRadius){
        super(WIDTH, HEIGHT, moveRadius, mouseRadius);
        pointer.setDrawMode(DrawMode.LINE);
        createPointers();
        group.getChildren().addAll(pointer, pointGroup);
        Rotate f = new Rotate(90, Rotate.X_AXIS);
        new AnimationTimer() {
            public void handle(long now) {
                spinWithRotation(pointGroup,1,Rotate.Z_AXIS);
                spinWithRotation(pointer, -3, Rotate.Z_AXIS);
                spinWithRotation(pointer, -2, Rotate.Y_AXIS);
            }
        }.start();
    }

    public Group group() { return group; }

    private void createPointers(){
        Cylinder point1 = new Cylinder(15, 9, 6);
        Cylinder point2 = new Cylinder(15, 9, 20);
        point1.setDrawMode(DrawMode.LINE);
        point2.setDrawMode(DrawMode.LINE);
        point2.setMaterial(highlight);
        Rotate r = new Rotate(90, Rotate.X_AXIS);
        pointGroup.getChildren().addAll(point2);
        point1.getTransforms().add(r);
        point2.getTransforms().add(r);
    }

    public void onMouseMovement(double sceneX, double sceneY){
        double distance = Math.sqrt(Math.abs(Math.pow(sceneX - WIDTH/2, 2)+Math.pow(sceneY-HEIGHT/2, 2)));
        rotateWithMouse(pointGroup);
        moveWithMouse(sceneX, sceneY, mouseRadius-3, pointGroup);
        if(distance<mouseRadius){
            moveWithMouse(sceneX, sceneY, mouseRadius, pointer);
            pointer.setMaterial(inradius);
        }
        else {
            pointGroup.setTranslateX(-1000);
            pointer.setMaterial(outradius);
            pointer.setTranslateX(sceneX);
            pointer.setTranslateY(sceneY);
        }
    }
}
