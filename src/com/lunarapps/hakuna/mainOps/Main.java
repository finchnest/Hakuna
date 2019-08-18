package com.lunarapps.hakuna.mainOps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {//the application's gateway launch


    @Override
    //abstract method in application class...called after init method
    public void start(Stage primaryStage) throws Exception{
        Parent fxml = FXMLLoader.load(getClass().getResource("../layouts/main.fxml"));//loads object hierarchy from fxml doc

        Scene scene = new Scene(fxml);
        scene.getStylesheets().add("../css/main.css");
        Stage stage=new Stage();
        stage.setScene(scene);

        stage.initStyle(StageStyle.UTILITY);

        stage.show();

    }



    public static void main(String[] args) {
        launch(args);//starts the whole operation
    }
}
