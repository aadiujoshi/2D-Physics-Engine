import java.awt.*;

public class ExplosiveProjectile extends Projectile
{
    double projRadius;
    double blastForce;
    double blastRadius;

    public ExplosiveProjectile(InitialPoint initial, double depth, double width, double height, double mass, double projRadius, double blastRadius, double blastForce)
    {
        super(initial, depth, width, height, ((double)4/3)*Math.PI*(Math.pow(projRadius, 3)), mass, 0.47);
        this.blastRadius = blastRadius;
        this.projRadius = projRadius;
    }

    public void drawObject(int fWidth, int fHeight, int xOffset, Graphics2D g) 
    {
        g.setColor(Color.GREEN);
        g.setStroke(new BasicStroke(2));

        int cx = (fWidth/2)+(int)x-xOffset;
        int cy = fHeight-(int)y-40;

        g.drawOval(cx-(int)(width/2), cy-(int)(height/2), (int)width, (int)height);
    }   

    public double getProjected2DX() 
    {
        return Math.PI*projRadius*projRadius;
    }

    public double getProjected2DY() 
    {
        return Math.PI*projRadius*projRadius;
    }
}
