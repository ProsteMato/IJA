package ija.ija2019.traffic.maps;

import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate create(int x, int y) {
        if (x >= 0 && y >= 0) {
            return new Coordinate(x, y);
        }
        return null;
    }

    public int diffX(Coordinate c) {
        return this.x - c.x;
    }

    public int diffY(Coordinate c) {
        return this.y - c.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
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
}
