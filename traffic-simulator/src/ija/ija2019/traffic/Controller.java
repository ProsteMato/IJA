package ija.ija2019.traffic;

import ija.ija2019.traffic.maps.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.converter.DateTimeStringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controller of the application
 * @version 1.0
 * @author <a href="xkocim05@stud.fit.vutbr.cz">Martin Koči</a>
 * @author <a href="xkoval17@stud.fit.vutbr.cz">Michal Koval</a>
 */
public class Controller {
    public AnchorPane getMap() {
        return map;
    }
    private Data data;
    private long timeSpeed = 1;
    private boolean firstStart = false;
    @FXML
    private TextField timeTextField;
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
    private Color prevColor;

    Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            timeLabel.setText(time.format(DateTimeFormatter.ofPattern("H:mm:ss")));
        }
    };

    /**
     * This function is adding updatable drawable objects in update list that will be updated.
     * @param drawableUpdate updatable object.
     */
    public void addUpdate(DrawableUpdate drawableUpdate) {
        drawableUpdatesElements.add(drawableUpdate);
    }

    /**
     * This function setUp field for setting time.
     */
    public void setupTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            timeTextField.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(timeFormat), timeFormat.parse("00:00:00")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method setUps speed slider
     */
    public void setupSpeedSlider() {
        speedLabel.textProperty().bind(
                Bindings.format("%.0fx", speedSlider.valueProperty())
        );
        timeSpeed = (long)speedSlider.getValue();
        speedSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, this::changeSpeed);
    }

    /**
     * This method sets new time and checks connections that have to be on they way in that time
     */
    @FXML
    private void setTime(){
        time = LocalTime.parse(timeTextField.getText());
        startConnections();
    }

    /**
     * This function calculates time difference
     * @param start start of time
     * @return  difference of actual and given time.
     */
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

    /**
     * Closes timer
     */
    public void closeTimer() {
        timer.cancel();
    }

    /**
     * checks what connections needs to be on they way in given time and get there in position where they belong
     */
    public void startConnections(){
        firstStart = true;
        for (Connection connection : data.getConnections()) {
            LocalTime costumeTime = connection.getStartTime();
            drawableUpdatesElements.remove(connection);
            map.getChildren().removeAll(connection.getDrawableObjects());
            connection.setUpObject();
            if(connection.isAffectedByTimeShift(time)) {
                for (int i = 0; i < timeDifference(connection.getStartTime()); i++) {
                    costumeTime = costumeTime.plusSeconds(1);
                    connection.update(costumeTime);
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

    /**
     * This method gives data to controller
     * @param data data
     */
    public void giveData(Data data){
        this.data = data;
    }

    private HBox createStopInfoGroup(Stop s, Connection con, double progressValue, String time){
        Label label = new Label(s.getId());
        ProgressIndicator progressIndicator = new ProgressIndicator(progressValue);
        con.getIndicators().add(progressIndicator);
        Label timeLabel = new Label(time);
        HBox hBox = new HBox(10, progressIndicator, timeLabel, label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    private void changeInfo(Object o){
        if (currentInfoAbout instanceof Connection){
            Connection con = (Connection) currentInfoAbout;
            con.getIndicators().clear();
            connectionListPanel.getChildren().clear();
            infoScrollPane.setVisible(false);
            // changing the color of stops back to default
            for (Stop s : con.getLine().getStops()){
                highlightStop(s, Color.RED);
            }
            for (Street s : con.getLine().getStreets()){
                highlightStreet(s, Color.BLACK);
            }
            // removing delay labels
            infoPanel.getChildren().removeAll(con.getDelayLabels());
        } else if (currentInfoAbout instanceof Street){
            Street street = (Street) currentInfoAbout;
            infoPanel.getChildren().removeAll(street.getDrawnInfoObjects());
            ((Street) currentInfoAbout).setSelected(false);
            javafx.scene.shape.Line line = null;
            for (Shape shape : ((Street) currentInfoAbout).getDrawableObjects()){
                if (shape instanceof javafx.scene.shape.Line){
                    line = (javafx.scene.shape.Line) shape;
                }
            }
            if(line != null) {
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(5);
            }
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

    /**
     * This method is highLighting street on mouse over event
     * @param me mouse event
     */
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
        prevColor = (Color)line.getStroke();
        line.setStroke(Color.DARKGREEN);
    }

    /**
     * This method is unHighLighting street when mouse goes from them.
     * @param me mouse event
     */
    public void unStrokeStreet(MouseEvent me) {
        me.consume();
        javafx.scene.shape.Line line = (javafx.scene.shape.Line) me.getSource();
        Street street = data.getStreetById(line.getId());
        if (street.isSelected()) {
            return;
        }
        line.setStrokeWidth(5);
        line.setStroke(prevColor);
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

    /**
     * This function displays street info on click event on street
     * @param me click event
     */
    public void showStreetInfo(MouseEvent me){
        me.consume();
        // finding street object
        javafx.scene.shape.Line line = (javafx.scene.shape.Line) me.getSource();
        Street street = data.getStreetById(line.getId());
        street.setSelected(true);
        // changing info panel
        if (currentInfoAbout == street){
            return;
        } else{
            changeInfo(street);
        }
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
        // drawing street info nodes
        infoLabel.setText(street.getId());

        Rectangle trafficCard = new Rectangle(24,47, 221, 371);
        trafficCard.setArcHeight(5);
        trafficCard.setArcWidth(5);
        trafficCard.setFill(Color.WHITE);
        trafficCard.setStroke(Color.BLACK);
        trafficCard.setStrokeType(StrokeType.INSIDE);
        street.addInfoObject(trafficCard);

        Label trafficLabel = new Label("Traffic:");
        trafficLabel.setLayoutX(98);
        trafficLabel.setLayoutY(47);
        trafficLabel.setPrefSize(76, 29);
        trafficLabel.setAlignment(Pos.CENTER);
        trafficLabel.setFont(Font.font(13));
        trafficLabel.setStyle("-fx-font-weight: bold;");
        street.addInfoObject(trafficLabel);

        Label slowLabel = new Label("Slow");
        slowLabel.setLayoutX(52);
        slowLabel.setLayoutY(75);
        slowLabel.setPrefSize(49, 29);
        slowLabel.setAlignment(Pos.CENTER);
        slowLabel.setFont(Font.font(13));
        slowLabel.setStyle("-fx-font-style: italic; ");
        street.addInfoObject(slowLabel);

        Label fastLabel = new Label("Fast");
        fastLabel.setLayoutX(180);
        fastLabel.setLayoutY(75);
        fastLabel.setPrefSize(49, 29);
        fastLabel.setAlignment(Pos.CENTER);
        fastLabel.setFont(Font.font(13));
        fastLabel.setStyle("-fx-font-style: italic;");
        street.addInfoObject(fastLabel);

        Slider trafficSlider = new Slider(0.2, 1, street.getTraffic());
        trafficSlider.setLayoutX(66);
        trafficSlider.setLayoutY(69);
        trafficSlider.setMajorTickUnit(0.1);
        trafficSlider.prefHeight(14);
        trafficSlider.prefWidth(141);
        trafficSlider.setSnapToTicks(true);
        trafficSlider.setMinorTickCount(0);
        trafficSlider.setId(street.getId());
        trafficSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, this::changeTraffic);
        street.addInfoObject(trafficSlider);

        Label stopsLabel = new Label("Stops:");
        stopsLabel.setLayoutX(26);
        stopsLabel.setLayoutY(110);
        stopsLabel.setPrefSize(60, 38);
        stopsLabel.setAlignment(Pos.CENTER_RIGHT);
        stopsLabel.setFont(Font.font(13));
        stopsLabel.setStyle("-fx-font-weight: bold;");
        street.addInfoObject(stopsLabel);

        /**
        Button closeStreetButton = new Button("Close street");
        closeStreetButton.setLayoutX(96);
        closeStreetButton.setLayoutY(387);
        closeStreetButton.setPrefSize(80, 25);
        // handler closeStreetButton.
        street.addInfoObject(closeStreetButton);
        **/
        double yOffset = 110;

        for (Stop s :street.getStops()){
            Label stopLabel = new Label(s.getId());
            stopLabel.setLayoutX(90);
            stopLabel.setLayoutY(yOffset);
            stopLabel.setPrefSize(120, 38);
            stopLabel.setAlignment(Pos.CENTER_LEFT);
            stopLabel.setFont(Font.font(13));
            street.addInfoObject(stopLabel);
            yOffset += 20;
        }

        // najdenie lines ulice
        List<Line> lines = new ArrayList<>();
        for (Line l : data.getLines()){
            for (Path p : l.getPaths()){
                for (Street s : p.getStreets()){
                    if (s.getId().equals(street.getId())){
                        if (!lines.contains(l)){
                            lines.add(l);
                        }
                    }
                }
            }
        }

        if (stopsLabel.getLayoutY() == yOffset){
            yOffset += 20;
        }

        Label linesLabel = new Label("Lines:");
        linesLabel.setLayoutX(26);
        linesLabel.setLayoutY(yOffset);
        linesLabel.setPrefSize(60, 38);
        linesLabel.setAlignment(Pos.CENTER_RIGHT);
        linesLabel.setFont(Font.font(13));
        linesLabel.setStyle("-fx-font-weight: bold;");
        street.addInfoObject(linesLabel);
        yOffset += 20;

        for (Line l : lines){
            Label lineLabel = new Label(l.getId());
            lineLabel.setLayoutX(90);
            lineLabel.setLayoutY(yOffset);
            lineLabel.setPrefSize(120, 38);
            lineLabel.setAlignment(Pos.CENTER_LEFT);
            lineLabel.setFont(Font.font(13));
            street.addInfoObject(lineLabel);
            yOffset += 20;
        }

        infoPanel.getChildren().addAll(street.getDrawnInfoObjects());
    }

    private void changeTraffic(MouseEvent me){
        me.consume();
        // finding street object
        Slider slider = (Slider) me.getSource();
        Street street = data.getStreetById(slider.getId());
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

    private void highlightStreet(Street street, Color color) {
        for (Shape shape : street.getDrawableObjects()){
            if (shape instanceof javafx.scene.shape.Line){
                shape.setStroke(color);
            }
        }
    }

    /**
     * This function displays connection info on click event on connection.
     * @param me click event
     */
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
        infoScrollPane.setVisible(true);
        infoLabel.setText(con.getId());
        Line line = con.getLine();
        List<Stop> stops = line.getStops();
        int stopId = con.getNextStopIndex();
        List<TimeTable> timeTables = con.getTimetable();
        // delay
        List<Label> labelsList = con.getDelayLabels();
        Label delayTextLabel = new Label("Delay:");
        delayTextLabel.setLayoutX(7);
        delayTextLabel.setLayoutY(34);
        delayTextLabel.setPrefSize(122,47);
        delayTextLabel.setAlignment(Pos.CENTER_RIGHT);
        delayTextLabel.setFont(Font.font(17));
        labelsList.add(delayTextLabel);
        infoPanel.getChildren().add(delayTextLabel);
        Label delayLabel = new Label();
        delayLabel.setLayoutX(137);
        delayLabel.setLayoutY(34);
        delayLabel.setPrefSize(131,47);
        delayLabel.setAlignment(Pos.CENTER_LEFT);
        delayLabel.setFont(Font.font(17));
        delayLabel.textProperty().bind(
                Bindings.format("+%d min", con.delayPropertyProperty())
        );
        infoPanel.getChildren().add(delayLabel);

        for (Stop stop : stops) {
            highlightStop(stop, line.getStopColor());
        }
        for (Street street : con.getLine().getStreets()) {
            highlightStreet(street, line.getStopColor());
        }
        labelsList.add(delayLabel);
        VBox vbox = new VBox();
        // already passed stops
        for (int i = 0; i < stopId; i++){
            vbox.getChildren().add(createStopInfoGroup(stops.get(i), con, 1, timeTables.get(i).getTime()));
        }
        // next stop
        vbox.getChildren().add(createStopInfoGroup(stops.get(stopId), con, 0, timeTables.get(stopId).getTime()));
        con.getIndicators().get(stopId).progressProperty().bind(con.currentProgressProperty());
        highlightStop(stops.get(stopId), line.getStopColor());
        // upcoming stops
        for (int i = stopId+1; i < stops.size(); i++) {
            vbox.getChildren().add(createStopInfoGroup(stops.get(i), con, 0, timeTables.get(i).getTime()));
        }
        connectionListPanel.getChildren().add(vbox);
    }

    /**
     * This method changes zoom on scroll event
     * @param event scroll event
     */
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

    /**
     * This method draws objects
     * @param shapes object to be drown
     */
    public void draw(List<Shape> shapes){
        for (Shape s : shapes)
            map.getChildren().addAll(s);
    }

    /**
     * This function is setuping connections.
     * @param connection connection
     */
    public void setUpConnection(Connection connection) {
        for (Shape shape : connection.getDrawableObjects()) {
            if (shape instanceof Circle) {
                shape.addEventHandler(MouseEvent.MOUSE_PRESSED, this::showConnectionInfo);
            }
        }
        addUpdate(connection);
        draw(connection.getDrawableObjects());
    }

    /**
     * This method checks for start time of connetions and executing them
     */
    public void checkConnections() {
        for (Connection connection : data.getConnections()) {
            if (connection.getStartTime().equals(time)) {
                setUpConnection(connection);
            }
        }
    }

    /**
     * This function resets connections
     * @param connection connection to be reset.
     */
    public void resetObject(Connection connection) {
        connection.setUpObject();
    }

    /**
     * This method is simulating time.
     */
    public void runTime() {
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                if (!firstStart) {
                    Platform.runLater(() -> startConnections());
                }
                Platform.runLater(() -> checkConnections());
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
