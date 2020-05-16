package ija.ija2019.traffic.maps;

import java.time.LocalTime;

public interface DrawableUpdate extends Drawable {
    int update(LocalTime time);
}
