package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Line implements ILine {
    private String id;
    private Map<String, List<Stop>> path;
    private List<Street> streets;

    public Line(String id) {
        this.id = id;
        streets = new ArrayList<>();
    }

    private Line(){}

    public String getId() {
        return id;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public Map<String, List<Stop>> getPath() {
        return path;
    }

    private void getStopsCoordinates(List<Coordinate> coordinates, Street street) {
        if (this.path.get(street.getId()) != null) {
            for (Stop stop : this.path.get(street.getId())) {
                coordinates.add(stop.getCoordinate());
            }
        }
    }

    private void getStreetPathCoordinate(List<Coordinate> coordinates, Street firstStreet, Street secondStreet ) {
        if (firstStreet.getEnd().equals(secondStreet.getBegin()) ||
            firstStreet.getBegin().equals(secondStreet.getBegin())){
            coordinates.add(secondStreet.getBegin());
        } else if (firstStreet.getBegin().equals(secondStreet.getEnd()) ||
                firstStreet.getEnd().equals(secondStreet.getEnd())) {
            coordinates.add(secondStreet.getEnd());
        }
    }

    public List<Coordinate> getCoordinates() {
        List<Coordinate> path = new ArrayList<>();
        for(int i = 0; i < streets.size(); i++) {
            getStopsCoordinates(path, streets.get(i));
            if (i != streets.size() - 1)
                getStreetPathCoordinate(path, streets.get(i), streets.get(i + 1));
        }
        return path;
    }

    public Coordinate calculateNewPosition(Coordinate source, Coordinate destination, double distance) {
        Coordinate v = destination.subCoordinate(source);
        Coordinate u = v.normalizeCoordinate();
        return source.sumCoordinate(u.timesCoordinate(distance));
    }

    @Override
    public String toString() {
        return "Line{" +
                "id='" + id + '\'' +
                ", path=" + path +
                ", streets=" + streets +
                '}';
    }
}
