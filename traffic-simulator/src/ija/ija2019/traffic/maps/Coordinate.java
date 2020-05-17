package ija.ija2019.traffic.maps;

import java.util.Objects;

/**
 * This class implements Coordinates and operation on them.
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private Coordinate(){}

    /**
     * This method returns diff of two coordinates x value.
     * @param c other coordinate
     * @return difference of x values
     */
    public double diffX(Coordinate c) {
        return this.x - c.x;
    }

    /**
     * This method returns diff of two coordinates y value.
     * @param c other coordinate
     * @return difference of y values
     */
    public double diffY(Coordinate c) {
        return this.y - c.y;
    }

    /**
     * This method calculates sub of two coordinates
     * @param c other coordinate
     * @return new coordinate that is sub of given coordinates
     */
    public Coordinate subCoordinate(Coordinate c) {
        return new Coordinate(this.diffX(c), this.diffY(c));
    }

    /**
     * This method calculates sum of two coordinates
     * @param c other coordinate
     * @return new coordinate that is sum of given coordinates
     */
    public Coordinate sumCoordinate(Coordinate c) {
        return new Coordinate(this.x + c.getX(), this.y + c.getY());
    }

    /**
     * This method normalize coordinate
     * @return new coordinate that is normalized
     */
    public Coordinate normalizeCoordinate() {
        return new Coordinate(x / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), y / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    /**
     * This method calculate times of number with coordinate
     * @param number multiplier
     * @return new coordinate that is multiplied with number
     */
    public Coordinate timesCoordinate(double number) {
        return new Coordinate(x * number, y * number);
    }

    /**
     * This method returns x coordinate
     * @return x coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * This method returns y coordinate
     * @return y coordinate
     */
    public double getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
