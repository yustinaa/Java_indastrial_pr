package org.example.library_xml;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML private TableView<Book> table;

    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, Double> colPrice;
    @FXML private TableColumn<Book, String> colCategory;
    @FXML private TableColumn<Book, Integer> colCopies;
    @FXML private TableColumn<Book, Integer> colAvailable;


    private XMLManager xml = new XMLManager();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copies"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));


    }

    @FXML
    protected void onLoadXml() {
        if (xml.loadXML()) {
            table.setItems(FXCollections.observableArrayList(xml.getBooks()));
        } else {
            showAlert("Ошибка", "Не удалось загрузить XML или он не проходит XSD!");
        }
    }

    @FXML
    protected void onAddBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавить книгу");
        dialog.setHeaderText("Введите данные книги через запятую: title,author,year,price,category,copies,available");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                Book b = new Book(
                        xml.getBooks().size() + 1,
                        Integer.parseInt(parts[5].trim()),
                        Integer.parseInt(parts[6].trim()),
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        Double.parseDouble(parts[3].trim()),
                        parts[4].trim()
                );
                xml.addBook(b);
                table.setItems(FXCollections.observableArrayList(xml.getBooks()));
            } catch (Exception e) {
                showAlert("Ошибка", "Неверный формат ввода!");
            }
        });
    }

    @FXML
    protected void onChangePrice() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Изменить цену");
        dialog.setHeaderText("Введите ID книги и новую цену через запятую: id,newPrice");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                int id = Integer.parseInt(parts[0].trim());
                double price = Double.parseDouble(parts[1].trim());
                xml.changePrice(id, price);
                table.setItems(FXCollections.observableArrayList(xml.getBooks()));
            } catch (Exception e) {
                showAlert("Ошибка", "Неверный формат ввода!");
            }
        });
    }

    @FXML
    protected void onGiveBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Выдать книгу");
        dialog.setHeaderText("Введите ID книги для выдачи:");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                int id = Integer.parseInt(input.trim());
                xml.giveBook(id);
                table.setItems(FXCollections.observableArrayList(xml.getBooks()));
            } catch (Exception e) {
                showAlert("Ошибка", "Неверный формат ввода!");
            }
        });
    }

    @FXML
    protected void onSearch() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Поиск книги");
        dialog.setHeaderText("Введите автор, год, категория через запятую (оставьте пустым, если не фильтруем):");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                String author = parts.length > 0 ? parts[0].trim() : "";
                Integer year = parts.length > 1 && !parts[1].trim().isEmpty() ? Integer.parseInt(parts[1].trim()) : null;
                String category = parts.length > 2 ? parts[2].trim() : "";

                List<Book> result = xml.searchBooks(author, year, category);
                table.setItems(FXCollections.observableArrayList(result));
            } catch (Exception e) {
                showAlert("Ошибка", "Неверный формат ввода!");
            }
        });
    }

    private void showAlert(String title, String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(text);
        a.showAndWait();
    }
}

