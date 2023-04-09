package com.example.visdat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ControllerScreen {

    @FXML
    protected void linePlotScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lineplot.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Line Plot");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void barPlotScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("barplot.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Bar Plot");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void scatterPlotScreen() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scatterplot.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Scatter Plot");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void closeApp(){
        Platform.exit();
    }

    @FXML
    protected void about() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("about.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Scatter Plot");
        stage.setScene(scene);
        stage.show();
    }
}