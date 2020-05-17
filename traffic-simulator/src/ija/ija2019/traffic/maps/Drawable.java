package ija.ija2019.traffic.maps;

import javafx.scene.shape.Shape;

import java.util.List;

/**
 * This interface is representing all objects that are Drawable on Scene
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public interface Drawable {
    /**
     * This method is returning all drawable objects that can be rendered
     * @return drawable objects.
     */
    List<Shape> getDrawableObjects();
}
