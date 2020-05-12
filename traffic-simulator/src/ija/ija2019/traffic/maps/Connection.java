package ija.ija2019.traffic.maps;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Connection implements Drawable {
    private String id;
    private Coordinate position;
    private List<Shape> drawableObjects;

    public Connection(String id, Coordinate position) {
        this.id = id;
        this.position = position;
        drawableObjects = new ArrayList<>();
        drawableObjects.add(new Circle(position.getX(), position.getY(), 5.0, Color.BLUE));
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
}
