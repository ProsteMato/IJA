package ija.ija2019.traffic;

import ija.ija2019.traffic.maps.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.*;

public class Controller {
    public AnchorPane getMap() {
        return map;
    }
    private Data data;
    private long timeSpeed = 1;
    @FXML
    private AnchorPane infoPanel;
    @FXML
    private AnchorPane connectionListPanel;
    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private AnchorPane map;
    private List<Connection> drawableUpdatesElements = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.now();
    private List<Node> infoPanelObjects = new ArrayList<Node>();

    Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeLabel.setText(time.toString().substring(0,8));
        }
    };
    public void addUpdate(Connection drawableUpdate) {
        drawableUpdatesElements.add(drawableUpdate);
    }

    public void setLabelText(String labelText) {
        this.infoLabel.setText(labelText);
    }

    public void setupSpeedSlider() {
        speedLabel.textProperty().bind(
                Bindings.format("%.0fx", speedSlider.valueProperty())
        );
        speedSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, this::changeSpeed);
    }

    private void changeSpeed(MouseEvent me){
        me.consume();
        timeSpeed = (long) speedSlider.getValue();
        timer.cancel();
        runTime();
    }

    public void giveData(Data data){
        this.data = data;
    }

    public void showConnectionInfo(MouseEvent me) {
        me.consume();
        Circle bus = (Circle) me.getSource();
        // finding the connection
        for (Connection con : data.getConnections()) {
            if (con.getId().equals(bus.getId())) {
                double yOffset = 5;
                infoPanel.setVisible(true);
                infoLabel.setText(con.getId());
                Line line = con.getLine();
                List<Stop> stops = line.getStops();
                for (Stop s : stops){
                    Label label = new Label(s.getId());
                    label.setLayoutX(39);
                    label.setLayoutY(yOffset);
                    ProgressIndicator progressIndicator = new ProgressIndicator(0.3);
                    progressIndicator.setLayoutX(8);
                    progressIndicator.setLayoutY(yOffset);
                    infoPanelObjects.add(new Group(progressIndicator, label));
                    yOffset += 30;
                }
            }
        }
        for (Node node : infoPanelObjects){
            connectionListPanel.getChildren().add(node);
        }
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
                for (Connection drawableUpdate : drawableUpdatesElements) {
                    if (drawableUpdate.update(time) == 1) {
                        Platform.runLater(() -> map.getChildren().removeAll(drawableUpdate.getDrawableObjects()));
                        Platform.runLater(() -> drawableUpdatesElements.remove(drawableUpdate));
                    }
                }
                // timer update
                Platform.runLater(updateTimer);
            }
        }, 0, 1000 / timeSpeed);
    }
}
