package com.example.polish_zap_calc;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class HelloController {
    @FXML
    private TextField input;

    @FXML
    private Label post_zap;

    @FXML
    private Label result_l;
    @FXML
    protected void onButtonClick() {
        try {
            String expr = input.getText();
            String post = PolishCalc.infToPost(expr);
            int result = PolishCalc.countRes(post);

            post_zap.setText(post);
            result_l.setText(String.valueOf(result));
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Что-то пошло не так 😕");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
