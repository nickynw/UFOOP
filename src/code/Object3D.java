package code;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

/*Object3D is my own abstract class that created to contain methods that are shared
by classes/3D objects that have particular behaviours such as moving with the mouse
or rotating around the surfaces of spheres.*/

public class Object3D {
    public int WIDTH, HEIGHT;
    public int moveRadius;
    public int mouseRadius;
    public int xoffset;
    public double YROTATION, XROTATION;
    public Stage stage;
    Rotate r;
    Transform t = new Rotate();

    //Materials
    PhongMaterial bodymetal = createMaterial(Color.SLATEGRAY, Color.SILVER);
    PhongMaterial basemetal = createMaterial(Color.GAINSBORO, Color.SILVER);
    PhongMaterial platemetal = createMaterial(Color.YELLOW, Color.SILVER);
    PhongMaterial inradius = createMaterial(Color.CYAN, Color.CYAN);
    PhongMaterial outradius = createMaterial(Color.DARKRED, Color.DARKRED);
    PhongMaterial highlight = createMaterial(Color.CORNFLOWERBLUE, Color.CORNFLOWERBLUE);
    PhongMaterial beamcolour = createMaterial(new Color(0,0,1,0.5), new Color(0,0,1,0.5));
    PhongMaterial beamdecolour = createMaterial(new Color(0.3,0.3,0.7,0.1), new Color(0.3,0.3,0.7,0.1));


    public void offset(int x, int y) {
        WIDTH+=x;
        HEIGHT+=y;
    }

    public Object3D(int WIDTH, int HEIGHT, int moveRadius, int mouseRadius){
        this.WIDTH=WIDTH;
        this.HEIGHT=HEIGHT;
        this.moveRadius = moveRadius;
        this.mouseRadius = mouseRadius;
    }

    public boolean isInRadius(double sceneX, double sceneY){
        double distance = Math.sqrt(Math.abs(Math.pow(sceneX - WIDTH/2, 2)+Math.pow(sceneY-HEIGHT/2, 2)));
        if(distance < mouseRadius) return true;
        return false;
    }


    //prevents rotating around the sphere where the player cannot see the object
    private double limitRadiusX(double sceneX, double sceneY){
        double distance = Math.sqrt(Math.abs(Math.pow(sceneX - WIDTH/2, 2)+Math.pow(sceneY-HEIGHT/2, 2)));
        if(distance>=mouseRadius) {
            double angle = Math.atan2(sceneY - HEIGHT/2, sceneX - WIDTH/2);
            return WIDTH/2 + mouseRadius * Math.cos(angle);
        }
        return sceneX;
    }

    //prevents rotating around the sphere where the player cannot see the object
    public double limitRadiusY(double sceneX, double sceneY){
        double distance = Math.sqrt(Math.abs(Math.pow(sceneX - WIDTH/2, 2)+Math.pow(sceneY-HEIGHT/2, 2)));
        if(distance>=mouseRadius) {
            double angle = Math.atan2(sceneY - HEIGHT/2, sceneX - WIDTH/2);
            return HEIGHT/2 + mouseRadius * Math.sin(angle);
        }
        return sceneY;
    }

    public void moveWithMouse(double sceneX, double sceneY, double radius, Node object) {
        double boundx = limitRadiusX(sceneX, sceneY);
        double boundy = limitRadiusY(sceneX, sceneY);
        double newx = -radius*Math.sin(boundx*2*Math.PI/WIDTH);
        double newy = -radius*Math.sin(boundy*2*Math.PI/HEIGHT);
        double newz = -Math.sqrt(Math.abs(Math.pow(radius, 2)-newx*newx-newy*newy));
        object.setTranslateX(newx + WIDTH/2 );
        object.setTranslateY(newy + HEIGHT/2 );
        object.setTranslateZ(newz);
        object.setTranslateX(object.getTranslateX()+xoffset);
    }

    public void rotateWithMouse(Node object){
        double yzangle = Math.atan2(object.translateZProperty().get(), object.translateYProperty().get() - WIDTH/2) * 180 / Math.PI+270;
        double zxangle = Math.atan2(object.translateXProperty().get() - WIDTH/2, object.translateZProperty().get()) * 180 / Math.PI+180;
        Rotate yx = new Rotate(-zxangle, Rotate.Y_AXIS);
        Rotate yz = new Rotate(yzangle, Rotate.X_AXIS);
        YROTATION = -zxangle;
        XROTATION = yzangle;
        object.getTransforms().clear();
        object.getTransforms().addAll(yz, yx);
    }

    //spins whilst translating rather than rotating
    public void spinOnly(Node object, double angle, Point3D axis) {
        r = new Rotate(angle, axis);
        this.t = t.createConcatenation(r);
        object.getTransforms().clear();
        object.getTransforms().addAll(t);
    }

    //spins whilst rotating at the same time, e.g. beams
    public void spinWithRotation(Node object, double angle, Point3D axis) {
        Rotate r = new Rotate(-angle, axis);
        object.getTransforms().add(r);
    }

    PhongMaterial createMaterial(Color diffuseColor, Color specularColor) {
        PhongMaterial material = new PhongMaterial(diffuseColor);
        material.setSpecularColor(specularColor);
        return material;
    }

    public double YROTATION(){
        return YROTATION;
    }

    public double XROTATION(){ return XROTATION; }

    //used for testing
    public void setStage(Stage stage){
        this.stage = stage;
    }
}
