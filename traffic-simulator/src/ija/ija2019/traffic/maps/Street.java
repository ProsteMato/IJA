package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.*;

/**
 * This class is representing one street
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter = Street.StreetDeserialize.class)
public class Street implements Drawable {
    private String id;
    private Coordinate begin;
    private Coordinate end;
    private List<Stop> stops;
    private double traffic;
    private boolean isOpen;
    private List<Shape> drawableObjects;
    private List<Node> drawnInfoObjects;
    private boolean isSelected = false;

    public Street(String id, Coordinate begin, Coordinate end, List<Stop> stops, double traffic, boolean isOpen) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.stops = stops;
        this.traffic = traffic;
        this.isOpen = isOpen;
        setDrawableObjects();
    }

    /**
     * This method is for checking if street was selected or not.
     * @return true if selected or false if not selected.
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * This method is for setting if streets was selected
     * @param selected true if selected false otherwise
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    //TODO: dokumentacia
    public void addInfoObject(Node node){
        drawnInfoObjects.add(node);
    }

    //TODO: dokumentacia
    public List<Node> getDrawnInfoObjects(){
        return drawnInfoObjects;
    }

    /**
     * This method is setup for drawing streets object.
     */
    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        drawnInfoObjects = new ArrayList<>();
        stops = new ArrayList<>();
        Line street = new Line(begin.getX(), begin.getY(), end.getX(), end.getY());
        street.setStrokeWidth(5);
        street.setId(id);
        drawableObjects.add(street);
}

    private Street(){}

    /**
     * This method is returning begin coordinate of street.
     * @return begin coordinate
     */
    public Coordinate getBegin() {
        return this.begin;
    }

    /**
     * This method checks if position is begin or end coordinate
     * @param position position
     * @return true if position is begin or end, false otherwise
     */
    public boolean positionOnStreet(Coordinate position) {
        return position.equals(begin) || position.equals(end);
    }

    /**
     * This function returns end of street
     * @return Coordinate - end of street
     */
    public Coordinate getEnd() {
        return this.end;
    }

    /**
     * This function check if street is open.
     * @return true if street is open, false otherwise.
     */
    public boolean getIsOpen() {
        return this.isOpen;
    }
    /**
     * This function sets if street is open or not
     * @param open true if street is open, false otherwise.
     */
    public void setIsOpen(boolean open) {
        this.isOpen = open;
    }

    /**
     * This method returns traffic of street.
     * @return street traffic
     */
    public double getTraffic() {
        return traffic;
    }

    /**
     * This method is setting street traffic
     * @param traffic value of traffic from 0.2 to 1.0
     */
    public void setTraffic(double traffic) {
        if (traffic >= 0.2 || traffic <= 1.0)
            this.traffic = traffic;
    }

    /**
     * This method adds stops to street
     * @param stop stop on street
     */
    public void addStop(Stop stop) {
        if(!stops.contains(stop)) {
            stops.add(stop);
        }
    }

    /**
     * This method returns id of street
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * This method returns stops of street
     * @return stops of street
     */
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

    /**
     * This function return drawable objects
     * @return drawable objects
     */
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
