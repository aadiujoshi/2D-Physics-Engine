import java.awt.Graphics2D;

public class InitialPoint 
{
    double x; //also height (cartesian, NOT graphical coordinates)
    double y;

    Vector horizontalVelocity;
    Vector verticalVelocity;
    Vector velocity;

    public InitialPoint(double x, double y, Vector velocity)
    {
        this.x = x;
        this.y = y;
        this.velocity = velocity;

        this.horizontalVelocity = new Vector(Math.cos(velocity.radians)*velocity.velocity, (velocity.angle>90 && velocity.angle<270)?180:0);
        this.verticalVelocity = new Vector(Math.sin(velocity.radians)*velocity.velocity, (velocity.angle>=0 && velocity.angle<=180)?90:270);
    }

    public void updateVectorDirection(double ox, double oy) //point in mouse direction (cartesian points)
    {
        double cx = ox - this.x;
        double cy = oy - this.y;

        double theta = Math.atan2(cy, cx)*(180/Math.PI);
        theta = theta>=0 ? theta : 360+theta;

        this.velocity.angle = theta;
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

    public void drawInitialPoint(int fWidth, int fHeight, int xOffset, Graphics2D g)
    {
        horizontalVelocity.drawVector((int)x, (int)y, fWidth, fHeight, xOffset, g);
        verticalVelocity.drawVector((int)x, (int)y, fWidth, fHeight, xOffset, g);
        velocity.drawVector((int)x, (int)y, fWidth, fHeight, xOffset, g);
    }

    public String toString()
    {
        return "x=" + x + "  y=" + y + "  (Vx=" + horizontalVelocity + ")  (Vy=" + verticalVelocity + ")  (V=" + velocity + ")";
    }
}
