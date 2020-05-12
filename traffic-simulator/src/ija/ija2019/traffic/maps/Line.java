package ija.ija2019.traffic.maps;

import java.util.ArrayList;
import java.util.List;

public class Line implements ILine {
    private String id;
    private List<Stop> stops;
    private List<Street> streets;

    public Line(String id) {
        this.id = id;
        stops = new ArrayList<>();
        streets = new ArrayList<>();
    }

    private Line(){}

    public String getId() {
        return id;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public boolean addStop(Stop stop) {
        if ((stops.isEmpty() && streets.isEmpty()) || stop.getStreet().follows(streets.get(streets.size() - 1))) {
            stops.add(stop);
            streets.add(stop.getStreet());
            return true;
        }
        return false;
    }

    public boolean addStreet(Street street) {
        if (street.getStops().isEmpty() && !stops.isEmpty()) {
            streets.add(street);
            stops.add(null);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id='" + id + '\'' +
                ", stops=" + stops +
                ", streets=" + streets +
                '}';
    }
}
