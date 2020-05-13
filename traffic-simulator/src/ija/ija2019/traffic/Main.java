package ija.ija2019.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ija.ija2019.traffic.maps.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        YAMLFactory yaml_factory = new YAMLFactory();
        ObjectMapper obj_mapper = new ObjectMapper(yaml_factory);
        Data data = obj_mapper.readValue(new File("data/data.yml"), Data.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Traffic Simulator");
        primaryStage.setScene(scene);

        Controller controller = loader.getController();

        for (Drawable drawable : data.getDrawables()){
            controller.draw(drawable.getDrawableObjects());
            if (drawable instanceof DrawableUpdate)
                controller.addUpdate((DrawableUpdate) drawable);
        }

        primaryStage.show();
        controller.runTime();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
