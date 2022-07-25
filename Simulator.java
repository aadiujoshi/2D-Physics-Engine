import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Simulator extends JPanel implements KeyListener, MouseListener
{
    //gooey
    private JFrame frame;
    private JFrame parametersFrame;
    private ParametersPanel parameters;
    volatile private boolean mouseHeld;

    //physics
    private PhysicsHandler engine;
    private InitialPoint initial;

    private volatile ArrayList<Projectile> projectiles;

    //projectile
    private double projDepth;
    private double projWidth;
    private double projHeight;
    private double projMass;

    private double blastRadius;
    private double blastForce;

    private int projType; //type of projectile that is shot | 1 -> rectangle, 2 -> explosive

    //rendering
    private double frameLocationX;
    private double frameLocationY;

    private double mouseX;
    private double mouseY;

    private Thread graphicsThread;
    private Thread mouseCursorThread;
    private Thread physicsHandlerThread;

    private boolean antialiasing;

    private final int FPS = 1000;
    private final int TICK_SPEED = 1000; //determines how many times per second projectile position is calculated
    // private final int FRAME_DELAY_NANOS = 500000;

    //scrolling
    private int xOffset;
    

    public Simulator(int width, int height)
    {
        this.engine = new PhysicsHandler();
        this.initial = new InitialPoint(0, 100, new Vector(100, 45));
        this.projectiles = new ArrayList<Projectile>();
        this.projHeight = 50;
        this.projDepth = 50;
        this.projWidth = 50;
        this.projMass = 100;
        this.blastRadius = 300;
        this.blastForce = 10;
        this.projType = 1;
        this.xOffset = 0;
        this.graphicsThread = new Thread(new Runnable(){
            public void run() {
                while(true){
                    nanoDelay(1000000000/FPS);
                    repaint();
                }
            }
        });
        this.mouseCursorThread = new Thread(new Runnable(){
            public void run() {
                while(true) {
                    mouseX = MouseInfo.getPointerInfo().getLocation().getX();
                    mouseY = MouseInfo.getPointerInfo().getLocation().getY();
                }
            }
        });
        this.physicsHandlerThread = new Thread(new Runnable(){
            public void run(){
                double mx;
                double my;
                while(true) {
                    nanoDelay(1000000000/TICK_SPEED);

                    mx = mouseX-frameLocationX;
                    my = mouseY-frameLocationY;
                    
                    if(mouseHeld)
                        initial.updateVectorDirection(mx-(frame.getWidth()/2)+xOffset, frame.getHeight()-my);

                    engine.calculateProjectileMotion(initial, projectiles, 1000/TICK_SPEED);
                }
            }
        });
        this.initWindow(width, height);
        this.antialiasing = true;

        //threads
        graphicsThread.start();
        mouseCursorThread.start();
        physicsHandlerThread.start();
    }

    public void initWindow(int width, int height)
    {
        frame = new JFrame("2D Physics Engine");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        parametersFrame = new JFrame("Parameter Values");
        parametersFrame.setSize(400, 200);
        parametersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parameters = new ParametersPanel();

        JButton updateValuesButton = new JButton("Apply");
        updateValuesButton.setFocusable(false);
        updateValuesButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                parameters.updateToTextFieldValues();

                for(int i = 0; i < parameters.changeParameter.length; i++)
                {
                    boolean b = parameters.changeParameter[i];
                    switch(i)
                    {
                        case 0: engine.gravity = b ? parameters.gravity : engine.gravity;
                            break;
                        case 1: projDepth = b ? parameters.projDepth : projDepth;
                            break;
                        case 2: projWidth = b ? parameters.projWidth : projWidth;
                            break;
                        case 3: projHeight = b ? parameters.projHeight : projHeight;
                            break;
                        case 4: projMass = b ? parameters.projMass : projMass;
                            break;
                        case 5: initial.x = b ? parameters.x : initial.x;
                            break;
                        case 6: initial.y = b ? parameters.y : initial.y;
                            break;
                        case 7: initial.velocity.velocity = b ? parameters.velocity : initial.velocity.velocity;
                            break;
                        case 8: 
                            if(b) //angle
                                initial.updateVectorDirection(initial.x+Math.cos((double)parameters.angle*(Math.PI/180)), initial.y+Math.sin((double)parameters.angle*(Math.PI/180)));
                            break;
                        case 9: blastRadius = b ? parameters.blastRadius : blastRadius;
                            break;
                        case 10: blastForce = b ? parameters.blastForce : blastForce;
                            break;
                        case 11: engine.airResistance = b ? parameters.airResistance : engine.airResistance;
                            break;
                        case 12: engine.collision = b ? parameters.collision : engine.collision;


                    }
                    initial.updateXYVectorMagnitudes();
                }
            }
        });
        parameters.add(updateValuesButton);
        parametersFrame.add(parameters);

        this.setLayout(null);

        frame.add(this);
        frame.addComponentListener(new ComponentListener(){
            public void componentMoved(ComponentEvent e) {
                try{
                    frameLocationX = getLocationOnScreen().getX();
                    frameLocationY = getLocationOnScreen().getY(); 
                } catch(java.awt.IllegalComponentStateException i){}
            }
            public void componentResized(ComponentEvent e) {  
                try{
                    frameLocationX = getLocationOnScreen().getX();
                    frameLocationY = getLocationOnScreen().getY(); 
                } catch(java.awt.IllegalComponentStateException i) {}
            }
            public void componentHidden(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
        });
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        
        frame.setVisible(true);
        
        this.frameLocationX = this.getLocationOnScreen().getX();
        this.frameLocationY = this.getLocationOnScreen().getY();

        parametersFrame.setVisible(true);
        frame.validate();
    }

    public void paintComponent(Graphics gr)
    {
        Graphics2D g = (Graphics2D)gr;
        if(antialiasing)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        initial.drawInitialPoint(frame.getWidth(), frame.getHeight(), xOffset, g);

        if(projectiles != null) //draw projectiles
            synchronized(projectiles){
                try{
                    for(Projectile so: projectiles)
                        so.drawObject(frame.getWidth(), frame.getHeight(), xOffset, g);
                } catch(ConcurrentModificationException e) {}
            }

        g.dispose();
    }

    public void nanoDelay(long nanos)
    {
        if(nanos < 0) return;
        final long end = System.nanoTime() + nanos;
        long timeLeft = nanos;
        do {
            timeLeft = end - System.nanoTime();
        } while (timeLeft > 0);
    }

    public void createProjectileObject()
    {
        synchronized(projectiles) {
            if(projType == 1)
                projectiles.add(new RectangleProjectile(new InitialPoint(initial.x, initial.y, new Vector(initial.velocity.velocity, initial.velocity.angle)), projDepth, projWidth, projHeight, projMass));
            if(projType == 2)
                projectiles.add(new ExplosiveProjectile(new InitialPoint(initial.x, initial.y, new Vector(initial.velocity.velocity, initial.velocity.angle)), projDepth, projWidth, projHeight, projMass, projWidth /*used as radius*/, blastRadius, blastForce));
        }
    }

    public void keyPressed(KeyEvent e)
    {
        //refactor and use switch statement
        if(e.getKeyChar() == 'w')
            initial.y+=10;
        if(e.getKeyChar() == 'a')
            initial.x-=10;
        if(e.getKeyChar() == 's')
            initial.y-=10;
        if(e.getKeyChar() == 'd')
            initial.x+=10; 
        
        if(e.getKeyChar() == 'b')
            projType = projType == 1 ? 2 : 1;
        
        if(e.getKeyChar() == 'r')
        {
            engine.gravity = 500;
            engine.airResistance = false;
            engine.collision = false;

            projHeight = 50;
            projDepth = 50;
            projWidth = 50;
            projMass = 100;

            initial = new InitialPoint(0, 100, new Vector(100, 45));
            
            blastRadius = 50;
            blastForce = 10;

            // initial.updateXYVectorMagnitudes();

            xOffset = 0;
            projectiles.clear();
        }

        if(e.getKeyChar() == ' ')
        {
            this.createProjectileObject();
            // System.out.println(projectiles.size());
        }

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

    public void mousePressed(MouseEvent e) { mouseHeld = true; }
    public void mouseReleased(MouseEvent e) { mouseHeld = false; }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
