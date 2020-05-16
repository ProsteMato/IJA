package ija.ija2019.traffic.maps;

import java.util.List;

public interface IStreet {
    public void addStop(Stop stop);

    public Coordinate begin();

    public Coordinate end();

    public boolean follows(Street s);

    public List<Coordinate> getCoordinates();

    public String getId();

    public List<Stop> getStops();

    public double getTraffic();

    public void setTraffic(double traffic);

    public boolean isOpen();

    public void setOpen(boolean open);
}
