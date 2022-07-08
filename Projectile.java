import java.awt.*;

public abstract class Projectile 
{
    double x; //(cartesian, NOT graphical coordinates) | also the center of the objects/shape
    double y;

    //int length;
    int width;
    int height;

    double mass;
    //double volume;

    InitialPoint initial; //stores inital velocities 

    double time;

    public Projectile(double x, double y, /*int length,*/ int width, int height, double mass, InitialPoint iPoint)
    {
        this.x = x;
        this.y = y;
        //this.length = length;
        this.width = width;
        this.height = height;
        this.initial = iPoint;
        //this.volume = length*width*height;
        this.mass = mass;
        this.time = 0;
    }

    abstract public void drawObject(int fWidth, int fHeight, int xOffset, Graphics2D g);
}
