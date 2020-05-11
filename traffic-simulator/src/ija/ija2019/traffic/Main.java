package ija.ija2019.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ija.ija2019.traffic.maps.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import ija.ija2019.traffic.maps.Stop;
import ija.ija2019.traffic.maps.Street;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        YAMLFactory yaml_factory = new YAMLFactory();
        ObjectMapper obj_mapper = new ObjectMapper(yaml_factory);
        Data data = obj_mapper.readValue(new File("data/map.yml"), Data.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(scene);

        Controller controller = loader.getController();

        for (Street s : data.getStreets()){
            s.setDrawableObjects();
            controller.draw(s.getDrawableObjects());
        }

        for (Stop s : data.getStops()) {
            s.setDrawableObjects();
            controller.draw(s.getDrawableObjects());
        }
        
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
