package org.example.thread_sort;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.application.Platform;
import java.io.*;
import java.util.*;

public class HelloController {


    @FXML private Button startBtn;

    @FXML private Label start1;
    @FXML private Label end1;

    @FXML private Label start2;
    @FXML private Label end2;

    @FXML private Label start3;
    @FXML private Label end3;

    @FXML
    private BarChart<String, Number> barChart1;

    @FXML
    private BarChart<String, Number> barChart2;

    @FXML
    private BarChart<String, Number> barChart3;

        private List<Integer> numbers = new ArrayList<>();

        @FXML
        public void initialize() {
            /*fixBarChart(barChart1);
            fixBarChart(barChart2);
            fixBarChart(barChart3);*/

            loadFromFile("numbers.txt");

            startBtn.setOnAction(e -> startSorting());


        }

    private void startSorting() {

        fixBarChart(barChart1);
        fixBarChart(barChart2);
        fixBarChart(barChart3);
        List<Integer> arr1 = new ArrayList<>(numbers);
        List<Integer> arr2 = new ArrayList<>(numbers);
        List<Integer> arr3 = new ArrayList<>(numbers);

        /*showArray(barChart1, arr1);
        showArray(barChart2, arr2);
        showArray(barChart3, arr3);*/


        var s1 = initSeries(barChart1, arr1);
        var s2 = initSeries(barChart2, arr2);
        var s3 = initSeries(barChart3, arr3);

        bubbleSort(arr1, s1, start1, end1);
        selectionSort(arr2, s2, start2, end2);
        insertionSort(arr3, s3, start3, end3);
    }


    private void setTime(Label label, String text) {
        Platform.runLater(() -> label.setText(text));
    }

    private String now() {
        return java.time.LocalTime.now().withNano(0).toString();
    }

    private void fixBarChart(BarChart<String, Number> chart) {
        chart.setCategoryGap(0);  // расстояние между категориями
        chart.setBarGap(0);       // расстояние между столбиками
    }


    private XYChart.Series<String, Number> initSeries(BarChart<String, Number> chart, List<Integer> arr) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < arr.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), arr.get(i)));
        }

        chart.getData().clear();
        chart.getData().add(series);

        return series;
    }


    private void updateFrame(XYChart.Series<String, Number> series, List<Integer> arr) {
        Platform.runLater(() -> {
            for (int i = 0; i < arr.size(); i++) {
                series.getData().get(i).setYValue(arr.get(i));
            }
        });
    }


    private void bubbleSort(List<Integer> arr, XYChart.Series<String, Number> series,
                            Label startLabel, Label endLabel) {

        new Thread(() -> {
            try {
                setTime(startLabel, "Старт: " + now());

                for (int i = 0; i < arr.size(); i++) {
                    for (int j = 0; j < arr.size() - 1 - i; j++) {
                        if (arr.get(j) > arr.get(j + 1)) {
                            Collections.swap(arr, j, j + 1);
                        }
                        updateFrame(series, arr);
                        Thread.sleep(150);
                    }
                }

                setTime(endLabel, "Конец: " + now());

            } catch (InterruptedException ignored) {}
        }).start();
    }


    private void selectionSort(List<Integer> arr, XYChart.Series<String, Number> series,
                               Label startLabel, Label endLabel) {

        new Thread(() -> {
            try {
                setTime(startLabel, "Старт: " + now());

                for (int i = 0; i < arr.size(); i++) {
                    int min = i;
                    for (int j = i + 1; j < arr.size(); j++) {
                        if (arr.get(j) < arr.get(min)) min = j;
                    }
                    Collections.swap(arr, i, min);
                    updateFrame(series, arr);
                    Thread.sleep(150);
                }

                setTime(endLabel, "Конец: " + now());

            } catch (InterruptedException ignored) {}
        }).start();
    }


    private void insertionSort(List<Integer> arr, XYChart.Series<String, Number> series,
                               Label startLabel, Label endLabel) {

        new Thread(() -> {
            try {
                setTime(startLabel, "Старт: " + now());

                for (int i = 1; i < arr.size(); i++) {
                    int key = arr.get(i);
                    int j = i - 1;
                    while (j >= 0 && arr.get(j) > key) {
                        arr.set(j + 1, arr.get(j));
                        j--;
                        updateFrame(series, arr);
                        Thread.sleep(150);
                    }
                    arr.set(j + 1, key);
                    updateFrame(series, arr);
                }

                setTime(endLabel, "Конец: " + now());

            } catch (InterruptedException ignored) {}
        }).start();
    }






    private void loadFromFile(String filename) {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line = br.readLine();
                if (line != null) {
                    for (String s : line.split("\\s+")) {
                        numbers.add(Integer.parseInt(s));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void showArray(BarChart<String, Number> chart, List<Integer> arr) {
            chart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (int i = 0; i < arr.size(); i++) {
                series.getData().add(new XYChart.Data<>(String.valueOf(i), arr.get(i)));
            }

            chart.getData().add(series);
        }

    }

