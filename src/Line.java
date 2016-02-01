/**
 * Created by Nicholas on 2016-01-31.
 */
class Line {
    private double A, B, C; // for Ax + By = C

    //Constructors
    public Line() //default constructor
    {
        A = 0;
        B = 0;
        C = 0;
    }

    public Line(double a, double b, double c) //constructor with values
    {
        A = a;
        B = b;
        C = c;
    }

    public Line(String text) //creates a new line using a string equation
    {
        readLine(text);
    }

    //to String Method
    public String toString() {
        String equation = "";
        //scenerios for A
        if (A == 1)
            equation = "x";
        else if (A == -1)
            equation = "-x";
        else if (A != 0)
            equation = (int) A + "x";

        //Scenarios for B
        if (B == 1)
            equation = equation + " + y";
        else if (B == -1)
            equation = equation + " - y";
        else if (B < 0)
            equation = equation + " - " + (int) (-B) + "y";
        else if (B > 0)
            equation = equation + " + " + (int) B + "y";

        //Scenarios for C
        if (C > 0)
            equation = equation + " + " + (int) C;
        else if (C < 0)
            equation = equation + " - " + (int) (-C);

        equation = equation + " = 0";
        return equation;
    }

    public double getY(double x) //returns y value for given x
    {
        return x * slope() + yint();
    }

    public double xint() //returns x intercept
    {
        return (double) -C / A;
    }

    public double yint() //returns y intercept
    {
        return (double) -C / B;
    }

    public double slope() //returns slope
    {
        return (double) -A / B;
    }

    public boolean isVertical() //returns true/ false for whether the line is vertical or not
    {
        if (B == 0)
            return true;
        else
            return false;
    }

    public boolean isHorizontal()  //returns true/ false for whether the line is horizontal or not
    {
        if (A == 0)
            return true;
        else
            return false;
    }

    public void readLine(String text) //reads the string equation and converts it into a line
    {
        double a = 0, b = 0, c = 0, pre = 1, temp = 0;
        boolean negative = false;
        for (int j = 0; j < text.length(); j++) //go through entire string
        {
            //System.out.println(text.charAt(j) + ", " + a + ", " + b + ", " + c + ", " + temp);
            if ("1234567890".indexOf(text.charAt(j)) != -1) //if its a number
            {
                temp *= 10;
                if (negative)
                    temp -= pre * (text.charAt(j) - 48);
                else
                    temp += pre * (text.charAt(j) - 48);

            } else if (text.charAt(j) == 'x') {
                if (j == 0 && a == 0)
                    a = 1;
                else if (("0987654321".indexOf(text.charAt(j - 1)) == -1 && a == 0) || temp == 0) {
                    if (negative)
                        a -= 1 * pre;
                    else
                        a += 1 * pre;
                } else
                    a += temp;
                if (a == 0 && negative)
                    a = -1;
                temp = 0;
                negative = false;
            } else if (text.charAt(j) == 'y') {
                if (j == 0 && b == 0)
                    b = 1;
                else if (("0987654321".indexOf(text.charAt(j - 1)) == -1 && b == 0) || temp == 0) {
                    if (negative)
                        b -= 1 * pre;
                    else
                        b += 1 * pre;
                } else
                    b += temp;
                temp = 0;
                negative = false;
            } else if (text.charAt(j) == '+') {
                c += temp;
                temp = 0;
            } else if (text.charAt(j) == '-') {
                c += temp;
                temp = 0;
                negative = true;
            } else if (text.charAt(j) == '=') {
                c += temp;
                pre = -1;
                temp = 0;
                negative = false;
            } else {
                c += temp;
                temp = 0;
                negative = false;
            }
        }
        c += temp;
        A = a;
        B = b;
        C = c;
    }

    public String intersect(Line line) //finds the intersection between two lines
    {
        double x, y;
        String point;

        if (line.slope() == slope() && line.yint() != yint())
            return "No Intersect";
        else if (line.equals(this))
            return "Coincident Lines";
        else if (isVertical()) {
            x = xint();
            y = line.slope() * xint() + line.yint();
            return (new Point(x, y).toString());
        } else if (line.isVertical()) {
            x = line.xint();
            y = slope() * line.xint() + yint();
            return (new Point(x, y).toString());
        } else {
            x = (line.yint() - yint()) / (double) (line.slope() - slope());
            y = slope() * x + yint();
            return (new Point(x, y).toString());
        }

    }
}
