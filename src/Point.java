/**
 * Created by Nicholas on 2016-01-31.
 */

class Point {
    private double X, Y; //x and y value

    //Default constructor
    public Point() {
        X = 0;
        Y = 0;
    }

    //Constructor with x and y
    public Point(double x, double y) {
        X = x;
        Y = y;
    }

    public String toString() //outputs x and y
    {
        return "(" + (Math.round(X * 100.0) / 100.0) + ", " + (Math.round(Y * 100.0) / 100.0) + ")";
    }

}
