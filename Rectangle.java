import java.awt.*;

public class Rectangle extends Projectile
{
    public Rectangle(int x, int y, int width, int height, InitialPoint iPoint)
    {
        super(x, y, width, height, iPoint);
    }

    public void drawObject(int fWidth, int fHeight, int xOffset, Graphics2D g) 
    {
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));

        int cx = (fWidth/2)+(int)x-xOffset;
        int cy = fHeight-(int)y;

        g.drawRect(cx-(width/2), cy-(height/2), width, height);
        
        g.setColor(Color.RED);

        g.drawOval((fWidth/2)+(int)x-xOffset-3, fHeight-(int)y-3, 6, 6);
    }
}