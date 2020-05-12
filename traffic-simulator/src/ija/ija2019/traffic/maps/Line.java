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

    public List<Coordinate> getPath() {
        List<Coordinate> path = new ArrayList<>();
        Street lastStreet = null;
        for (int i = 0; i < streets.size(); i++) {
            if (lastStreet != null && !lastStreet.equals(streets.get(i))) {
                path.add(lastStreet.end());
                lastStreet = streets.get(i);
            }
            if (lastStreet == null) {
                path.add(streets.get(i).begin());
                lastStreet = streets.get(i);
            }
            if (i == streets.size() - 1) {
                path.add(streets.get(i).end());
            }
            if (stops.get(i) != null) {
                path.add(stops.get(i).getCoordinate());
            }
        }
        return path;
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
