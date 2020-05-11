package ija.ija2019.traffic.maps;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Stop implements IStop {
    private String id;
    private Coordinate coordinate;
    private Street street;

    public Stop(String id, Coordinate coordinate, Street street) {
        this.id = id;
        this.coordinate = coordinate;
        this.street = street;
    }

    private Stop(){}

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public String getId() {
        return this.id;
    }

    public Street getStreet() {
        return this.street;
    }

    public void setStreet(Street s) {
        this.street = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop)) return false;
        Stop myStop = (Stop) o;
        return Objects.equals(id, myStop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "stop(" + id  + ")";
    }
}
