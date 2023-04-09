package com.example.visdat;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScatterPlot {
    public VBox scatterPlotID;
    public TextField fileName;
    public TableView tableView;
    public ChoiceBox columnSelect;
    public ScatterChart scatterChart;
    public Button viz;
    public ColorPicker colorPicker;

    private List<String> columnNames = null;

    public static List<List<String>> readCSV(String file) {
        List<List<String>> csvData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                csvData.add(Arrays.asList(parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvData;
    }

    @FXML
    protected void openFile(){
        Stage stage = (Stage) scatterPlotID.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Comma Seperated Values (*.csv)",
                "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null){
            fileName.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void runProcess() {
        List<List<String>> csvData = readCSV(fileName.getText());

        this.columnNames = csvData.get(0);


        for (int i = 0; i < csvData.get(0).size(); i++) {
            TableColumn<List<String>, String> column = new TableColumn<>(this.columnNames.get(i));
            final int colIndex = i;
            column.setCellValueFactory(param -> {
                List<String> row = param.getValue();
                if (row != null && colIndex < row.size()) {
                    return new SimpleStringProperty(row.get(colIndex));
                } else {
                    return new SimpleStringProperty("");
                }
            });
            tableView.getColumns().add(column);
        }

        ObservableList<List<String>> data = FXCollections.observableArrayList();
        data.addAll(csvData.subList(1, csvData.size()));
        tableView.setItems(data);

        columnSelect.getItems().addAll(this.columnNames);
        columnSelect.setDisable(false);
        viz.setDisable(false);
    }

    @FXML
    protected void visualize() {
        Color color = colorPicker.getValue();
        String columnSelected = (String) columnSelect.getValue();
        int columnIndex = columnNames.indexOf(columnSelected);
        XYChart.Series series = new XYChart.Series();
        TableColumn<List<String>, String> column = null;
        if (columnIndex >= 0) {
            column = (TableColumn<List<String>, String>) tableView.getColumns().get(columnIndex);
        }
        ObservableList<List<String>> data = tableView.getItems();

        String hexColor = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        scatterChart.setStyle("-fx-stroke: " + hexColor + ";");

        if (column != null) {
            column.setCellValueFactory(param -> {
                List<String> row = param.getValue();
                String cellValue = row.get(columnIndex);
                return new SimpleStringProperty(cellValue);
            });
            int count = 0;
            for (List<String> row : data) {
                String actualValue = column.getCellObservableValue(row).getValue();
                System.out.println(actualValue);
                series.getData().add(new XYChart.Data(String.valueOf(count), Double.parseDouble(actualValue)));
                System.out.println(Double.parseDouble(actualValue));
                count++;
            }

            scatterChart.getData().add(series);
        }
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
