package com.example.gifts_kr;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.collections.ListChangeListener;


import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private ComboBox<Gifts> pozdrav;

    @FXML
    private ListView<String> spisok_podarkov;

    @FXML
    private RadioButton concert;

    @FXML
    private CheckBox is_client;

    @FXML
    private Label cena;

    @FXML
    private TextArea end_spisok_podarkov;

    private int concertPrice = 3000;

    @FXML
    public void initialize() {

        pozdrav.getItems().addAll(
                new Flowers(),
                new Juwerly(),
                new Chocolate()
        );

        pozdrav.setConverter(new StringConverter<>() {
            @Override
            public String toString(Gifts p) {
                return (p == null) ? "" : p.getName();
            }

            @Override
            public Gifts fromString(String s) {
                return null;
            }
        });

        pozdrav.setOnAction(e -> {
            updateGifts();
            updatePrice();
        });

        spisok_podarkov.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        spisok_podarkov.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<String>) change -> updatePrice()
        );
        concert.setOnAction(e -> updatePrice());
        is_client.setOnAction(e -> updatePrice());
    }

    private void updateGifts() {
        Gifts selected = pozdrav.getValue();

        spisok_podarkov.getItems().clear();

        if (selected != null) {
            spisok_podarkov.getItems().addAll(selected.getGifts().keySet());
        }
    }

    private void updatePrice() {
        Gifts p = pozdrav.getValue();

        List<String> selectedGifts = new ArrayList<>(spisok_podarkov.getSelectionModel().getSelectedItems());

        if (p == null || selectedGifts.isEmpty()) {
            cena.setText("Цена: 0");
            end_spisok_podarkov.setText("Поздравитель: " + (p == null ? "-" : p.getName()) + "\nПодарки: -");
            return;
        }

        double total = 0;

        for (String gift : selectedGifts) {
            Integer price = p.getGifts().get(gift);
            if (price != null) total += price;
        }

        if (concert.isSelected()) {
            total += concertPrice;
        }

        if (is_client.isSelected()) {
            total *= 0.9;
        }

        int totalInt = (int)(total);

        cena.setText("Цена: " + totalInt);

        end_spisok_podarkov.setText(
                "Поздравитель: " + p.getName() + "\n" +
                        "Подарки: " + String.join(", ", selectedGifts) + "\n" +
                        "Концерт: " + (concert.isSelected() ? "Да" : "Нет") + "\n" +
                        "Постоянный клиент: " + (is_client.isSelected() ? "Да (10%)" : "Нет") + "\n" +
                        "Итоговая сумма: " + totalInt
        );
    }
}
