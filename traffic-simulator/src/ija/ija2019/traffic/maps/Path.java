package ija.ija2019.traffic.maps;

import sun.plugin.dom.core.CoreConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Path {
    private Stop source;
    private Stop destination;
    private boolean sameLine;
    private List<Street> coordinates;

    private Path(){}

    public Path(Stop source, Stop destination, boolean sameLine, List<Street> coordinates) {
        this.source = source;
        this.coordinates = coordinates;
        this.sameLine = sameLine;
        this.destination = destination;
    }

    public boolean isSameLine() {
        return sameLine;
    }

    public void setSameLine(boolean sameLine) {
        this.sameLine = sameLine;
    }

    public Stop getSource() {
        return source;
    }

    public void setSource(Stop source) {
        this.source = source;
    }

    public Stop getDestination() {
        return destination;
    }

    public void setDestination(Stop destination) {
        this.destination = destination;
    }

    public List<Street> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Street> coordinates) {
        this.coordinates = coordinates;
    }

    private Coordinate commonCoordinate(Street firstStreet, Street secondStreet) {
        if (secondStreet.getEnd().equals(firstStreet.getBegin()) ||
                secondStreet.getBegin().equals(firstStreet.getBegin())){
            return firstStreet.getBegin();
        } else if (secondStreet.getBegin().equals(firstStreet.getEnd()) ||
                secondStreet.getEnd().equals(firstStreet.getEnd())) {
            return firstStreet.getEnd();
        }
        return null;
    }

    public List<Street> getStreets() {
        List<Street> streets = new ArrayList<>();
        streets.add(source.getStreet());
        if (coordinates != null) {
            streets.addAll(coordinates);
        }
        streets.add(destination.getStreet());
        return streets;
    }

    public boolean isAffectedToTrafficChange(Street street, Coordinate currentDestination) {
        for(Street s : getStreets()) {
            if (s.equals(street) && isOnStreet(currentDestination)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnStreet(Coordinate position) {
        List<Street> streets = getStreets();
        if (destination.getCoordinate().equals(position)) {
            return true;
        }
        for(Street street : streets) {
            if (street.positionOnStreet(position)) {
                return true;
            }
        }
        return false;
    }

    public double getTraffic(Coordinate position) {
        List<Street> streets = getStreets();
        if (destination.getCoordinate().equals(position)) {
            return destination.getStreet().getTraffic();
        }
        for(Street street : streets) {
            if (street.positionOnStreet(position)) {
                return street.getTraffic();
            }
        }
        return 0.6;
    }

    public List<Coordinate> getPath() {
        List<Coordinate> path = new ArrayList<>();
        path.add(source.getCoordinate());
        if (coordinates != null){
            path.add(commonCoordinate(source.getStreet(), coordinates.get(0)));
            for (int i = 0; i < coordinates.size() - 1; i++) {
                path.add(commonCoordinate(coordinates.get(i), coordinates.get(i+1)));
            }
            path.add(commonCoordinate(coordinates.get(coordinates.size() - 1), destination.getStreet()));
        } else if (!sameLine) {
            path.add(commonCoordinate(source.getStreet(), destination.getStreet()));
        }
        path.add(destination.getCoordinate());
        return path;
    }

    private double calculateLength(Coordinate source, Coordinate destination) {
        return Math.sqrt(Math.pow(source.diffX(destination), 2) + Math.pow(source.diffY(destination), 2));
    }

    public double getPathLength() {
        double length = 0.0;
        List<Coordinate> coordinates = getPath();
        for (int i = 0; i < coordinates.size() - 1; i++) {
            length += calculateLength(coordinates.get(i), coordinates.get(i+1));
        }
        return length;
    }



}
