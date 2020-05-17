package ija.ija2019.traffic.maps;

import java.time.LocalTime;

/**
 * This interface implements updating of objects in time.
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public interface DrawableUpdate extends Drawable {
    /**
     * This method is updating all DrawableUpdate objects.
     * @param time actual time
     * @return return code 0 if ok 1 if we can delete object.
     */
    int update(LocalTime time);
}
