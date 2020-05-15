package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import ija.ija2019.traffic.Controller;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//@JsonDeserialize(converter= Connection.ConnectionDeserialize.class)
public class Connection implements Drawable, DrawableUpdate {
    private String id;
    private double speed;
    private Line line;
    private Coordinate position;
    private Coordinate currentDestination;
    private double currentLength;
    private double length;
    private List<Shape> drawableObjects;

    public Connection(){}

    @JsonCreator
    public Connection(@JsonProperty("id") String id, @JsonProperty("speed") double speed, @JsonProperty("line") Line line) {
        this.id = id;
        this.speed = speed;
        this.line = line;
        this.position = line.getPathsIterator().next().getSource().getCoordinate();
        this.currentDestination = line.getPathsIterator().next().getDestination().getCoordinate();
        this.currentLength = line.getPathsIterator().next().getPathLength();
        this.length = 0.0;
        setDrawableObjects();
    }

    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        Circle bus = new Circle(position.getX(), position.getY(), 8.0, Color.BLUE);
        bus.setId(id);
        drawableObjects.add(bus);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    public String getId() {
        return id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCurrentLength(double currentLength) {
        this.currentLength = currentLength;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Coordinate getCurrentDestination() {
        return currentDestination;
    }

    public void setCurrentDestination(Coordinate currentDestination) {
        this.currentDestination = currentDestination;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public void updateGui(Coordinate coordinate) {
        for (Shape shape : drawableObjects) {
            shape.setTranslateX(coordinate.getX() - position.getX() + shape.getTranslateX());
            shape.setTranslateY(coordinate.getY() - position.getY() + shape.getTranslateY());
        }
    }

    @Override
    public List<Shape> getDrawableObjects() {
        return drawableObjects;
    }

    @Override
    public void update(LocalTime time) {
        double speed = currentLength / 20.0;
        length += speed;
        System.out.format("%s - %f - %f\n", currentDestination, currentLength, length);
        if (length > currentLength) {
            updateGui(currentDestination);
            setPosition(currentDestination);
            if(!line.getPathsIterator().hasNext()) {
                return;
            }
            setCurrentDestination(line.getPathsIterator().next().getDestination().getCoordinate());
            setCurrentLength(line.getPathsIterator().next().getPathLength());
            setLength(0.0);
            speed = currentLength / 20.0;
            length += speed;
        }
        Coordinate newPosition = line.calculateNewPosition(position, currentDestination, speed);
        updateGui(newPosition);
        position = newPosition;
    }

    /*static class ConnectionDeserialize extends StdConverter<Connection, Connection> {

        @Override
        public Connection convert(Connection connection) {
            connection.setPosition(connection.getLine().getCoordinates().get(0));
            connection.setCurrentDestination(connection.getLine().getCoordinates().get(1));
            connection.setCurrentLength(connection.calculateTotalLength());
            connection.setLength(0.0d);
            connection.setDrawableObjects();
            return connection;
        }
    }*/
}
