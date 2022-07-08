import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Parameters extends JPanel
{
    //array of parameter types as strings
    //used for jlabels
    String[] parameterStrings;

    //array of all parameters (multiple data types)
    // gravity(0), projLength(1), projWidth(2), projHeight(3), projMass(4), x(5), y(6), velocity(7), angle(8), airResistance(9)
    Object[] parameters;

    //array of whether or not to change the parameter
    //same order as before
    boolean[] changeParameter;

    //array of JTextFields (inputs)
    //same order as before
    JTextField[] textfields;

    //air resistance checkbox
    JCheckBox airResistanceCheckBox;

    public Parameters()
    {
        parameterStrings = new String[]{"Gravity", "Projectile Length", "Projectile Width", "Projectile height", "Projectile Mass", "Initial X", "Initial Y", "Initial Velocity", "Initial Angle"};
        parameters = new Object[]{300.0, 50, 50, 50, 100.0, 0.0, 100.0, 200.0, 45, false};
        changeParameter = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        textfields = new JTextField[9]; //exclude airResistance
        
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
            @Override
            public void itemStateChanged(ItemEvent e) {
                parameters[9] = !(boolean)parameters[9];
                //more textfields :(
            }
        });
        this.add(airResistanceCheckBox);
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
                    if(parameters[i].getClass() == Double.class)
                        parameters[i] = Double.parseDouble(textfields[i].getText().trim());
                    else if(parameters[i].getClass() == Integer.class)
                        parameters[i] = Integer.parseInt(textfields[i].getText().trim());
                }
            }
        }
        catch(java.lang.NumberFormatException e) { System.out.println("Numbers Only"); }
    }
}
