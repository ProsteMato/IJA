package ija.ija2019.traffic.maps;

import sun.plugin.dom.core.CoreConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Path {
    private Stop source;
    private Stop destination;
    private List<Coordinate> coordinates;

    private Path(){}

    public Path(Stop source, List<Coordinate> coordinates) {
        this.source = source;
        this.coordinates = coordinates;
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

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public List<Coordinate> getPath() {
        List<Coordinate> path = new ArrayList<>();
        path.add(source.getCoordinate());
        if (coordinates != null) {
            path.addAll(coordinates);
        }
        path.add(destination.getCoordinate());
        return path;
    }

    private double calculateLength(Coordinate source, Coordinate destination) {
        return Math.sqrt(Math.pow(source.diffX(destination), 2) + Math.pow(source.diffY(destination), 2));
    }

    public double getPathLength() {
        double length = 0.0;
        if (coordinates == null){
            length += calculateLength(this.source.getCoordinate(), this.destination.getCoordinate());
        } else {
            length += calculateLength(this.source.getCoordinate(), this.coordinates.get(0));
            for (int i = 0; i < coordinates.size() - 1; i++) {
                length += calculateLength(this.coordinates.get(i), this.coordinates.get(i+1));
            }
            length += calculateLength(coordinates.get(coordinates.size() - 1), this.destination.getCoordinate());
        }
        return length;


    }

}
