import java.awt.*;

public class Vector 
{
    double velocity; //pixels per second
    double angle;
    double radians;

    public Vector(double velocity, double angle)
    {
        this.velocity = velocity;
        this.angle = angle;
        this.radians = angle*(Math.PI/180);
    }

    public void drawVector(int x, int y, int fWidth, int fHeight, int xOffset, Graphics2D g)
    {
        g.setColor(angle%90 == 0 ? (angle == 90 || angle == 270) ? Color.RED : Color.BLUE : Color.YELLOW);
        g.setStroke(new BasicStroke(2));

        g.drawLine((fWidth/2)+(int)x-xOffset, fHeight-(int)y-40, 
       (fWidth/2+(int)x)+(int)(velocity*Math.cos(radians))-xOffset, (fHeight-(int)y)-(int)(velocity*Math.sin(radians))-40);
    }

    @Override
    public String toString()
    {
        return "velocity=" + velocity + "  angle=" + angle;
    }
}
