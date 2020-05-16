package ija.ija2019.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ija.ija2019.traffic.maps.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalTime;

import ija.ija2019.traffic.Controller;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        YAMLFactory yaml_factory = new YAMLFactory();
        ObjectMapper obj_mapper = new ObjectMapper(yaml_factory);
        Data data = obj_mapper.readValue(new File("data/data.yml"), Data.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(scene);

        Controller controller = loader.getController();
        controller.setupSpeedSlider();
        controller.giveData(data);

        for (Drawable drawable : data.getDrawables()){
            if (drawable instanceof Connection){
                for (Shape shape :drawable.getDrawableObjects()){
                    if (shape instanceof Circle){
                        shape.addEventHandler(MouseEvent.MOUSE_PRESSED, controller::showConnectionInfo);
                    }
                }
            } else if (drawable instanceof Street){
                for (Shape shape :drawable.getDrawableObjects()){
                    if (shape instanceof Line){
                        shape.addEventHandler(MouseEvent.MOUSE_PRESSED, controller::showStreetInfo);
                        shape.addEventHandler(MouseEvent.MOUSE_ENTERED, controller::strokeStreet);
                        shape.addEventHandler(MouseEvent.MOUSE_EXITED, controller::unStrokeStreet);
                    }
                }
            }
            if (!(drawable instanceof Connection)) {
                controller.draw(drawable.getDrawableObjects());
            }
        }

        primaryStage.show();
        controller.runTime();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
