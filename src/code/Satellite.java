package code;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

/*The satellite is at the moment just a tiny moon that orbits the planet sphere
and doesn't do anything else.  */
public class Satellite {
    Sphere sphere = new Sphere(5);

    public Satellite(int WIDTH, int HEIGHT, int radius){
        new AnimationTimer() {
            double i, j;
            Translate translation = new Translate();
            public void handle(long now) {
                i+=0.005;
                j+=0.0051;
                translation.setX((radius)*Math.cos(i)+WIDTH/2);
                translation.setY((radius)*Math.sin(j)+HEIGHT/2);
                sphere.getTransforms().clear();
                sphere.getTransforms().add(translation);
            }
        }.start();
    }

    public Sphere get(){ return sphere;}
}
