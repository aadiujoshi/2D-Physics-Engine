import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class Simulator extends JPanel implements KeyListener, MouseListener
{
    //gooey
    JFrame frame;

    //physics
    PhysicsHandler engine;
    InitialPoint iPoint;

    double gravity;

    //scrolling
    int xOffset;
    

    public Simulator(int width, int height, double gravity)
    {
        engine = new PhysicsHandler();
        iPoint = new InitialPoint(0, 100, new Vector(100, 45));
        xOffset = 0;
        this.gravity = gravity;
        initWindow(width, height);
        mainloop();
    }

    public void initWindow(int width, int height)
    {
        frame = new JFrame("2D Physics Engine");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setLayout(null);

        frame.add(this);
        
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.validate();
    }
    
    public void mainloop()
    {
        int fps = 144;
        while(true)
        {
            try { Thread.sleep(1000/fps); } 
            catch (InterruptedException e) {}

            engine.calculateProjectileMotion(iPoint, iPoint.projectiles, 1000/fps, gravity);

            super.repaint();
        }
    }

    public void paintComponent(Graphics gr)
    {
        Graphics2D g = (Graphics2D)gr;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        iPoint.drawInitialPoint(frame.getWidth(), frame.getHeight(), xOffset, g);

        if(iPoint.projectiles != null) //draw projectiles
            for(Projectile so: iPoint.projectiles)
                so.drawObject(frame.getWidth(), frame.getHeight(), xOffset, g);

        g.dispose();
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyChar() == 'w')
            iPoint.y+=10;
        if(e.getKeyChar() == 'a')
            iPoint.x-=10;
        if(e.getKeyChar() == 's')
            iPoint.y-=10;
        if(e.getKeyChar() == 'd')
            iPoint.x+=10;
        
        if(e.getKeyChar() == 'r')
        {
            iPoint.velocity.velocity = 100;
            iPoint.velocity.angle = 45;
            iPoint.velocity.radians = iPoint.velocity.angle*(Math.PI/180);
            iPoint.x = 0;
            iPoint.y = 100;
            
            xOffset = 0;

            iPoint.projectiles.clear();
        }

        if(e.getKeyChar() == ' ')
            iPoint.createProjectileObject();

        if(e.getKeyChar() == '+')
        {
            iPoint.velocity.velocity+=3;
            iPoint.updateXYVectorMagnitudes();
        }

        if(e.getKeyChar() == '-')
            if(iPoint.velocity.velocity-3 >= 0)
            {
                iPoint.velocity.velocity-=3;
                iPoint.updateXYVectorMagnitudes();
            }
        
        if(e.getKeyCode() == 37) //left
            xOffset-=20;
        if(e.getKeyCode() == 39) //right
            xOffset+=20; 
    }

    public void mousePressed(MouseEvent e) 
    {
        iPoint.updateVectorDirection(e.getX()-(frame.getWidth()/2)+xOffset, frame.getHeight()-e.getY());
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
