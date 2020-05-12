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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter= Connection.ConnectionDeserialize.class)
public class Connection implements Drawable {
    private String id;
    private Coordinate position;
    private double speed;
    private Line line;

    private List<Shape> drawableObjects;

    private Connection(){}

    public Connection(String id, Coordinate position) {
        this.id = id;
        this.position = position;
    }

    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        drawableObjects.add(new Circle(position.getX(), position.getY(), 5.0, Color.BLUE));
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

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    @Override
    public List<Shape> getDrawableObjects() {
        return drawableObjects;
    }

    static class ConnectionDeserialize extends StdConverter<Connection, Connection> {

        @Override
        public Connection convert(Connection connection) {
            connection.setDrawableObjects();
            return connection;
        }
    }
}
