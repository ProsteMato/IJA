package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is representation of stop.
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter= Stop.StopDeserialize.class)
public class Stop implements Drawable{
    private String id;
    private Coordinate coordinate;
    private Street street;
    private List<Shape> drawableObjects;

    public Stop(String id, Coordinate coordinate, Street street) {
        this.id = id;
        this.coordinate = coordinate;
        this.street = street;
        setDrawableObjects();
    }

    private Stop(){}

    /**
     * This function is setting drawable objects
     */
    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        drawableObjects.add(new Circle(coordinate.getX(), coordinate.getY(), 6, Color.RED));
    }

    /**
     * This method is adding stop to street.
     */
    public void addStreetStop() {
        street.addStop(this);
    }

    /**
     * This method is returning coordinates of stop
     * @return coordinates of stop
     */
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    /**
     * This method is returning id of stop
     * @return id of stop
     */
    public String getId() {
        return this.id;
    }

    /**
     * This method is returning street of stop
     * @return street of stop
     */
    public Street getStreet() {
        return this.street;
    }

    /**
     * This method is setting street of stop
     * @param s street on which is stop
     */
    public void setStreet(Street s) {
        this.street = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;
        Stop myStop = (Stop) o;
        return Objects.equals(id, myStop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id='" + id + '\'' +
                ", coordinate=" + coordinate +
                '}';
    }

    @Override
    public List<Shape> getDrawableObjects() {
        return drawableObjects;
    }

    static class StopDeserialize extends StdConverter<Stop, Stop> {

        @Override
        public Stop convert(Stop stop) {
            stop.setDrawableObjects();
            stop.addStreetStop();
            return stop;
        }
    }
}
