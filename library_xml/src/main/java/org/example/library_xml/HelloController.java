package org.example.library_xml;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    @FXML private TitledPane addBookPane;

    @FXML private TextField tfTitle;
    @FXML private TextField tfAuthor;
    @FXML private TextField tfYear;
    @FXML private TextField tfPrice;
    @FXML private TextField tfCategory;
    @FXML private TextField tfCopies;
    @FXML private TextField tfAvailable;

    @FXML private CheckBox checkXsd;

    private ObservableList<Book> books = FXCollections.observableArrayList();

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

        table.setItems(books);
    }

    @FXML
    private void onLoadXml() {
        List<Book> loadedBooks = xml.loadXML();

        books.clear();
        books.addAll(loadedBooks);

        boolean isValid = xml.isXmlValid();
        checkXsd.setSelected(isValid);

        if (loadedBooks.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось загрузить XML-файл!");
        } else if (!isValid) {
            showAlert(Alert.AlertType.WARNING, "Внимание", "XML-файл загружен, но не прошел XSD-валидацию!");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Успех", "XML-файл успешно загружен и прошел XSD-валидацию.");
        }
    }

    @FXML
    protected void onAddBook() {
        addBookPane.setExpanded(!addBookPane.isExpanded());
        if(addBookPane.isExpanded()){
            clearForm();
        }
    }

    @FXML
    private void onConfirmAddBook() {
        try {
            if (tfTitle.getText().isEmpty() || tfAuthor.getText().isEmpty() ||
                    tfYear.getText().isEmpty() || tfPrice.getText().isEmpty() ||
                    tfCategory.getText().isEmpty() || tfCopies.getText().isEmpty() ||
                    tfAvailable.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Ошибка ввода", "Все поля должны быть заполнены!");
                return;
            }

            int newId = xml.getNewBookId();

            Book b = new Book(
                    newId,
                    Integer.parseInt(tfCopies.getText()),
                    Integer.parseInt(tfAvailable.getText()),
                    tfTitle.getText(),
                    tfAuthor.getText(),
                    Integer.parseInt(tfYear.getText()),
                    Double.parseDouble(tfPrice.getText()),
                    tfCategory.getText()
            );

            xml.addBook(b);
            books.add(b);   //добавл в ObservableList (для обновления таблицы)

            addBookPane.setExpanded(false);
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Успех", "Книга успешно добавлена!");

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Ошибка ввода", "Поля Год, Цена, Копии и Доступно должны быть числами.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при добавлении книги: " + ex.getMessage());
        }
    }


    @FXML
    protected void onChangePrice() {
        TextInputDialog dialog = createTextInputDialog("Изменить цену", "Введите ID книги и новую цену через запятую: id,newPrice");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                if (parts.length != 2) throw new IllegalArgumentException("Неверный формат");

                int id = Integer.parseInt(parts[0].trim());
                double price = Double.parseDouble(parts[1].trim());

                xml.changePrice(id, price);
                updateBookList(); // обновл ObservableList

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверный формат ввода (ожидаются: id,цена)!");
            }
        });
    }

    @FXML
    protected void onGiveBook() {
        TextInputDialog dialog = createTextInputDialog("Выдать книгу", "Введите ID книги для выдачи:");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                int id = Integer.parseInt(input.trim());
                xml.giveBook(id);
                updateBookList();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверный формат ввода (ожидается числовой ID)!");
            } catch (Exception e) {

            }
        });
    }

    @FXML
    protected void onSearch() {
        TextInputDialog dialog = createTextInputDialog("Поиск книги", "Введите автор, год, категория через запятую:");
        Optional<String> res = dialog.showAndWait();
        res.ifPresent(input -> {
            try {
                String[] parts = (input + ",,").split(",", 3);

                String author = parts[0].trim().isEmpty() ? null : parts[0].trim();
                Integer year = parts[1].trim().isEmpty() ? null : Integer.parseInt(parts[1].trim());
                String category = parts[2].trim().isEmpty() ? null : parts[2].trim();

                List<Book> result = xml.searchBooks(author, year, category);

                books.clear();
                books.addAll(result);

                showAlert(Alert.AlertType.INFORMATION, "Поиск завершен", "Найдено книг: " + result.size());

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Год должен быть числом!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверный формат ввода!");
            }
        });
    }

    private void updateBookList() {
        books.clear();
        try {
            books.addAll(xml.getBooksXPath());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка обновления", "Не удалось обновить список книг.");
        }
    }

    private void clearForm() {
        tfTitle.clear();
        tfAuthor.clear();
        tfYear.clear();
        tfPrice.clear();
        tfCategory.clear();
        tfCopies.clear();
        tfAvailable.clear();
    }

    private TextInputDialog createTextInputDialog(String title, String header) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        return dialog;
    }

    private void showAlert(Alert.AlertType type, String title, String text) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(text);
        a.showAndWait();
    }
}