package ija.ija2019.traffic.maps;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents path that is from source Stop to destination Stop.
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
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

    /**
     * This method returns if path from source to destination is on one street.
     * @return boolean
     */
    public boolean isSameLine() {
        return sameLine;
    }

    /**
     * This method is setting if path from source to destination is on one street.
     * @param sameLine boolean if the path is on one street.
     */
    public void setSameLine(boolean sameLine) {
        this.sameLine = sameLine;
    }

    /**
     * This method return starting Stop of path.
     * @return starting Stop of path.
     */
    public Stop getSource() {
        return source;
    }

    /**
     * This method set starting Stop of path.
     * @param source Stop.
     */
    public void setSource(Stop source) {
        this.source = source;
    }

    /**
     * This method return ending Stop of path.
     * @return ending Stop of path.
     */
    public Stop getDestination() {
        return destination;
    }

    /**
     * This method sets ending Stop of path.
     * @param destination Stop.
     */
    public void setDestination(Stop destination) {
        this.destination = destination;
    }

    /**
     * This method returns Streets between source and end of the path.
     * @return List of Streets.
     */
    public List<Street> getCoordinates() {
        return coordinates;
    }

    /**
     * This method sets Streets that are between source and end of the path.
     * @param coordinates List of Streets that are between source and end.
     */
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

    /**
     * This method return all streets that are in path.
     * @return List of Streets that are in path.
     */
    public List<Street> getStreets() {
        List<Street> streets = new ArrayList<>();
        streets.add(source.getStreet());
        if (coordinates != null) {
            streets.addAll(coordinates);
        }
        streets.add(destination.getStreet());
        return streets;
    }

    /**
     * This method is for getting traffic from streets that position is on.
     * @param position Current destination of out connection.
     * @return traffic of street that position is on if not a good position is set it will return one.
     */
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
        return 1.0;
    }

    /**
     * This method is creating path of coordinates from source to destination.
     * @return list of coordinates from source to destination.
     */
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

    /**
     * This method calculates path length
     * @return path length
     */
    public double getPathLength() {
        double length = 0.0;
        List<Coordinate> coordinates = getPath();
        for (int i = 0; i < coordinates.size() - 1; i++) {
            length += calculateLength(coordinates.get(i), coordinates.get(i+1));
        }
        return length;
    }



}
