package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class represents one connection of line
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public class Connection implements DrawableUpdate {
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
    private List<TimeTable> timetable;
    private ListIterator<Path> pathsIterator;
    private long delay;
    @JsonIgnore
    private IntegerProperty delayProperty;
    private int nextStopIndex;
    @JsonIgnore
    private DoubleProperty currentProgress;
    @JsonIgnore
    private List<ProgressIndicator> indicators;
    @JsonIgnore
    private List<Label> delayLabels;


    private void setCurrentProgress(double value){
        currentProgress.set(value);
    }

    private void updatePathProgress(double pathLength){
        currentProgress.set(length/pathLength);
    }

    /**
     * Method return list of drawn labels providing delay info of the connection
     * @return List of labels
     */
    public List<Label> getDelayLabels() {
        return delayLabels;
    }

    /**
     * Method return index of next stop
     * @return index of next stop
     */
    public int getNextStopIndex() {
        return nextStopIndex;
    }

    /**
     * Method returns currentProgressProperty of the connection for progress indicator binding
     * @return currentProgressProperty
     */
    public DoubleProperty currentProgressProperty() {
        return currentProgress;
    }

    /**
     * Method return list of progress indicators drawn for this connection
     * @return list of progress indicators
     */
    public List<ProgressIndicator> getIndicators() {
        return indicators;
    }

    /**
     * Method returns list of Timetable objects, representing the times when connection should be at certain stops
     * @return list of Timetable objects
     */
    public List<TimeTable> getTimetable() {
        return timetable;
    }

    /**
     * Method returns the time when bus leaves first stop of the line
     * @return time of first stop in LocalTime
     */
    public LocalTime getStartTime() {
        return timetable.get(0).getLocalTime();
    }


    private void bindToNext(){
        indicators.get(nextStopIndex).progressProperty().unbind();
        indicators.get(nextStopIndex).progressProperty().set(1);
        setCurrentProgress(0);
        indicators.get(nextStopIndex + 1).progressProperty().bind(currentProgress);
    }

    /**
     * Empty constructor for jackson yaml parsing
     */
    public Connection(){}

    /**
     * JsonCreator constructor for jackson yaml parsing
     * @param id id of connection
     * @param line line of connection
     */
    @JsonCreator
    public Connection(@JsonProperty("id") String id, @JsonProperty("line") Line line) {
        this.id = id;
        this.line = line;
        setUpObject();
    }

    /**
     * Sets initial drawable objects of the connection
     */
    public void setDrawableObjects() {
        drawableObjects = new ArrayList<>();
        Circle bus = new Circle(position.getX(), position.getY(), 10.0, line.getBusColor());
        bus.setId(id);
        drawableObjects.add(bus);
    }


    /**
     * Method returns line of connection
     * @return line of connection
     */
    public Line getLine() {
        return line;
    }

    /**
     * Method return Id of connection
     * @return String id of connection
     */
    public String getId() {
        return id;
    }

    /**
     * Method returns delayProperty of connection for binding to label displaying the delay
     * @return IntegerProperty representing delay property of connection
     */
    public IntegerProperty delayPropertyProperty() {
        return delayProperty;
    }

    /**
     * Methods moves the drawable objects of connection to coordinate
     * @param coordinate destination coordinate
     */
    public void updateGui(Coordinate coordinate) {
        for (Shape shape : drawableObjects) {
            shape.setTranslateX(coordinate.getX() - position.getX() + shape.getTranslateX());
            shape.setTranslateY(coordinate.getY() - position.getY() + shape.getTranslateY());
        }
    }

    /**
     * Method returns list of drawable objects of connection
     * @return list of drawable objects of connection
     */
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

    /**
     * Method calculates and sets the current delay of connection
     * @param time time for which the delay will be calculated
     */
    public void setDelay(LocalTime time) {
        LocalTime destination = timetable.get(getIndexOfCurrentDestinationInTimeTable()).getLocalTime();
        if (destination.isAfter(time.plusSeconds(10))) {
            this.delay = ChronoUnit.SECONDS.between(destination, LocalTime.parse("23:59:59"));
            this.delay += ChronoUnit.SECONDS.between(LocalTime.MIDNIGHT, time);
        } else {
            this.delay = ChronoUnit.MINUTES.between(destination, time);
        }
        Platform.runLater(new Runnable() {
            @Override public void run() {
                delayProperty.setValue((int)delay);
            }
        });
    }

    /**
     * Initializes the attributes of object
     */
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
        this.delayProperty = new SimpleIntegerProperty(0);
        this.delayLabels = new ArrayList<>();
        setDrawableObjects();
    }

    /**
     * TODO
     * @param time
     * @return true if is affected by time shift otherwise false
     */
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

    /**
     * Method calculates and updates position of connection and its attributes
     * @param time actual current time
     * @return 1 when lifetime of connection has ended, 0 otherwise
     */
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
