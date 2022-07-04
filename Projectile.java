import java.awt.*;

public abstract class Projectile 
{
    double x; //(cartesian, NOT graphical coordinates) | also the center of the objects/shape
    double y;

    int width;
    int height;

    InitialPoint initial; //stores inital velocities 

    double time;

    public Projectile(double x, double y, int width, int height, InitialPoint iPoint)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.initial = iPoint;
        this.time = 0;
    }

    abstract public void drawObject(int fWidth, int fHeight, int xOffset, Graphics2D g);
}
