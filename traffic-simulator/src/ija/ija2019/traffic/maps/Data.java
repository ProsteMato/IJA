package ija.ija2019.traffic.maps;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Street> streets;
    private List<Stop> stops;
    private List<Line> lines;
    private List<Connection> connections;

    public Data(List<Street> streets, List<Stop> stops) {
        this.streets = streets;
        this.stops = stops;
    }

    public Connection getConnectionById(String id){
        for (Connection con : connections) {
            if (con.getId().equals(id)) {
                return con;
            }
        }
        return null;
    }

    public Street getStreetById(String id){
        for (Street s : streets){
            if (s.getId().equals(id)){
                return s;
            }
        }
        return null;
    }

    private Data(){}

    public List<Line> getLines() {
        return lines;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Drawable> getDrawables() {
        List<Drawable> drawables = new ArrayList<>(streets);
        drawables.addAll(stops);
        drawables.addAll(connections);
        return drawables;
    }

    @Override
    public String toString() {
        return "Data{" +
                "streets=" + streets +
                ", stops=" + stops +
                '}';
    }
}
