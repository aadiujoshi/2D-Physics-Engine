import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class Simulator extends JPanel implements KeyListener, MouseListener
{
    //gooey
    private JFrame frame;
    private JFrame parametersFrame;
    private Parameters parameters;

    //physics
    private PhysicsHandler engine;
    private InitialPoint initial;

    private int fps;

    //scrolling
    private int xOffset;
    

    public Simulator(int width, int height)
    {
        engine = new PhysicsHandler();
        initial = new InitialPoint(0, 100, new Vector(100, 45));
        xOffset = 0;
        fps = 360;
        initWindow(width, height);
        mainloop();
    }

    public void initWindow(int width, int height)
    {
        frame = new JFrame("2D Physics Engine");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        parametersFrame = new JFrame("Parameters");
        parametersFrame.setSize(400, 200);

        parameters = new Parameters();

        JButton updateValuesButton = new JButton("Update Values");
        updateValuesButton.setFocusable(false);
        updateValuesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) 
            {
                // yandere code L
                parameters.updateToTextFieldValues();
                engine.gravity = parameters.changeParameter[0] ? (double)parameters.parameters[0] : engine.gravity;
             // initial.projLength = parameters.changeParameter[1] ? (int)parameters.parameters[1] : initial.projLength;
                initial.projWidth = parameters.changeParameter[2] ? (int)parameters.parameters[2] : initial.projWidth;
                initial.projHeight = parameters.changeParameter[3] ? (int)parameters.parameters[3] : initial.projHeight;
                initial.projMass = parameters.changeParameter[4] ? (double)parameters.parameters[4] : initial.projMass;
                initial.x = parameters.changeParameter[5] ? (double)parameters.parameters[5] : initial.x;
                initial.y = parameters.changeParameter[6] ? (double)parameters.parameters[6] : initial.y;
                initial.velocity.velocity = parameters.changeParameter[7] ? (double)parameters.parameters[7] : initial.velocity.velocity;
                initial.velocity.angle = parameters.changeParameter[8] ? (int)parameters.parameters[8] : initial.velocity.angle;
                engine.airResistance = parameters.changeParameter[9] ? (boolean)parameters.parameters[9] : engine.airResistance;
            }
        });
        parameters.add(updateValuesButton);
        parametersFrame.add(parameters);

        this.setLayout(null);

        frame.add(this);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        
        frame.setVisible(true);
        parametersFrame.setVisible(true);
        frame.validate();
    }
    
    public void mainloop()
    {
        while(true)
        {
            try { Thread.sleep(1000/fps); } 
            catch (InterruptedException e) {}

            initial.updateXYVectorMagnitudes();

            engine.calculateProjectileMotion(initial, initial.projectiles, 1000/fps);

            super.repaint();
        }
    }

    public void paintComponent(Graphics gr)
    {
        Graphics2D g = (Graphics2D)gr;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        initial.drawInitialPoint(frame.getWidth(), frame.getHeight(), xOffset, g);

        if(initial.projectiles != null) //draw projectiles
            for(Projectile so: initial.projectiles)
                so.drawObject(frame.getWidth(), frame.getHeight(), xOffset, g);

        g.dispose();
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyChar() == 'w')
            initial.y+=10;
        if(e.getKeyChar() == 'a')
            initial.x-=10;
        if(e.getKeyChar() == 's')
            initial.y-=10;
        if(e.getKeyChar() == 'd')
            initial.x+=10;
        
        if(e.getKeyChar() == 'r')
        {
            engine.gravity = 200;
            engine.airResistance = false;

            initial.projHeight = 50;
            // initial.projLength = 50;
            initial.projWidth = 50;
            initial.projMass = 100;
            initial.velocity.velocity = 100;
            initial.velocity.angle = 45;
            initial.velocity.radians = initial.velocity.angle*(Math.PI/180);
            initial.x = 0;
            initial.y = 100;
            
            xOffset = 0;
            initial.projectiles.clear();
        }

        if(e.getKeyChar() == ' ')
            initial.createProjectileObject();

        if(e.getKeyChar() == '+')
        {
            initial.velocity.velocity+=3;
            initial.updateXYVectorMagnitudes();
        }

        if(e.getKeyChar() == '-')
            if(initial.velocity.velocity-3 >= 0)
            {
                initial.velocity.velocity-=3;
                initial.updateXYVectorMagnitudes();
            }
        
        if(e.getKeyCode() == 37) //left
            xOffset-=20;
        if(e.getKeyCode() == 39) //right
            xOffset+=20; 
    }

    public void mousePressed(MouseEvent e) 
    {
        initial.updateVectorDirection(e.getX()-(frame.getWidth()/2)+xOffset, frame.getHeight()-e.getY());
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
