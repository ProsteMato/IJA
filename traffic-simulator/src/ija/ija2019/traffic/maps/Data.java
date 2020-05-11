package ija.ija2019.traffic.maps;

import java.util.List;

public class Data {
    private List<Street> streets;
    private List<Stop> stops;

    public Data(List<Street> streets, List<Stop> stops) {
        this.streets = streets;
        this.stops = stops;
    }

    private Data(){}

    public List<Street> getStreets() {
        return streets;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "Data{" +
                "streets=" + streets +
                ", stops=" + stops +
                '}';
    }
}
