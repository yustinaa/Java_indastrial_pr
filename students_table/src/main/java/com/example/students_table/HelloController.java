package com.example.students_table;

import javafx.fxml.FXML;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {
    @FXML
    private TableView<Students> table;

    @FXML
    private TableColumn<Students, Long> colNum;

    @FXML
    private TableColumn<Students, String> colName;

    @FXML
    private TableColumn<Students, Integer> colGroup;

    @FXML
    private TableColumn<Students, Double> colGrade;

    @FXML
    private ComboBox<String> sortBox;

    private ObservableList<Students> studentsList;

    public void initialize() {

        colNum.setCellValueFactory(new PropertyValueFactory<>("num"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        studentsList = FXCollections.observableArrayList(
                Students.loadF("input.txt")
        );

        table.setItems(studentsList);//данные устанавливаются в таблицу

        sortBox.getItems().addAll(
                "By ID",
                "By name",
                "group+name",
                "group+grade",
                "grade increase",
                "grade decrease"
        );

        sortBox.setOnAction(event -> chooseSorting());
    }

    private void chooseSorting() {
        String option = sortBox.getValue();
        if (option == null) return;

        switch (option) {

            case "By ID" -> studentsList.sort(new Students.IdComparator());

            case "By name" -> studentsList.sort(new Students.NameComparator());

            case "group+name" -> studentsList.sort(new Students.GroupNameComparator());

            case "group+grade" -> studentsList.sort(new Students.GroupGradeComparator());

            case "grade increase" -> studentsList.sort(new Students.GradeAscComparator());

            case "grade decrease" -> studentsList.sort(new Students.GradeDecComparator());
        }
    }
}
