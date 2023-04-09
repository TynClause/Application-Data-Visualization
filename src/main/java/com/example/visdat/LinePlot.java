package com.example.visdat;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class LinePlot {

    public VBox linePlotID;
    public TextField fileName;
    public TableView tableView;
    public ChoiceBox columnSelect;
    public LineChart lineChart;
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
        Stage stage = (Stage) linePlotID.getScene().getWindow();
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
//=============== Method 1 (Use opencsv) =================
//        FileReader filereader = new FileReader(fileName.getText());
//        CSVReader csvReader = new CSVReader(filereader);
//        List<String[]> data = csvReader.readAll();
//
//        boolean flag = false;
//        ObservableList<TableColumn> tableColumns = FXCollections.observableArrayList();
//
//        for (String[] row : data) {
//            if(!flag){
//                for (String cell : row) {
//                    TableColumn<String,String> column = new TableColumn<>(cell);
//                    tableColumns.add(column);
//                }
//                tableView.getColumns().addAll(tableColumns);
//            }
//            else {
//                ObservableList<String> observableList = FXCollections.observableArrayList(row);
//
//                System.out.println(observableList);
//                tableView.getItems().add(observableList);
//
//            }
//            flag = true;
//        }

//=========================== Method 2 =====================================
        List<List<String>> csvData = readCSV(fileName.getText());

        this.columnNames = csvData.get(0);

//        System.out.println(columnNames);


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

//        String columnSelected = columnSelect.getValue().toString();
//        int columnIndex = columnNames.indexOf(columnSelected);
//        ObservableList<XYChart.Data<String, Float>> dataForLineChart = FXCollections.observableArrayList();
//
//        ObservableList<List<String>> data = tableView.getItems();
//        TableColumn<List<String>, String> column = (TableColumn<List<String>, String>) tableView.getColumns().get(columnIndex);
//
//        if (column != null) {
//            column.setCellValueFactory(param -> {
//                List<String> row = param.getValue();
//                String cellValue = row.get(columnIndex);
//                return new SimpleStringProperty(cellValue);
//            });
//
//            int counter = 1;
//            for (List<String> row : data) {
//                String actualValue = column.getCellObservableValue(row).getValue();
//                XYChart.Data<String, Float> dataPoint = new XYChart.Data<>("data " + Integer.toString(counter),
//                        Float.parseFloat(actualValue));
//                dataForLineChart.add(dataPoint);
//                System.out.println(dataPoint);
//                counter++;
//            }
//        }
//
//        lineChart.getData().clear();
//        lineChart.getData().add(new XYChart.Series<>("Data", dataForLineChart));

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

        lineChart.setStyle("-fx-stroke: " + hexColor + ";");

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

            lineChart.getData().add(series);
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
