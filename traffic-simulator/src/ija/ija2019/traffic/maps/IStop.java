package ija.ija2019.traffic.maps;

public interface IStop {
    public Coordinate getCoordinate();

    public String getId();

    public Street getStreet();

    public void setStreet(Street s);
}
