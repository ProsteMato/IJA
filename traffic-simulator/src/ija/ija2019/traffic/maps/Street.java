package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Street implements IStreet, Drawable {
    private String id;
    private Coordinate begin;
    private Coordinate end;
    private List<Stop> stops;
    private double traffic;
    private boolean isOpen;
    private List<Shape> drawableObjects = new ArrayList<>();

    public Street(String id, Coordinate begin, Coordinate end) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        stops = new ArrayList<>();
    }

    public void setDrawableObjects() {
        drawableObjects.add(new Line(begin.getX(), begin.getY(), end.getX(), end.getY()));
        //drawableObjects.add(new Text((begin.getX() + end.getX())/2, (begin.getY() + end.getY())/2, id));
    }

    private Street(){}

    public Coordinate getBegin() {
        return this.begin;
    }

    public Coordinate getEnd() {
        return this.end;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    public double getTraffic() {
        return traffic;
    }

    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean addStop(Stop stop) {
        Coordinate stopCoordinate = stop.getCoordinate();
        if (distance(begin, stopCoordinate) + distance(stopCoordinate, end) == distance(begin, end)) {
            stop.setStreet(this);
            stops.add(stop);
            return true;
        }
        return false;
    }

    public double distance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.diffX(c2), 2) + Math.pow(c1.diffY(c2), 2));
    }

    public Coordinate begin() {
        return begin;
    }

    public Coordinate end() {
        return end;
    }

    public boolean follows(Street s) {
        return (s.end().getX() == this.begin().getX() && s.end().getY() == this.begin().getY()) ||
                (s.begin().getX() == this.end().getX() && s.begin().getY() == this.end().getY());

    }

    public List<Coordinate> getCoordinates() {
        return Arrays.asList(begin, end);
    }

    public String getId() {
        return this.id;
    }

    public List<Stop> getStops() {
        return this.stops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Street)) return false;
        Street myStreet = (Street) o;
        return id.equals(myStreet.id) &&
                begin.equals(myStreet.begin) &&
                stops.equals(myStreet.stops) &&
                end.equals(myStreet.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, begin, end, stops);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public List<Shape> getDrawableObjects() {
        return drawableObjects;
    }
}
