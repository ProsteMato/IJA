package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter = Street.StreetDeserialize.class)
public class Street implements IStreet, Drawable {
    private String id;
    private Coordinate begin;
    private Coordinate end;
    private List<Stop> stops = new ArrayList<>();
    private double traffic;
    private boolean isOpen;
    private List<Shape> drawableObjects = new ArrayList<>();
    private List<Node> drawnInfoObjects = new ArrayList<>();
    public boolean isSelected = false;

    public Street(String id, Coordinate begin, Coordinate end) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        stops = new ArrayList<>();
        setDrawableObjects();
    }

    public void addInfoObject(Node node){
        drawnInfoObjects.add(node);
    }

    public List<Node> getDrawnInfoObjects(){
        return drawnInfoObjects;
    }

    public void setDrawableObjects() {
        Line street = new Line(begin.getX(), begin.getY(), end.getX(), end.getY());
        street.setStrokeWidth(5);
        street.setId(id);
        drawableObjects.add(street);
        //drawableObjects.add(new Text((begin.getX() + end.getX())/2, (begin.getY() + end.getY())/2, id));
    }

    private Street(){}

    public Coordinate getBegin() {
        return this.begin;
    }

    public boolean positionOnStreet(Coordinate position) {
        return position.equals(begin) || position.equals(end);
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

    public void addStop(Stop stop) {
        stops.add(stop);
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

    static class StreetDeserialize extends StdConverter<Street, Street> {

        @Override
        public Street convert(Street street) {
            street.setDrawableObjects();
            return street;
        }
    }
}
