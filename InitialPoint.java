import java.util.ArrayList;
import java.awt.Graphics2D;

public class InitialPoint 
{
    int x; //also height (cartesian, NOT graphical coordinates)
    int y;

    Vector horizontalVelocity;
    Vector verticalVelocity;
    Vector velocity;

    ArrayList<Projectile> projectiles;
    
    public InitialPoint(int x, int y, Vector velocity)
    {
        this.x = x;
        this.y = y;
        this.velocity = velocity;

        this.projectiles = new ArrayList<>();

        this.horizontalVelocity = new Vector(Math.cos(velocity.radians)*velocity.velocity, (velocity.angle>90 && velocity.angle<270)?180:0);
        this.verticalVelocity = new Vector(Math.sin(velocity.radians)*velocity.velocity, (velocity.angle>=0 && velocity.angle<=180)?90:270);
    }

    public void updateVectorDirection(int ox, int oy) //point in mouse direction (cartesian points)
    {
        int cx = ox - this.x;
        int cy = oy - this.y;

        double theta = Math.atan2(cy, cx)*(180/Math.PI);
        theta = theta>=0 ? theta : 360+theta;

        this.velocity.angle = (int)Math.round(theta);
        this.velocity.radians = velocity.angle*(Math.PI/180);

        updateXYVectorMagnitudes();
    }

    public void updateXYVectorMagnitudes()
    {
        this.horizontalVelocity.velocity = Math.cos(velocity.radians)*velocity.velocity;
        this.horizontalVelocity.angle = (velocity.angle>90 && velocity.angle<270)?180:0;

        this.verticalVelocity.velocity = Math.sin(velocity.radians)*velocity.velocity;
        this.verticalVelocity.angle = (velocity.angle>=0 && velocity.angle<=180)?90:270;
    }

    public void createProjectileObject()
    {
        projectiles.add(new Rectangle(this.x, this.y, 50, 50, new InitialPoint(this.x, this.y, this.velocity)));
    }

    public void drawInitialPoint(int fWidth, int fHeight, int xOffset, Graphics2D g)
    {
        horizontalVelocity.drawVector(x, y, fWidth, fHeight, xOffset, g);
        verticalVelocity.drawVector(x, y, fWidth, fHeight, xOffset, g);
        velocity.drawVector(x, y, fWidth, fHeight, xOffset, g);
    }

    public String toString()
    {
        return "x=" + x + "  y=" + y + "  (Vx=" + horizontalVelocity + ")  (Vy=" + verticalVelocity + ")  (V=" + velocity + ")";
    }
}