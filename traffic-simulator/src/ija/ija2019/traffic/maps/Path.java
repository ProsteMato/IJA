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
        if (firstStreet.getEnd().equals(secondStreet.getBegin()) ||
                firstStreet.getBegin().equals(secondStreet.getBegin())){
            return secondStreet.getBegin();
        } else if (firstStreet.getBegin().equals(secondStreet.getEnd()) ||
                firstStreet.getEnd().equals(secondStreet.getEnd())) {
            return secondStreet.getEnd();
        }
        return null;
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
