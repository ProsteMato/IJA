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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonDeserialize(converter = Line.LineDeserialize.class)
public class Line implements ILine {
    private String id;
    private String busColor;
    private String stopColor;
    private List<Path> paths;

    public Line(String id, List<Path> paths) {
        this.id = id;
        this.paths = paths;
    }

    private Line(){}

    public String getId() {
        return id;
    }

    public List<Stop> getStops(){
        List<Stop> stops = new ArrayList<>();
        for (Path path : paths) {
            stops.add(path.getSource());
        }
        stops.add(paths.get(paths.size() - 1).getDestination());
        return stops;
    }

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

    public Color getBusColor() {
        return Color.web(busColor);
    }

    public void setBusColor(String color) {
        this.busColor = color;
    }

    public Color getStopColor() {
        return Color.web(stopColor);
    }

    public void setStopColor(String stopColor) {
        this.stopColor = stopColor;
    }

    public List<Path> getPaths() {
        return paths;
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
