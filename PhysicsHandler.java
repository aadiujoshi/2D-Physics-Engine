import java.util.ArrayList;

public class PhysicsHandler 
{
    double gravity;
    boolean airResistance;

    public PhysicsHandler()
    {
        this.gravity = 300;
        this.airResistance = false;
    }

    public void calculateProjectileMotion(InitialPoint tPoint, ArrayList<Projectile> projectiles, double timeStep)
    {
        for(Projectile p: projectiles)
        {
            if(p.initial.y + p.initial.verticalVelocity.velocity*p.time + (-gravity/2)*(p.time*p.time)-(p.height/2) <= 0)
                continue;

            p.x = p.initial.x + p.initial.horizontalVelocity.velocity*p.time;
            p.y = p.initial.y + p.initial.verticalVelocity.velocity*p.time + (-gravity/2)*(p.time*p.time);
            p.time+=timeStep/1000; //convert milliseconds to seconds
        }
    }
}
