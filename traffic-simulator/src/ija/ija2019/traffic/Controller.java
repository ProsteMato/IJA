package ija.ija2019.traffic;

import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;

import java.util.List;

public class Controller {
    public AnchorPane getMap() {
        return map;
    }

    @FXML
    private AnchorPane map;

    @FXML
    private void changeZoom(ScrollEvent event){
        event.consume();
        double scroll = event.getDeltaY();
        double zoomScale;
        if (scroll > 0){
            zoomScale = 1.1;
        } else {
            zoomScale = 0.9;
        }
        map.setScaleX(zoomScale * map.getScaleX());
        map.setScaleY(zoomScale * map.getScaleY());
    }

    public void draw(List<Shape> shapes){
        for (Shape s : shapes)
        map.getChildren().addAll(s);
    }
}
