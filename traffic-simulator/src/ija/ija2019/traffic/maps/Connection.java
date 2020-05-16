package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class Connection implements Drawable, DrawableUpdate {
    private String id;
    private double speed;
    private Line line;
    private Coordinate position;
    private Coordinate currentDestination;
    private Path currentPath;
    private ListIterator<Coordinate> coordinateListIterator;
    private double currentTotalLength;
    private double length;
    private List<Shape> drawableObjects;
    private List<Timetable> timetable;
    private ListIterator<Path> pathsIterator;
    @JsonIgnore
    private int nextStopIndex;
    @JsonIgnore
    public DoubleProperty currentProgress;
    @JsonIgnore
    public List<ProgressIndicator> indicators;

    private void setCurrentProgress(double value){
        currentProgress.set(value);
    }

    private void updatePathProgress(double pathLength){
        currentProgress.set(length/pathLength);
    }

    public int getNextStopIndex() {
        return nextStopIndex;
    }

    public double getCurrentProgress() {
        return currentProgress.get();
    }

    public DoubleProperty currentProgressProperty() {
        return currentProgress;
    }

    public List<ProgressIndicator> getIndicators() {
        return indicators;
    }

    public List<Timetable> getTimetable() {
        return timetable;
    }

    public LocalTime getStartTime() {
        return timetable.get(0).getLocalTime();
    }

    public void setTimetable(List<Timetable> timetable) {
        this.timetable = timetable;
    }

    private void bindToNext(){
        indicators.get(nextStopIndex).progressProperty().unbind();
        indicators.get(nextStopIndex).progressProperty().set(1);
        setCurrentProgress(0);
        indicators.get(nextStopIndex + 1).progressProperty().bind(currentProgress);
    }

    public Connection(){}

    @JsonCreator
    public Connection(@JsonProperty("id") String id, @JsonProperty("line") Line line) {
        this.id = id;
        this.line = line;
        this.pathsIterator = line.getPaths().listIterator();
        this.currentPath = this.pathsIterator.next();
        this.coordinateListIterator = currentPath.getPath().listIterator();
        this.position = coordinateListIterator.next();
        this.currentDestination = coordinateListIterator.next();
        this.currentTotalLength = currentLength(position, currentDestination);
        this.length = 0.0;
        this.nextStopIndex = 1;
        this.currentProgress = new SimpleDoubleProperty(0);
        this.indicators = new ArrayList<>();
        setDrawableObjects();
    }

    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        Circle bus = new Circle(position.getX(), position.getY(), 10.0, Color.BLUEVIOLET);
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

    public Coordinate getCurrentDestination() {
        return currentDestination;
    }

    public void setCurrentDestination(Coordinate currentDestination) {
        this.currentDestination = currentDestination;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }

    public ListIterator<Coordinate> getCoordinateListIterator() {
        return coordinateListIterator;
    }

    public void setCoordinateListIterator(ListIterator<Coordinate> coordinateListIterator) {
        this.coordinateListIterator = coordinateListIterator;
    }

    public double getCurrentTotalLength() {
        return currentTotalLength;
    }

    public void setCurrentTotalLength(double currentTotalLength) {
        this.currentTotalLength = currentTotalLength;
    }

    public double getLength() {
        return length;
    }

    public void setDrawableObjects(List<Shape> drawableObjects) {
        this.drawableObjects = drawableObjects;
    }

    public void setLength(double length) {
        this.length = length;
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

    private double currentLength(Coordinate source, Coordinate destination) {
        return Math.sqrt(Math.pow(source.diffX(destination), 2) + Math.pow(source.diffY(destination), 2));
    }

    @Override
    public int update(LocalTime time) {
        double pathLength = currentPath.getPathLength();
        double speed = pathLength / 20.0;
        speed = speed * currentPath.getTraffic(currentDestination);
        length += speed;
        if (length > pathLength) {
            updateGui(currentDestination);
            if (!pathsIterator.hasNext()) {
                updatePathProgress(pathLength);
                return 1;
            }
            currentPath = pathsIterator.next();
            coordinateListIterator = currentPath.getPath().listIterator();
            position = coordinateListIterator.next();
            currentDestination = coordinateListIterator.next();
            length = 0.0;
            currentTotalLength = currentLength(position, currentDestination);
            pathLength = currentPath.getPathLength();
            speed = pathLength / 20.0;
            speed = speed * currentPath.getTraffic(currentDestination);
            length += speed;
            if (indicators.size() > 0){
                bindToNext();
            }
            nextStopIndex += 1;
        }
        if (length > currentTotalLength) {
            speed = length - currentTotalLength;
            updateGui(currentDestination);
            position = currentDestination;
            currentDestination = coordinateListIterator.next();
            currentTotalLength = currentLength(position, currentDestination) + length;
        }
        Coordinate newPosition = line.calculateNewPosition(position, currentDestination, speed);
        updateGui(newPosition);
        position = newPosition;
        updatePathProgress(pathLength);
        return 0;
    }
}
