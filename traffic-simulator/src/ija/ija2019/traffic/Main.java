package ija.ija2019.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ija.ija2019.traffic.maps.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ija.ija2019.traffic.maps.Stop;
import ija.ija2019.traffic.maps.Street;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // ------- demonstracia citacia yaml ---------
        YAMLFactory yaml_factory = new YAMLFactory();
        ObjectMapper obj_mapper = new ObjectMapper(yaml_factory);
        Data data = obj_mapper.readValue(new File("data/map.yml"), Data.class);

        for (Street s : data.getStreets()){
            System.out.println(s);
        }

        for (Stop s : data.getStops()){
            System.out.println(s);
        }
        // -----------------------------------------------------
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
