import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class PhysicsHandler 
{
    double airDensity;
    boolean airResistance;
    
    double gravity;

    boolean collision;

    public PhysicsHandler()
    {
        this.gravity = 500;
        this.airResistance = false;
        this.airDensity = 1;
        this.collision = false;
    }

    public void calculateProjectileMotion(InitialPoint tPoint, ArrayList<Projectile> projectiles, double timeStep)
    {
        for(int i = 0; i < projectiles.size(); i++)
        {
            Projectile p;
            synchronized(projectiles){
                p = projectiles.get(i);
            }

            if(p.grounded) continue;

            if(groundCollision(p)){
                p.grounded = true;
                continue;
            }

            if(p instanceof ExplosiveProjectile)
                if(generalCollision(p, projectiles))
                    disperseExplosion((ExplosiveProjectile)p, projectiles);
                
            if(collision) 
                this.collision(p, projectiles);

            if(p.grounded) continue;

            p.x = p.initial.x + p.initial.horizontalVelocity.velocity*p.time;
            p.y = p.initial.y + p.initial.verticalVelocity.velocity*p.time + (-gravity/2)*(p.time*p.time);

            p.time+=timeStep/1000; //convert milliseconds to seconds

            try{ synchronized(projectiles){projectiles.set(i, p); } } 
            catch(IndexOutOfBoundsException e) { continue; }
        }
    }

    public void disperseExplosion(ExplosiveProjectile explosive, ArrayList<Projectile> projectiles)
    {
        collision = false;
        for(int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = projectiles.get(i);
            if(p instanceof ExplosiveProjectile){
                synchronized(projectiles){
                    projectiles.remove(i);
                }
                continue;
            }

            double distance = Math.sqrt(Math.pow(explosive.x-p.x, 2)+Math.pow(explosive.y-p.y, 2));
            double velocity = (p.mass/(distance/explosive.blastRadius)); //i am cheating and using acceleration as velocity since air resistance has not been implemented yet
            
            //calculate angle
            double cx = p.x - explosive.x;
            double cy = p.y - explosive.y;

            double theta = Math.atan2(cy, cx)*(180/Math.PI);
            theta = theta>=0 ? theta : 360+theta;

            //construct new initial point
            p.initial = new InitialPoint(p.x, p.y, new Vector(velocity, theta));
            p.grounded = false;

            // System.out.println(p.initial);

            try{ synchronized(projectiles){ projectiles.set(i, p); } } 
            catch(IndexOutOfBoundsException e) { continue; }
        }
    }

    public boolean groundCollision(Projectile p)
    {
        return p.y-p.width/2 <= 0;
    }

    public boolean generalCollision(Projectile p, ArrayList<Projectile> projectiles)
    {
        try{
            if(groundCollision(p)) return true;
            for(Projectile op : projectiles)
            {
                if(((p.x-p.width/2 < op.x+op.width/2 && p.x>op.x) || 
                    (p.x+p.width/2 > op.x-op.width/2 && p.x<op.x)) && 
                    ((p.y+p.height/2 > op.y-op.height/2 && p.y<op.y) || 
                    (p.y-p.height/2 < op.y+op.height/2 && p.y>op.y)))
                        return true;
            }
        } catch(ConcurrentModificationException e) {}
        return false;
    }

    public void bottomProjectileCollision(Projectile p, ArrayList<Projectile> projectiles)
    {
        try{
            for(Projectile op : projectiles)
            {
                if(((p.x-p.width/2 < op.x+op.width/2 && p.x>op.x) || 
                    (p.x+p.width/2 > op.x-op.width/2 && p.x<op.x)) && 
                    ((p.y+p.height/2 > op.y-op.height/2 && p.y<op.y) || 
                    (p.y-p.height/2 < op.y+op.height/2 && p.y>op.y)))
                        if((p.height/2+op.height/2)-(p.y-op.y) < 0.1)
                            p.grounded = true;
            }
        } catch(ConcurrentModificationException e) {}
    }

    public int projectileCollision(Projectile p, ArrayList<Projectile> projectiles) //returns 0, 1, or 2 -> no collision, side collision, bottom collision
    {
        try{
            for(Projectile op : projectiles)
            {
                if(((p.x-p.width/2 < op.x+op.width/2 && p.x>op.x) || 
                    (p.x+p.width/2 > op.x-op.width/2 && p.x<op.x)) && 
                    ((p.y+p.height/2 > op.y-op.height/2 && p.y<op.y) || 
                    (p.y-p.height/2 < op.y+op.height/2 && p.y>op.y)))
                        if((p.width/2+op.width/2)-(Math.abs(p.x-op.x)) < (p.height/2+op.height/2)-(p.y-op.y) && op.grounded == true)
                            return 1;
                        else if(op.grounded == true)
                            return 2;
            }   
        } catch(ConcurrentModificationException e) {}
        return 0;
    }

    public void collision(Projectile p, ArrayList<Projectile> projectiles)
    {
        if(p.initial.velocity.velocity == 0)//if already hit edge
        { 
            bottomProjectileCollision(p, projectiles);
            return;
        }

        int c = projectileCollision(p, projectiles);
        
        if(c == 0) return;

        if(groundCollision(p) || c == 2)
        {
            p.grounded = true;
            return;
        }

        if(c == 1)
        {
            p.time = 0;
            p.initial = new InitialPoint(p.x, p.y, new Vector(0, 270));
        }
    }
}
