package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class represents Line which has several stops and path how buses get to them
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter = Line.LineDeserialize.class)
public class Line {
    private String id;
    private String busColor;
    private String stopColor;
    private List<Path> paths;

    private Line(){}

    /**
     * Method returns id of line
     * @return id of line
     */
    public String getId() {
        return id;
    }

    /**
     * Method returns list of stops of line
     * @return list of stops of line
     */
    public List<Stop> getStops(){
        List<Stop> stops = new ArrayList<>();
        for (Path path : paths) {
            stops.add(path.getSource());
        }
        stops.add(paths.get(paths.size() - 1).getDestination());
        return stops;
    }

    /**
     * Method returns list of streets that are part of line path
     * @return list of streets
     */
    public List<Street> getStreets() {
        List<Street> streets = new ArrayList<>();
        for (Path path : paths) {
            for (Street street : path.getStreets()) {
                if (!streets.contains(street))
                    streets.add(street);
            }
        }
        return streets;
    }

    /**
     * Method returns the bus color of this line in Color object
     * @return color of buses
     */
    public Color getBusColor() {
        return Color.web(busColor);
    }

    /**
     * Method returns the stop color of this line in Color object
     * @return color of stops
     */
    public Color getStopColor() {
        return Color.web(stopColor);
    }

    /**
     * Method returns list representing the path of line containing Path objects
     * @return list of Path objects
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     * Calculates new position of connection
     * @param source current point
     * @param destination destination point
     * @param distance this is current distance that connection have to make
     * @return new position
     */
    public Coordinate calculateNewPosition(Coordinate source, Coordinate destination, double distance) {
        Coordinate v = destination.subCoordinate(source);
        Coordinate u = v.normalizeCoordinate();
        return source.sumCoordinate(u.timesCoordinate(distance));
    }

    @Override
    public String toString() {
        return "Line{" +
                "id='" + id + '\'' +
                ", path=" + paths +
                '}';
    }

    static class LineDeserialize extends StdConverter<Line, Line> {

        @Override
        public Line convert(Line line) {
            return line;
        }
    }
}
