import java.awt.*;

public class RectangleProjectile extends Projectile
{
    public RectangleProjectile(InitialPoint initial, double depth, double width, double height, double mass)
    {
        super(initial, depth, width, height, depth*width*height, mass, 1.05);
    }

    public void drawObject(int fWidth, int fHeight, int xOffset, Graphics2D g) 
    {
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));

        int cx = (fWidth/2)+(int)x-xOffset;
        int cy = fHeight-(int)y-40;

        g.drawRect(cx-(int)(width/2), cy-(int)(height/2), (int)width, (int)height);
        
        g.setColor(Color.RED);

        g.drawOval((fWidth/2)+(int)x-xOffset-3, fHeight-(int)y-3-40, 6, 6);
    }

    public double getProjected2DX() 
    {
        return depth*height;
    }

    public double getProjected2DY() 
    {
        return depth*width;
    }
}
