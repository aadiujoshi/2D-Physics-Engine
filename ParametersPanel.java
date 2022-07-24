import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ParametersPanel extends JPanel
{
    //array of parameter types as strings
    //used for jlabels
    String[] parameterStrings;

    // 11 values
    // gravity(0), projDepth(1), projWidth(2), projHeight(3), 
    // projMass(4), x(5), y(6), velocity(7), angle(8), 
    // airResistance(9), collision(10)

    double gravity;
    int projDepth;
    int projWidth;
    int projHeight;
    double projMass;
    double x;
    double y;
    double velocity;
    double angle;
    boolean airResistance;
    boolean collision;

    //array of whether or not to change the parameter
    //same order as before
    boolean[] changeParameter;

    //array of JTextFields (inputs)
    //same order as before
    JTextField[] textfields;

    //checkboxes
    JCheckBox airResistanceCheckBox;
    JCheckBox collisionCheckBox;

    public ParametersPanel()
    {
        parameterStrings = new String[]{"Gravity", "Projectile Depth", "Projectile Width", "Projectile height", "Projectile Mass", "Initial X", "Initial Y", "Initial Velocity", "Initial Angle"};
        changeParameter = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};
        textfields = new JTextField[9]; 
        
        airResistance = false;
        collision = false;

        for(int i = 0; i < textfields.length; i++)
        {
            this.add(new JLabel(parameterStrings[i]));
            textfields[i] = new JTextField();
            textfields[i].setPreferredSize(new Dimension(50, 25));
            this.add(textfields[i]);
        }

        airResistanceCheckBox = new JCheckBox("Air Resistance");
        airResistanceCheckBox.setFocusable(false);
        airResistanceCheckBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                changeParameter[9] = true; //always true
                airResistance = !airResistance;
            }
        });
        this.add(airResistanceCheckBox);

        collisionCheckBox = new JCheckBox("Collision");
        collisionCheckBox.setFocusable(false);
        collisionCheckBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                changeParameter[10] = true; //always true;
                collision = !collision;
            }
        });
        this.add(collisionCheckBox);
    }

    public void updateToTextFieldValues()
    {
        try
        {
            for(int i = 0; i < textfields.length; i++)
            {
                if(textfields[i].getText().trim().equals(""))
                    changeParameter[i] = false;
                else
                {
                    changeParameter[i] = true;
                    double n = Double.parseDouble(textfields[i].getText().trim());

                    switch(i)
                    {
                        case 0: gravity = n;
                            break;
                        case 1: projDepth = (int)n;
                            break;
                        case 2: projWidth = (int)n;
                            break;
                        case 3: projHeight = (int)n;
                            break;
                        case 4: projMass = n;
                            break;
                        case 5: x = n;
                            break;
                        case 6: y = n;
                            break;
                        case 7: velocity = n;
                            break;
                        case 8: angle = n;
                            break;
                    }
                }
            }
        }
        catch(java.lang.NumberFormatException e) { System.out.println("Numbers Only"); }
    }
}
