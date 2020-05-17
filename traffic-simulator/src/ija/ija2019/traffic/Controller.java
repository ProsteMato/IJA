package ija.ija2019.traffic;

import ija.ija2019.traffic.maps.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.converter.DateTimeStringConverter;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

public class Controller {
    public AnchorPane getMap() {
        return map;
    }
    private Data data;
    private long timeSpeed = 1;
    private boolean firstStart = false;
    @FXML
    private TextField timeTextField;
    private SimpleDateFormat timeFormat;
    @FXML
    private Button setTimeButton;
    @FXML
    private AnchorPane infoPanel;
    private Object currentInfoAbout;
    @FXML
    private AnchorPane connectionListPanel;
    @FXML
    private Slider speedSlider;
    @FXML
    private ScrollPane infoScrollPane;
    @FXML
    private Label speedLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private AnchorPane map;
    private List<DrawableUpdate> drawableUpdatesElements = new CopyOnWriteArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

    Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeLabel.setText(time.format(DateTimeFormatter.ofPattern("H:mm:ss")));
        }
    };
    public void addUpdate(DrawableUpdate drawableUpdate) {
        drawableUpdatesElements.add(drawableUpdate);
    }

    public void setLabelText(String labelText) {
        this.infoLabel.setText(labelText);
    }

    public void setupTime(){
        timeFormat = new SimpleDateFormat("HH:mm");
        try {
            timeTextField.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(timeFormat), timeFormat.parse("00:00:00")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setupSpeedSlider() {
        speedLabel.textProperty().bind(
                Bindings.format("%.0fx", speedSlider.valueProperty())
        );
        timeSpeed = (long)speedSlider.getValue();
        speedSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, this::changeSpeed);
    }

    @FXML
    private void setTime(MouseEvent me){
        time = LocalTime.parse(timeTextField.getText());
        startConnections();
    }

    private long timeDifference(LocalTime start) {
        long duration = 0;
        if (start.isAfter(time)) {
            duration += ChronoUnit.SECONDS.between(start, LocalTime.parse("23:59:59"));
            duration += ChronoUnit.SECONDS.between(LocalTime.parse("00:00"), time);
        } else {
            duration = ChronoUnit.SECONDS.between(start, time);
        }
        return duration;
    }

    public void closeTimer() {
        timer.cancel();
    }

    public void startConnections(){
        firstStart = true;
        for (Connection connection : data.getConnections()) {
            drawableUpdatesElements.remove(connection);
            map.getChildren().removeAll(connection.getDrawableObjects());
            connection.setUpObject();
            if(connection.isAffectedByTimeShift(time)) {
                for (int i = 0; i < timeDifference(connection.getStartTime()); i++) {
                    connection.update(time);
                }
                setUpConnection(connection);
            }
        }
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

    private void createStopInfoGroup(Stop s, Connection con, double yOffset, double val){
        Label label = new Label(s.getId());
        label.setLayoutX(39);
        label.setLayoutY(yOffset);
        ProgressIndicator progressIndicator = new ProgressIndicator(val);
        progressIndicator.setLayoutX(8);
        progressIndicator.setLayoutY(yOffset);
        con.indicators.add(progressIndicator);
        connectionListPanel.getChildren().add(progressIndicator);
        connectionListPanel.getChildren().add(label);
        yOffset += 30;
    }

    private void changeInfo(Object o){
        if (currentInfoAbout instanceof Connection){
            Connection con = (Connection) currentInfoAbout;
            con.indicators.clear();
            connectionListPanel.getChildren().clear();
            infoScrollPane.setVisible(false);
            // changing the color of stops back to default
            for (Stop s : con.getLine().getStops()){
                highlightStop(s, Color.RED);
            }
        } else if (currentInfoAbout instanceof Street){
            Street street = (Street) currentInfoAbout;
            infoPanel.getChildren().removeAll(street.getDrawnInfoObjects());
            ((Street) currentInfoAbout).isSelected = false;
            javafx.scene.shape.Line line = null;
            for (Shape shape : ((Street) currentInfoAbout).getDrawableObjects()){
                if (shape instanceof javafx.scene.shape.Line){
                    line = (javafx.scene.shape.Line) shape;
                }
            }
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(5);
            for (Stop s : street.getStops()){
                for (Shape shape : s.getDrawableObjects()){
                    if (shape instanceof Circle){
                        ((Circle) shape).setRadius(6);
                    }
                }
            }
        }
        currentInfoAbout = o;
    }

    public void strokeStreet(MouseEvent me) {
        me.consume();
        javafx.scene.shape.Line line = (javafx.scene.shape.Line) me.getSource();
        line.setStrokeWidth(8.5);
        Street street = data.getStreetById(line.getId());
        List<Shape> shapes = new ArrayList<>();
        for (Stop stop : street.getStops()) {
            shapes.addAll(stop.getDrawableObjects());
        }
        for (Shape shape : shapes) {
            if(shape instanceof Circle) {
                Circle circle = (Circle) shape;
                circle.setRadius(8.5);
            }
        }
        line.setStroke(Color.DARKGREEN);
    }

    public void unStrokeStreet(MouseEvent me) {
        me.consume();
        javafx.scene.shape.Line line = (javafx.scene.shape.Line) me.getSource();
        Street street = data.getStreetById(line.getId());
        if (street.isSelected) {
            return;
        }
        line.setStrokeWidth(5);
        line.setStroke(Color.BLACK);
        List<Shape> shapes = new ArrayList<>();
        for (Stop stop : street.getStops()) {
            shapes.addAll(stop.getDrawableObjects());
        }
        for (Shape shape : shapes) {
            if(shape instanceof Circle) {
                Circle circle = (Circle) shape;
                circle.setRadius(6);
            }
        }
    }

    public void showStreetInfo(MouseEvent me){
        me.consume();
        // finding street object
        javafx.scene.shape.Line line = (javafx.scene.shape.Line) me.getSource();
        Street street = data.getStreetById(line.getId());
        street.isSelected = true;
        // highlighting the street
        line.setStroke(Color.DARKGREEN);
        line.setStrokeWidth(8.5);
        for (Stop s : street.getStops()){
            for (Shape shape : s.getDrawableObjects()){
                if (shape instanceof Circle){
                    ((Circle) shape).setRadius(8.5);
                }
            }
        }
        // changing info panel
        if (currentInfoAbout == street){
            return;
        } else{
            changeInfo(street);
        }
        // drawing street info nodes
        infoLabel.setText(street.getId());

        Rectangle trafficCard = new Rectangle(5,39, 191, 390);
        trafficCard.setArcHeight(5);
        trafficCard.setArcWidth(5);
        trafficCard.setFill(Color.WHITE);
        trafficCard.setStroke(Color.BLACK);
        trafficCard.setStrokeType(StrokeType.INSIDE);
        street.addInfoObject(trafficCard);

        Label trafficLabel = new Label("Traffic:");
        trafficLabel.setLayoutX(78);
        trafficLabel.setLayoutY(47);
        trafficLabel.setPrefSize(49, 29);
        trafficLabel.setAlignment(Pos.CENTER);
        trafficLabel.setFont(Font.font(13));
        trafficLabel.setStyle("-fx-font-weight: bold;");
        street.addInfoObject(trafficLabel);

        Label slowLabel = new Label("Slow");
        slowLabel.setLayoutX(10);
        slowLabel.setLayoutY(75);
        slowLabel.setPrefSize(49, 29);
        slowLabel.setAlignment(Pos.CENTER);
        slowLabel.setFont(Font.font(13));
        slowLabel.setStyle("-fx-font-style: italic; ");
        street.addInfoObject(slowLabel);

        Label fastLabel = new Label("Fast");
        fastLabel.setLayoutX(144);
        fastLabel.setLayoutY(75);
        fastLabel.setPrefSize(49, 29);
        fastLabel.setAlignment(Pos.CENTER);
        fastLabel.setFont(Font.font(13));
        fastLabel.setStyle("-fx-font-style: italic;");
        street.addInfoObject(fastLabel);

        Slider trafficSlider = new Slider(0.2, 1, street.getTraffic());
        trafficSlider.setLayoutX(31);
        trafficSlider.setLayoutY(68);
        trafficSlider.setMajorTickUnit(0.1);
        trafficSlider.prefHeight(14);
        trafficSlider.prefWidth(141);
        trafficSlider.setSnapToTicks(true);
        trafficSlider.setMinorTickCount(0);
        trafficSlider.setId(street.getId());
        trafficSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, this::changeTraffic);
        street.addInfoObject(trafficSlider);

        infoPanel.getChildren().addAll(street.getDrawnInfoObjects());
    }

    private void changeTraffic(MouseEvent me){
        me.consume();
        // finding street object
        Slider slider = (Slider) me.getSource();
        Street street = data.getStreetById(slider.getId());
        for (DrawableUpdate drawableUpdate : drawableUpdatesElements) {
            if (drawableUpdate instanceof Connection) {
                Connection connection = (Connection) drawableUpdate;
                if (connection.getCurrentPath().isAffectedToTrafficChange(street, connection.getCurrentDestination())) {
                    connection.setDelay();
                }
            }
        }
        // changing the traffic
        street.setTraffic(slider.getValue());
    }

    private void highlightStop(Stop s, Color color){
        for (Shape shape : s.getDrawableObjects()){
            if (shape instanceof Circle){
                shape.setFill(color);
            }
        }
    }

    public void showConnectionInfo(MouseEvent me) {
        me.consume();
        // finding the connection
        Circle bus = (Circle) me.getSource();
        Connection con = data.getConnectionById(bus.getId());
        // changing info panel
        if (currentInfoAbout == con){
            return;
        } else{
            changeInfo(con);
        }
        // setting up drawing
        double yOffset = 5;
        infoScrollPane.setVisible(true);
        infoLabel.setText(con.getId());
        Line line = con.getLine();
        List<Stop> stops = line.getStops();
        int stopId = con.getNextStopIndex();
        // already passed stops
        for (int i = 0; i < stopId; i++){
            createStopInfoGroup(stops.get(i), con, yOffset, 1);
            yOffset += 30;
            highlightStop(stops.get(i), line.getStopColor());
        }
        // next stop
        createStopInfoGroup(stops.get(stopId), con, yOffset, 0);
        yOffset += 30;
        con.indicators.get(stopId).progressProperty().bind(con.currentProgress);
        highlightStop(stops.get(stopId), line.getStopColor());
        // upcoming stops
        for (int i = stopId+1; i < stops.size(); i++) {
            createStopInfoGroup(stops.get(i), con, yOffset, 0);
            yOffset += 30;
            highlightStop(stops.get(i), line.getStopColor());
        }
        con.getTimetable();
    }

    @FXML
    private void changeZoom(ScrollEvent event){
        event.consume();
        double scroll = event.getDeltaY();
        double zoomScale;
        if (scroll > 0){
            zoomScale = 1.1;
            if (map.getScaleX() > 1.17){
                return;
            }
        } else {
            zoomScale = 0.9;
            if (map.getScaleX() < 0.65){
                return;
            }
        }
        map.setScaleX(zoomScale * map.getScaleX());
        map.setScaleY(zoomScale * map.getScaleY());
    }

    public void draw(List<Shape> shapes){
        for (Shape s : shapes)
            map.getChildren().addAll(s);
    }

    public void setUpConnection(Connection connection) {
        for (Shape shape : connection.getDrawableObjects()) {
            if (shape instanceof Circle) {
                shape.addEventHandler(MouseEvent.MOUSE_PRESSED, this::showConnectionInfo);
            }
        }
        addUpdate(connection);
        draw(connection.getDrawableObjects());
    }

    public void checkConnections() {
        for (Connection connection : data.getConnections()) {
            if (connection.getStartTime().equals(time)) {
                setUpConnection(connection);
            }
        }
    }

    public void resetObject(Connection connection) {
        connection.setUpObject();
    }

    public void runTime() {
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                if (!firstStart) {
                    Platform.runLater(() -> startConnections());
                }
                for (DrawableUpdate drawableUpdate : drawableUpdatesElements) {
                    if (drawableUpdate.update(time) == 1) {
                        Platform.runLater(() -> map.getChildren().removeAll(drawableUpdate.getDrawableObjects()));
                        Platform.runLater(() -> drawableUpdatesElements.remove(drawableUpdate));
                        Platform.runLater(() -> resetObject((Connection) drawableUpdate));
                    }
                }
                // timer update
                Platform.runLater(updateTimer);
            }
        }, 0, 1000 / timeSpeed);
    }
}
