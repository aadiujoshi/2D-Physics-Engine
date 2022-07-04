import java.awt.*;

public class Vector 
{
    double velocity; //pixels per second
    int angle;
    double radians;

    public Vector(double velocity, int angle)
    {
        this.velocity = velocity;
        this.angle = angle;
        this.radians = angle*(Math.PI/180);
    }

    public void drawVector(int x, int y, int fWidth, int fHeight, int xOffset, Graphics2D g)
    {
        g.setColor(angle%90 == 0 ? (angle == 90 || angle == 270) ? Color.RED : Color.BLUE : Color.YELLOW);
        g.setStroke(new BasicStroke(2));

        g.drawLine((fWidth/2)+(int)x-xOffset, fHeight-(int)y, 
       (fWidth/2+(int)x)+(int)(velocity*Math.cos(radians))-xOffset, (fHeight-(int)y)-(int)(velocity*Math.sin(radians)));
    }

    @Override
    public String toString()
    {
        return "velocity=" + velocity + "  angle=" + angle;
    }
}
