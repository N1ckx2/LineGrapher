/* Nicholas Vadivelu
 * 19 October 2015
 * ICS 4U1
 * Line Class Assignment
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LineGrapherMain extends JFrame {
    private java.util.List<Line> line = new ArrayList<Line>(); //user inputted lines
    private java.util.List<Double[]> clickedPoints = new ArrayList<Double[]>();
    private JButton b_equation, b_left, b_right, b_down, b_up, b_bounds, b_zoomin, b_zoomout, b_reset;
    private JTextField tf_equation, tf_top, tf_bottom, tf_rbound, tf_lbound;
    private JPanel p_content, p_display, p_text;
    private JLabel[] xint, yint, slope, eqntext;
    private JLabel intersect, blue, red;
    private DrawArea display;
    private String[] equations; //raw user input for the equations
    private GridBagConstraints gbc = new GridBagConstraints(); //used for item positioning
    private int numEqn = 0, checker = 0; //used to count the total number of equations
    JComboBox<String> jcb_equations, jcb_equations2;
    //private boolean arePointsSet;


    /*Check Methods**************************************************
     * These methods are used to format the output appropriately, so that infinities and NaN are accounted for
     */
    public String check(Double num, String prefix) {
        num = (Math.round(num * 100.0) / 100.0);
        String item = Double.toString(num);
        if (item.equals("NaN") || item.equals("Infinity") || item.equals("-Infinity"))
            return "Does not exist";
        else
            return prefix + (item);
    }

    public String check(Double num) {
        num = (Math.round(num * 100.0) / 100.0);
        String item = Double.toString(num);
        if (item.equals("NaN") || item.equals("Infinity") || item.equals("-Infinity"))
            return "Does not exist";
        else
            return (item);
    }

    public String check(String item) {
        if (item.equals("NaN") || item.equals("Infinity") || item.equals("-Infinity"))
            return "Does not exist";
        else
            return (item);
    }

    public String check(Double x, Double y) {
        x = (Math.round(x * 100.0) / 100.0);
        y = (Math.round(y * 100.0) / 100.0);
        String item = Double.toString(x);
        if (item.equals("NaN") || item.equals("Infinity") || item.equals("-Infinity"))
            return "Does not exist";
        item = Double.toString(y);
        if (item.equals("NaN") || item.equals("Infinity") || item.equals("-Infinity"))
            return "Does not exist";
        else
            return (new Point(x, y)).toString();
    }

    public double round(double x) //rounds to 2 decimal places
    {
        return Math.round(x * 100.0) / 100.0;
    }

    public LineGrapherMain() {
        //Setting up JFrame
        setTitle("Nicholas Vadivelu's Line Graphing Tool"); //title set
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //so users can close the window with the x
        setVisible(true); //visibility
        setLocationRelativeTo(null);           // Center window
        //Give User more INfo

        JOptionPane.showMessageDialog(null, "<html><p>Welcome to Nicholas Vadivelu's Graphing Tool!<p><p>Hover over each element for more information</p><p></p><p>When entering equations, enter them in any form, seperate equations using a semicolon(;).</p><p>Please ensure the following: </p> <ul><li>Integers only</li><li>The number must precede the variable (e.g. 2x)</li><li>No double negatives</li><li> Only spaces, +, -, and = are allowed </li> </ul> <p>To change the boundaries on the graph, you can either use the on screen buttons or enter the values in the boxes.</p><p></p><p>You can click on the graph to get the points for each function at that x value</p><p>Finally, you can drag the graph to pan across it </p></html>");
        setLocationRelativeTo(null);
        line.add(new Line()); //this line is set up to prevent a nullpointer error

        //setting up content panes
        p_content = new JPanel();
        p_display = new JPanel(new BorderLayout());
        p_text = new JPanel(new GridBagLayout());

        //Setting up equation input
        gbc.gridy = 0;
        b_equation = new JButton("Enter");
        b_equation.addActionListener(new ButtonListener());
        tf_equation = new JTextField(7);
        tf_equation.setToolTipText("<html><p>Enter equations in any form, seperate equations using a semicolon(;)</p><p></p> <p>Please ensure the following: </p> <ul><li>Integers only</li><li>The number must precede the variable (e.g. 2x)</li><li>No double negatives</li><li> Only spaces, +, -, and = are allowed </li> </ul></html>");
        b_equation.setToolTipText("<html><p>Enter equations in any form, seperate equations using a semicolon(;)</p><p></p> <p>Please ensure the following: </p> <ul><li>Integers only</li><li>The number must precede the variable (e.g. 2x)</li><li>No double negatives</li><li> Only spaces, +, -, and = are allowed </li> </ul></html>");
        p_text.add(tf_equation, gbc);
        p_text.add(b_equation, gbc);

        //Set up data display
        xint = new JLabel[2];
        yint = new JLabel[2];
        jcb_equations = new JComboBox<String>();

        //Change displayed information based on the information from the line
        jcb_equations.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < equations.length; i++) {
                    if (equations[i].equals(e.getItem())) {
                        if (!line.get(i).isVertical()) {
                            yint[0].setText("Y Intercept: " + check(0.0, line.get(i).yint()));
                            slope[0].setText("Slope: " + check(line.get(i).slope()));
                        } else {
                            yint[0].setText("Y Intercept does not exist.");
                            slope[0].setText("Slope is undefined");
                        }
                        if (!line.get(i).isHorizontal())
                            xint[0].setText("X Intercept: " + check(line.get(i).xint(), 0.0));
                        else
                            xint[0].setText("X Intercept does not exist.");

                        eqntext[0].setText("Equation: " + check(line.get(i).toString()));
                        for (int j = 0; j < equations.length; j++) {
                            if (equations[j].equals(jcb_equations2.getSelectedItem()))
                                intersect.setText("Intersect: " + check(line.get(i).intersect(line.get(j))));
                        }
                    }
                    pack();
                }
            }
        });
        jcb_equations2 = new JComboBox<String>();

        //Change displayed information based on the information from the line
        jcb_equations2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < equations.length; i++) {
                    if (equations[i].equals(e.getItem())) {
                        if (!line.get(i).isVertical()) {
                            yint[1].setText("Y Intercept: " + check(0.0, line.get(i).yint()));
                            slope[1].setText("Slope: " + check(line.get(i).slope()));
                        } else {
                            yint[1].setText("Y Intercept does not exist.");
                            slope[1].setText("Slope is undefined");
                        }
                        if (!line.get(i).isHorizontal())
                            xint[1].setText("X Intercept: " + check(line.get(i).xint(), 0.0));
                        else
                            xint[1].setText("X Intercept does not exist.");

                        eqntext[1].setText("Equation: " + check(line.get(i).toString()));
                        for (int j = 0; j < equations.length; j++) {
                            if (equations[j].equals(jcb_equations.getSelectedItem()))
                                intersect.setText("Intersect: " + check(line.get(i).intersect(line.get(j))));
                        }
                    }
                    pack();
                }
            }
        });
        slope = new JLabel[2];
        eqntext = new JLabel[2];
        intersect = new JLabel("Intersect: ");
        blue = new JLabel("Blue Line");
        red = new JLabel("Red Line");

        //putting the data elements onto panel
        gbc.gridy++;
        p_text.add(jcb_equations, gbc);
        gbc.gridx = 1;
        p_text.add(new JLabel("<html><font color='blue'>Blue Line</font></html>"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        xint[0] = new JLabel("X Intercept: x = ");
        p_text.add(xint[0], gbc);
        gbc.gridy++;
        yint[0] = new JLabel("Y Intercept: y = ");
        p_text.add(yint[0], gbc);
        gbc.gridy++;
        slope[0] = new JLabel("Slope: ");
        p_text.add(slope[0], gbc);
        gbc.gridy++;
        eqntext[0] = new JLabel("Equation: ");
        p_text.add(eqntext[0], gbc);
        gbc.gridy++;
        p_text.add(new JLabel(" "), gbc);
        gbc.gridy++;
        p_text.add(jcb_equations2, gbc);
        gbc.gridx = 1;
        p_text.add(new JLabel("<html><font color='red'>Red Line</font></html>"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        xint[1] = new JLabel("X Intercept: x = ");
        p_text.add(xint[1], gbc);
        gbc.gridy++;
        yint[1] = new JLabel("Y Intercept: y = ");
        p_text.add(yint[1], gbc);
        gbc.gridy++;
        slope[1] = new JLabel("Slope: ");
        p_text.add(slope[1], gbc);
        gbc.gridy++;
        eqntext[1] = new JLabel("Equation: ");
        p_text.add(eqntext[1], gbc);
        gbc.gridy += 2;
        p_text.add(new JLabel(" "), gbc);
        gbc.gridy++;
        p_text.add(intersect, gbc);
        gbc.gridy++;
        p_text.add(new JLabel(" "), gbc);
        gbc.gridy++;

        //Set up left right up and down bounds
        p_text.add(new JLabel("Bounds: "), gbc);
        gbc.gridy++;
        b_bounds = new JButton("Enter");
        b_bounds.setToolTipText("Set the boundaries for the graph. Please ensure that the numbers are integers");
        b_bounds.addActionListener(new ButtonListener());
        tf_top = new JTextField(5);
        tf_bottom = new JTextField(5);
        tf_rbound = new JTextField(5);
        tf_lbound = new JTextField(5);
        tf_top.setText("250");
        tf_bottom.setText("-250");
        tf_rbound.setText("250");
        tf_lbound.setText("-250");
        tf_top.setToolTipText("Enter the top boundary for the graph. Please make sure this number is an integer");
        tf_bottom.setToolTipText("Enter the bottom boundary for the graph. Please make sure this number is an integer");
        tf_rbound.setToolTipText("Enter the right boundary for the graph. Please make sure this number is an integer");
        tf_lbound.setToolTipText("Enter the left boundary for the graph. Please make sure this number is an integer");

        p_text.add(new JLabel("Top: "), gbc);
        gbc.gridx = 1;
        p_text.add(tf_top, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        p_text.add(new JLabel("Bottom: "), gbc);
        gbc.gridx = 1;
        p_text.add(tf_bottom, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        p_text.add(new JLabel("Right: "), gbc);
        gbc.gridx = 1;
        p_text.add(tf_rbound, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        p_text.add(new JLabel("Left: "), gbc);
        gbc.gridx = 1;
        p_text.add(tf_lbound, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        p_text.add(b_bounds, gbc);
        gbc.gridy++;

        //Zooming in and out
        p_text.add(new JLabel(" "), gbc);
        gbc.gridy++;

        b_zoomin = new JButton("Zoom In");
        b_zoomin.setToolTipText("Zoom into the graph");
        b_zoomin.addActionListener(new ButtonListener());
        p_text.add(b_zoomin, gbc);
        gbc.gridx = 1;

        b_zoomout = new JButton("Zoom Out");
        b_zoomout.setToolTipText("Zoom out of the graph");
        b_zoomout.addActionListener(new ButtonListener());
        p_text.add(b_zoomout, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        b_reset = new JButton("Reset All");
        b_reset.setToolTipText("Resets the scale and clears the lines");
        b_reset.addActionListener(new ButtonListener());
        p_text.add(b_reset, gbc);
        gbc.gridy++;


        //sets up display
        display = new DrawArea(500, 500);
        //arePointsSet = false;
        Double[] holdit = {0.0, 0.0};
        clickedPoints.add(holdit);
        display.addMouseListener(new MouseAdapter() //Allows you to get x, y at a position
        {
            int x, y;

            public void mouseClicked(MouseEvent e) {
                Double[][] holder = display.mousePoints(e.getX());
                clickedPoints.clear();
                for (int i = 0; i < line.size(); i++) {
                    clickedPoints.add(holder[i]);
                }
                //arePointsSet = true;
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                x -= e.getX();
                y -= e.getY();
                display.changeBounds(-y, x);
                repaint();
            }
        });

        p_display.add(display, "Center");

        b_up = new JButton("U");
        b_up.setToolTipText("Go Up");
        b_up.addActionListener(new ButtonListener());
        p_display.add(b_up, "North");

        b_down = new JButton("D");
        b_down.setToolTipText("Go Down");
        b_down.addActionListener(new ButtonListener());
        p_display.add(b_down, "South");

        b_left = new JButton("L");
        b_left.setToolTipText("Go Left");
        b_left.addActionListener(new ButtonListener());
        p_display.add(b_left, "West");

        b_right = new JButton("R");
        b_right.setToolTipText("Go Right");
        b_right.addActionListener(new ButtonListener());
        p_display.add(b_right, "East");


        //adding panels to frames
        p_content.add(p_text);
        p_content.add(p_display);
        setContentPane(p_content);
        pack(); //condenses the window to the appropriate size.
    }

    public void readLine() {
        int numEquations = 1; //starting out with one equation already
        String text = tf_equation.getText().toLowerCase(); //gets the raw input from the text field
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ';') //every semi colon seperates an equation
                numEquations++;
            else if ("1234567890+-=xy;".indexOf(text.charAt(i)) == -1) //eliminates unwanted characters
            {
                text = text.substring(0, i) + text.substring(i + 1);
                //System.out.println(text);
                i--;
            }
        }

        //seperates the equations into the string array
        numEqn = numEquations;
        equations = new String[numEqn];
        text = text + ";";
        for (int i = 0, j = 0, n = 0; j < text.length() && i < equations.length; i++, j++) {
            n = j;
            if (j < text.length()) {
                while (text.charAt(j) != ';') {
                    j++;
                }
            }
            equations[i] = text.substring(n, j);
        }

        //iterates through the string array and converts them all into Line objects
        line.clear();
        for (int i = 0; i < numEquations; i++) {
            line.add(new Line(equations[i]));
        }
        //deleted code.txt was here
        checker = line.size();
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == b_equation) //User submits equations
            {
                readLine();
                jcb_equations.removeAllItems();
                jcb_equations2.removeAllItems();
                for (int i = 0; i < equations.length; i++) {
                    jcb_equations.addItem(equations[i]);
                    jcb_equations2.addItem(equations[i]);
                }

            }
            //Panning Methods for moving the screen*********************
            else if (e.getSource() == b_up) {
                display.changeBounds(40, 0);
            } else if (e.getSource() == b_down) {
                display.changeBounds(-40, 0);
            } else if (e.getSource() == b_right) {
                display.changeBounds(0, 40);
            } else if (e.getSource() == b_left) {
                display.changeBounds(0, -40);
            }
            //Setting up custom boundaries***************************************
            else if (e.getSource() == b_bounds) {
                int ubound, lbound, dbound, rbound;

                if (Integer.valueOf(tf_lbound.getText()) < Integer.valueOf(tf_rbound.getText())) //makes sure the numbers are in the right order
                {
                    lbound = Integer.valueOf(tf_lbound.getText());
                    rbound = Integer.valueOf(tf_rbound.getText());
                } else {
                    rbound = Integer.valueOf(tf_lbound.getText());
                    lbound = Integer.valueOf(tf_rbound.getText());
                }

                if (Integer.valueOf(tf_top.getText()) > Integer.valueOf(tf_bottom.getText())) {
                    ubound = Integer.valueOf(tf_top.getText());
                    dbound = Integer.valueOf(tf_bottom.getText());
                } else {
                    dbound = Integer.valueOf(tf_top.getText());
                    ubound = Integer.valueOf(tf_bottom.getText());
                }

                display.draw(lbound, rbound, dbound, ubound);
            }

            //Zooming in and out
            else if (e.getSource() == b_zoomin) {
                display.zoomIn();
            } else if (e.getSource() == b_zoomout) {
                display.zoomOut();
            } else if (e.getSource() == b_reset) {
                display.draw(-250, 250, -250, 250);
                line.clear();
                clickedPoints.clear();
                tf_equation.setText("");

            }
            tf_top.setText(Integer.toString(display.boundaries()[0]));
            tf_bottom.setText(Integer.toString(display.boundaries()[1]));
            tf_lbound.setText(Integer.toString(display.boundaries()[2]));
            tf_rbound.setText(Integer.toString(display.boundaries()[3]));
            repaint();
        }
    }

  /*
  class KeyEvent extends KeyAdapter // for the side arrow keys and WASD
  {
    public void keyPressed(KeyEvent e)
    {
      if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
        display.changeBounds(40, 0);

      repaint();
    }
  }*/

    class DrawArea extends JPanel {
        int left, right, top, bottom;

        public DrawArea(int width, int height) {
            this.setPreferredSize(new Dimension(width, height)); // size
            top = 250;
            bottom = -250;
            left = -250;
            right = 250;
        }

        //Allows the user to pan in different directions
        public void changeBounds(int vertical, int horizontal) {
            top += vertical / 2;
            bottom += vertical / 2;
            left += horizontal / 2;
            right += horizontal / 2;
        }

        //Allows the user to change the dimensions for the graph
        public void draw(int minX, int maxX, int minY, int maxY) {
            top = maxY;
            bottom = minY;
            left = minX;
            right = maxX;
        }

        public int[] boundaries() //allows access of the directions
        {
            int[] bounds = {top, bottom, left, right};
            return bounds;
        }

        public Double[][] mousePoints(int x) //displays points that the mouse is at
        {
            Double[][] points = new Double[line.size()][2];
            for (int i = 0; i < line.size(); i++) {
                if (!line.get(i).isVertical() || line.get(i).xint() == x - (left / (double) (left - right) * 500)) {
                    points[i][0] = x - (left / (double) (left - right) * 500);
                    points[i][1] = line.get(i).getY(x - (left / (double) (left - right) * 500));
                }
            }
            return points;
        }


        //Allows the user to zoom in until the decimals get too small
        public void zoomIn() {
            if (top - bottom > 25) {
                top /= 2;
                bottom /= 2;
                left /= 2;
                right /= 2;
            }
        }

        //Allows the user to zoom out until the numbers become too large to view
        public void zoomOut() {
            if (top - bottom < 100000) {
                top *= 2;
                bottom *= 2;
                left *= 2;
                right *= 2;
            }
        }

        public void paintComponent(Graphics g) {
            //filling in the grid
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 500, 500);
            g.setColor(Color.BLACK);
            //drawing axis
            for (int i = 0; i < 25; i++) {
                g.setColor(Color.LIGHT_GRAY); //grey gridlines
                g.drawLine(0, i * 20 + 10, 500, i * 20 + 10); //horizontal lines
                g.drawLine(2 * i * 20 + 10, 0, 2 * i * 20 + 10, 500); //vertical lines
            }
            for (int i = 0; i < 25; i++) {
                g.setColor(Color.BLACK); //black axis and text
                if (i % 2 == 0) {
                    if (((right - left) / 25.0) > 1) //for large scales
                        g.drawString(Integer.toString((int) ((left - 10 / (500.0 / (right - left)) + (right - left) / 25.0) + i * (right - left) / 25.0)), i * 20, (int) (top / (double) (top - bottom) * 500 + 15));//horizontal axis
                    else //for decimal scales
                        g.drawString(Double.toString(round(((left - 10 / (500.0 / right - left) + (right - left) / 25.0)) + i * (right - left) / 25.0)), i * 20, (int) (top / (double) (top - bottom) * 500 + 15));

                }
                if ((top - (top - bottom) / 25.0 - i * (top - bottom) / 25.0 + 10 / (500.0 / (top - bottom))) != 0.0) {
                    if (((top - bottom) / 25.0) > 1) //for large scales
                        g.drawString(Integer.toString((int) (top - (top - bottom) / 25.0 - i * (top - bottom) / 25.0 + 10 / (500.0 / (top - bottom)))), (int) (left / (double) (left - right) * 500 + 4), (i * 20) + 14); //vertical axis
                    else //for decimal scales
                        g.drawString(Double.toString(round(top - (top - bottom) / 25.0 - i * (top - bottom) / 25.0 + 10 / (500.0 / (top - bottom)))), (int) (left / (double) (left - right) * 500 + 4), (i * 20) + 14);
                }

            }
            //Axis lines
            g.drawLine(0, (int) (top / (double) (top - bottom) * 500), 500, (int) (top / (double) (top - bottom) * 500));// horizontal axis
            g.drawLine((int) (left / (double) (left - right) * 500), 0, (int) (left / (double) (left - right) * 500), 500); //vertix line

            //Draws the actual lines
            for (int i = 0; i < line.size(); i++) {
                if (line.size() > 1) {
                    //Sets the color of the lines
                    if (equations[i].equals(jcb_equations.getSelectedItem()))
                        g.setColor(Color.BLUE);
                    else if (equations[i].equals(jcb_equations2.getSelectedItem()))
                        g.setColor(Color.RED);
                }
                if (line.get(i).isVertical()) //vertical lines
                    g.drawLine((int) ((left / (double) (left - right) * 500) + line.get(i).xint() / (left - right) * -500.0), 0, (int) ((left / (double) (left - right) * 500) + line.get(i).xint() / (left - right) * -500.0), 500);
                else
                    g.drawLine(0, (int) (-line.get(i).getY(left) / (top / 250.0) + (top / (double) (top - bottom) * 500)), 500, (int) (-line.get(i).getY(right) / (top / 250.0) + (top / (double) (top - bottom) * 500)));
                g.setColor(Color.BLACK);


            }
            //drawing on points (please work)
            int liner = 15;
            try {
                for (int r = 0; r < line.size(); r++) {
                    if (!line.get(r).isVertical() && liner < 250) {
                        g.drawString(equations[r] + ": " + (new Point(clickedPoints.get(r)[0], clickedPoints.get(r)[1])).toString(), 15, liner);//(int) (clickedPoints[r][2]), (int)(clickedPoints[r][3]));
                        liner += 20;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.println("Index out of bounds for points");
            }
            //arePointsSet = true;
        }
    }

    public static void main(String[] args) {
        LineGrapherMain main = new LineGrapherMain();

        //Required Code
        //Line line2 = new Line(1, 2, 3);  // x + 2y + 3 = 0
        //System.out.println("The equation is: " + line2); // overwrite the toString method
       //System.out.println("The x-intercept is " + line2.xint());
    }
}


/*
 * Line Class Assignment
 *
 The equation of a line is defined by three coefficients in the equation Ax + By + C = 0.  What can you do with lines?  Create them, display them (as an equation or graphically), find their slope and intercepts, find a point of intersection with another line, etc.

 Line line = new Line(1,2,3);  // x + 2y + 3 = 0
 System.out.println("The equation is: " + line); // overwrite the toString method
 System.out.println("The x-intercept is " + line.xint());

 Here is a partial Line class as described above.

 * 1.  Improve the toString method so that 0 terms, 1 coefficients don't appear (x - 2y = 0 instead of 1x + -2y + 0 = 0)
 *
 2.  Create a user-friendly readLine method that reads in a linear equation from a JTextField (the more flexible, the better - Bonus!!).

 3.  Create methods to find the intercepts and slope, boolean methods to determine if the line is vertical / horizontal.  That is five little methods.

 4.  Create a small Point class with data fields x, y.  Add an intersect method to the Line class that accepts a Line object and determines the point of intersection with the invoking Line, then returns a Point object.

 5.  Create a draw method that accepts the maximum and minimum x and y values for the graph, then draws labeled axes and the line.

 6.  A GUI-driven program that allows the class to be thoroughly tested in a user-friendly way.
 */