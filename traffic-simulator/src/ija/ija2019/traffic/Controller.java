package ija.ija2019.traffic;

import ija.ija2019.traffic.maps.DrawableUpdate;
import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    public AnchorPane getMap() {
        return map;
    }

    @FXML
    private AnchorPane map;
    private List<DrawableUpdate> drawableUpdatesElements = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.now();

    public void addUpdate(DrawableUpdate drawableUpdate) {
        drawableUpdatesElements.add(drawableUpdate);
    }

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

    public void runTime() {
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                for (DrawableUpdate drawableUpdate : drawableUpdatesElements) {
                    drawableUpdate.update(time);
                }
            }
        }, 0, 1000);
    }
}
