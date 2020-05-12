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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter= Stop.StopDeserialize.class)
public class Stop implements IStop, Drawable{
    private String id;
    private Coordinate coordinate;
    private Street street;
    private List<Shape> drawableObjects = new ArrayList<>();

    public Stop(String id, Coordinate coordinate, Street street) {
        this.id = id;
        this.coordinate = coordinate;
        this.street = street;
        setDrawableObjects();
    }

    private Stop(){}

    public void setDrawableObjects() {
        drawableObjects.add(new Circle(coordinate.getX(), coordinate.getY(), 4, Color.RED));
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public String getId() {
        return this.id;
    }

    public Street getStreet() {
        return this.street;
    }

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
        return "stop(" + id  + ")";
    }

    @Override
    public List<Shape> getDrawableObjects() {
        return drawableObjects;
    }

    static class StopDeserialize extends StdConverter<Stop, Stop> {

        @Override
        public Stop convert(Stop stop) {
            stop.setDrawableObjects();
            return stop;
        }
    }
}
