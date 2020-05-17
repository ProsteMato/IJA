package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.SimpleTimeZone;


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
    private long delay;
    private IntegerProperty delayProperty = new SimpleIntegerProperty(0);
    private int nextStopIndex;
    private DoubleProperty currentProgress;
    private List<ProgressIndicator> indicators;

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
        setUpObject();
    }

    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        Circle bus = new Circle(position.getX(), position.getY(), 10.0, line.getBusColor());
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

    public int getDelayProperty() {
        return delayProperty.get();
    }

    public IntegerProperty delayPropertyProperty() {
        return delayProperty;
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

    private long getTimeForDestination() {
        long duration = 0;
        for (int i = 1; i < timetable.size(); i++) {
            if(timetable.get(i).getStop().equals(currentPath.getDestination())) {
                LocalTime source = timetable.get(i-1).getLocalTime();
                LocalTime destination = timetable.get(i).getLocalTime();
                LocalTime beforeMidNight = LocalTime.parse("23:59:59");
                LocalTime midNight = LocalTime.parse("00:00");
                if (source.isAfter(destination)) {
                    duration += ChronoUnit.SECONDS.between(source, beforeMidNight);
                    duration += ChronoUnit.SECONDS.between(midNight, destination);
                } else {
                    duration = ChronoUnit.SECONDS.between(source, destination);
                }
            }
        }
        return duration;
    }

    private int getIndexOfCurrentDestinationInTimeTable() {
        for (int index = 1; index < timetable.size(); index++) {
            if(timetable.get(index).getStop().equals(currentPath.getDestination())) {
                return index;
            }
        }
        return -1;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(LocalTime time) {
        LocalTime destination = timetable.get(getIndexOfCurrentDestinationInTimeTable()).getLocalTime();
        if (destination.isAfter(time.plusSeconds(10))) {
            this.delay = ChronoUnit.SECONDS.between(destination, LocalTime.parse("23:59:59"));
            this.delay += ChronoUnit.SECONDS.between(LocalTime.MIDNIGHT, time);
        } else {
            this.delay = ChronoUnit.MINUTES.between(destination, time);
        }
        delayProperty.setValue((int)delay);
    }

    public void setUpObject() {
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
        this.speed = 0.0;
        this.delay = 0;
        setDrawableObjects();
    }

    public boolean isAffectedByTimeShift(LocalTime time) {
        for (int i = 1; i < timetable.size(); i++) {
            LocalTime source = timetable.get(i-1).getLocalTime();
            LocalTime destination = timetable.get(i).getLocalTime();
            if (time.isAfter(source) && time.isBefore(destination)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int update(LocalTime time) {
        double pathLength = currentPath.getPathLength();
        speed = pathLength / getTimeForDestination();
        speed = speed * currentPath.getTraffic(currentDestination);
        length += speed;
        if (length > pathLength) {
            updateGui(currentDestination);
            setDelay(time);
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
            speed = pathLength / getTimeForDestination();
            speed = speed * currentPath.getTraffic(currentDestination);
            length += speed;
            if (indicators.size() > 0){
                bindToNext();
            }
            nextStopIndex += 1;
        }
        if (length > currentTotalLength && coordinateListIterator.hasNext()) {
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
