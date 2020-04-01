package code;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.paint.PhongMaterial;

/*Buildings is the name I use to refer to the blocks that transform on the planet.
The class contains the behaviours that the blocks undertake in different modes,
and keeps track of their health / height etc.*/

public class Building {

    private boolean special = false;
    Box box;
    private int maxHeight = 120;
    private int minHeight = 4;
    private boolean rising = false;
    private double XROTATION;
    private double YROTATION;
    private boolean frozen = false;
    private double speed = 0.8;
    private double health = 10;

    public Building(double XYoffset, double YZoffset, int WIDTH, int HEIGHT){
        double XYangle = XYoffset *18;
        double YZangle = YZoffset *18;
        XROTATION +=XYangle;
        YROTATION+=YZangle;
        box = new Box(10, 30, 30);
        box.translateXProperty().set(WIDTH/2);
        box.translateYProperty().set(HEIGHT/2);
        //transformations
        Rotate XYrotation = new Rotate(XYangle, Rotate.Z_AXIS);
        XYrotation.pivotXProperty().set(0);
        Translate translation = new Translate(100, 0 ,0);
        Rotate YZrotation = new Rotate(YZangle, Rotate.Y_AXIS);
        YZrotation.pivotYProperty().set(0);
        box.getTransforms().addAll(YZrotation, XYrotation, translation);
        //behaviour

    }

    public Box box(){ return box;  }

    public void alter(){
        if(special == true){
            box.setMaterial(new PhongMaterial(Color.CORAL));
            box.setDrawMode(DrawMode.LINE);
            box.setWidth(10);
            return;
        }
        if(box.getWidth()<maxHeight && rising == true){
            if(box.getWidth()+speed>maxHeight) box.setWidth(maxHeight);
            else box.setWidth(box.getWidth()+speed);
        }
        if(box.getWidth()>minHeight && rising == false) {
            if (box.getWidth() - speed < minHeight) box.setWidth(minHeight);
            else box.setWidth(box.getWidth() - speed);
        }
    }

    public void setDrawMode(DrawMode drawmode){
        box.setDrawMode(drawmode);
    }

    public void reset(){
        health = 10;
        box.setWidth(4);
        setRise(false);
        box.setDrawMode(DrawMode.FILL);
        frozen = false;
        special = false;
        box.setMaterial(new PhongMaterial(Color.SILVER));
    }

    //returns score if building destroyed
    public int damage(Difficulty difficulty){
        health--;
        if(health < 1 && special == false){
            special=true;
            return difficulty.score();
        }
        return 0;
    }

    public boolean special(){ return special;}

    public void setRise(boolean set) { rising = set;}

    public double width(){return box().getWidth();}

    public void setSpeed(double speed) { this.speed = speed;}


    public void setFreeze(boolean set, int speed) {
        frozen = set;
        setSpeed(speed);
    }

    public boolean frozen(){ return frozen;}

    public double YROTATION(double planetSpin){
       double angle = (YROTATION + planetSpin) % 360 ;
       return angle;
    }

    public double XROTATION(double planetSpin){
        double angle = (XROTATION + planetSpin) % 360;
        return angle;
    }

    public void sweeperBehaviour(double rotation){
        double bcolour = 0.4 + colourconvert(rotation) / 360;
        double gcolour = (120 - box().getWidth()) / 120;
        double rcolour = (10-health)/10;
        box().setMaterial(new PhongMaterial(new Color(rcolour, gcolour, bcolour, 1)));
        if(frozen==false) {
            if (rotation > 250) setSpeed(1.5);
            if (rotation > 240) setRise(true);
            if (rotation < 50) setRise(false);
        }
        if (frozen() == true && rotation > 160 && rotation < 170) {
            setRise(false);
            setFreeze(false, 8);
        }
    }

    public void bacteriaBehaviour(int random){
        double width = box.getWidth();
        double rcolour = 0, gcolour = 0;
        rcolour = 0.5+((120-width)/120/2);
        gcolour = (width)/120;
        box().setMaterial(new PhongMaterial(new Color(rcolour, gcolour, 0.25, 1)));
        if(random==0 && special==true) reset();
    }

    public void waveBehaviour(){
        double bcolour = ((120-width())/120)/2+0.5;
        double opacity = ((120-width())/120)/4+0.75;
        double rcolour = ((120-width())/120)/3;
        double gcolour = ((120-width())/120)/2+0.1;
        if(width()>115){
            setRise(false);
        }
        if(health>0) box().setMaterial(new PhongMaterial(new Color(rcolour, gcolour, bcolour, opacity)));
        else box().setMaterial(new PhongMaterial(new Color(0.8,0.3,0.7,1)));
    }

    public void slamdiscoBehaviour(){
        double bcolour = 0.1*(randomInt(1,5))+ (width()/120)/2;
        double gcolour = 0.1*(randomInt(1,5)) + (width()/120)/2;
        double rcolour = 0.1*(randomInt(1,5)) + (width()/120)/2;
        double opacity = (health/10)*0.5 + 0.5;
        if(randomInt(0,10)==0) box().setMaterial(new PhongMaterial(new Color(rcolour, gcolour, bcolour, opacity)));
    }

    //converts a rotation to colour value
    public double colourconvert(double rotation){
        if(rotation <= 180){ return 180-rotation; }
        return rotation-180;
    }

    public static int randomInt(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return (int)x;
    }

}
