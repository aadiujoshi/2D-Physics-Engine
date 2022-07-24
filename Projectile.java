import java.awt.*;

public abstract class Projectile 
{
    double x; //(cartesian, NOT graphical coordinates) | also the center of the objects/shape
    double y;
    double rotation;

    int depth;
    int width;
    int height;

    double mass;
    double volume;

    InitialPoint initial; //stores inital velocities 

    boolean grounded;

    //only for air resistance
    // double dragCoefficient;
    // double accelerationX;
    // double accelerationY;
   
    double time;

    public Projectile(InitialPoint initial, int depth, int width, int height, double volume, double mass, double dragCoefficient)
    {
        this.x = initial.x;
        this.y = initial.y;
        this.grounded = false;
        // this.accelerationX = 0;
        // this.accelerationY = 0; //determined by the physicshandler
        this.rotation = 0;
        this.depth = depth;
        this.width = width;
        this.height = height;
        this.volume = volume;
        // this.dragCoefficient = dragCoefficient;
        this.initial = initial;
        this.mass = mass;
        this.time = 0;
    }

    abstract public void drawObject(int fWidth, int fHeight, int xOffset, Graphics2D g);
    abstract public double getProjected2DX();
    abstract public double getProjected2DY();
}
