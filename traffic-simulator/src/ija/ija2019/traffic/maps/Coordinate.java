package ija.ija2019.traffic.maps;

import java.util.Objects;

public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private Coordinate(){}

    public static Coordinate create(double x, double y) {
        if (x >= 0 && y >= 0) {
            return new Coordinate(x, y);
        }
        return null;
    }

    public double diffX(Coordinate c) {
        return this.x - c.x;
    }

    public double diffY(Coordinate c) {
        return this.y - c.y;
    }

    public Coordinate subCoordinate(Coordinate c) {
        return new Coordinate(this.diffX(c), this.diffY(c));
    }

    public Coordinate sumCoordinate(Coordinate c) {
        return new Coordinate(this.x + c.getX(), this.y + c.getY());
    }

    public Coordinate normalizeCoordinate() {
        return new Coordinate(x / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), y / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    public Coordinate timesCoordinate(double number) {
        return new Coordinate(x * number, y * number);
    }

    public double getX() {
        return this.x;
    }

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
