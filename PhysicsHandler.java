import java.util.ArrayList;

public class PhysicsHandler 
{
    public PhysicsHandler(){}

    public void calculateProjectileMotion(InitialPoint tPoint, ArrayList<Projectile> projectiles, double timeStep, double gravity)
    {
        for(Projectile p: projectiles)
        {
            if(p.initial.y + p.initial.verticalVelocity.velocity*p.time + (-gravity/2)*(p.time*p.time)-(p.height/2)-40 <= 0)
                continue;

            p.x = p.initial.x + p.initial.horizontalVelocity.velocity*p.time;
            p.y = p.initial.y + p.initial.verticalVelocity.velocity*p.time + (-gravity/2)*(p.time*p.time);
            p.time+=timeStep/1000; //convert milliseconds to seconds
        }
    }
}
