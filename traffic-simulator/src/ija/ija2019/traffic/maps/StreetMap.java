package ija.ija2019.traffic.maps;

import java.util.HashMap;
import java.util.Map;

public class StreetMap implements IStreetMap {
    private Map<String, Street> streets;

    public StreetMap(){
        this.streets = new HashMap<>();
    }

    public void addStreet(Street s) {
        streets.put(s.getId(), s);
    }

    public Street getStreet(String id) {
        return streets.getOrDefault(id, null);
    }
}
