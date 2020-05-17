package ija.ija2019.traffic.maps;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is representing data for application.
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public class Data {
    private List<Street> streets;
    private List<Stop> stops;
    private List<Line> lines;
    private List<Connection> connections;

    public Data(List<Street> streets, List<Stop> stops, List<Line> lines, List<Connection> connections) {
        this.streets = streets;
        this.stops = stops;
        this.lines = lines;
        this.connections = connections;
    }

    /**
     * This function will return connection from list of connection by id
     * @param id id of connection
     * @return connection on success otherwise null
     */
    public Connection getConnectionById(String id){
        for (Connection con : connections) {
            if (con.getId().equals(id)) {
                return con;
            }
        }
        return null;
    }

    /**
     * This function will return street from list of streets by id
     * @param id id of street
     * @return street on success otherwise null
     */
    public Street getStreetById(String id){
        for (Street s : streets){
            if (s.getId().equals(id)){
                return s;
            }
        }
        return null;
    }

    private Data(){}

    /**
     * This method returns lines.
     * @return lines.
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * This method returns connections.
     * @return connections.
     */
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * This method returns streets.
     * @return streets.
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * This method returns stops.
     * @return stops.
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * This method returns all drawable objects.
     * @return all drawable objects.
     */
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
