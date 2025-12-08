package org.example.library_xml;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class XMLManager {

    private Document doc;
    private final String XML_FILE_NAME = "library.xml";
    private final String XSD_RESOURCE_PATH = "/org/example/library_xml/library.xsd";

    private XPath xpath = XPathFactory.newInstance().newXPath();


    public List<Book> loadXML() {
        File xmlFile = new File(XML_FILE_NAME);
        if (!xmlFile.exists()) {
            doc = null;
            return new ArrayList<>();
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(xmlFile);

            return getBooksXPath();

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            System.err.println("Ошибка парсинга или XPath запроса при загрузке: " + e.getMessage());
            doc = null;
            return new ArrayList<>();
        }
    }

    public boolean isXmlValid() {
        File xmlFile = new File(XML_FILE_NAME);
        try {
            URI xsdUri = getClass().getResource(XSD_RESOURCE_PATH).toURI();
            File xsdFile = new File(xsdUri);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (URISyntaxException | NullPointerException e) {
            System.err.println("XSD файл не найден или неверный путь: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("XSD Валидация не пройдена: " + e.getMessage());
            return false;
        }
    }

    public List<Book> getBooksXPath() throws XPathExpressionException {
        List<Book> list = new ArrayList<>();
        if (doc == null) return list;

        String expression = "//book";// цель - найти все элементы 'book'

        NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);

            try {
                String title = xpath.evaluate("title", el);
                String author = xpath.evaluate("author", el);

                Book b = new Book(
                        Integer.parseInt(el.getAttribute("id")),
                        Integer.parseInt(el.getAttribute("copies")),
                        Integer.parseInt(el.getAttribute("available")),
                        title,
                        author,
                        Integer.parseInt(xpath.evaluate("year", el)),
                        Double.parseDouble(xpath.evaluate("price", el)),
                        xpath.evaluate("category", el)
                );
                list.add(b);
            } catch (NumberFormatException e) {
                System.err.println("Ошибка парсинга числовых данных в книге с ID: " + el.getAttribute("id"));
            }
        }
        return list;
    }

    public int getNewBookId() {
        if (doc == null) return 1;
        try {
            List<Book> existingBooks = getBooksXPath();
            return existingBooks.stream()
                    .mapToInt(Book::getId)
                    .max()
                    .orElse(0) + 1;

        } catch (XPathExpressionException e) {
            System.err.println("Ошибка при поиске максимального ID: " + e.getMessage());
            return 1;
        }
    }

    private void saveXML() {
        if (doc == null) return;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(new File(XML_FILE_NAME)));
        } catch (Exception e) {
            System.err.println("Ошибка сохранения XML: " + e.getMessage());
        }
    }

    public void addBook(Book b) {
        if (doc == null) return;

        Element root = doc.getDocumentElement();

        Element book = doc.createElement("book");
        book.setAttribute("id", String.valueOf(b.getId()));
        book.setAttribute("copies", String.valueOf(b.getCopies()));
        book.setAttribute("available", String.valueOf(b.getAvailable()));

        Element title = doc.createElement("title");
        title.setTextContent(b.getTitle());

        Element author = doc.createElement("author");
        author.setTextContent(b.getAuthor());

        Element year = doc.createElement("year");
        year.setTextContent(String.valueOf(b.getYear()));

        Element price = doc.createElement("price");
        price.setTextContent(String.valueOf(b.getPrice()));

        Element category = doc.createElement("category");
        category.setTextContent(b.getCategory());

        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(year);
        book.appendChild(price);
        book.appendChild(category);

        root.appendChild(book);
        saveXML();
    }

    public List<Book> searchBooks(String author, Integer year, String category) {
        List<Book> result = new ArrayList<>();
        if (doc == null) return result;

        try {
            StringBuilder expression = new StringBuilder("//book");
            List<String> conditions = new ArrayList<>();

            if (author != null && !author.isEmpty())
            {
                conditions.add("author = '" + author.replace("'", "''") + "'");
            }
            if (year != null) {
                conditions.add("year = " + year);
            }
            if (category != null && !category.isEmpty()) {
                conditions.add("category = '" + category.replace("'", "''") + "'");
            }

            if (!conditions.isEmpty()) {
                expression.append("[");
                expression.append(String.join(" and ", conditions));
                expression.append("]");
            }

            NodeList nodes = (NodeList) xpath.evaluate(expression.toString(), doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element el = (Element) nodes.item(i);

                String title = xpath.evaluate("title", el);
                String auth = xpath.evaluate("author", el);

                Book b = new Book(
                        Integer.parseInt(el.getAttribute("id")),
                        Integer.parseInt(el.getAttribute("copies")),
                        Integer.parseInt(el.getAttribute("available")),
                        title,
                        auth,
                        Integer.parseInt(xpath.evaluate("year", el)),
                        Double.parseDouble(xpath.evaluate("price", el)),
                        xpath.evaluate("category", el)
                );
                result.add(b);
            }

        } catch (XPathExpressionException | NumberFormatException e) {
            System.err.println("Ошибка при выполнении XPath поиска: " + e.getMessage());
        }
        return result;
    }


    public void changePrice(int id, double newPrice) {
        if (doc == null) return;
        try {
            String expression = String.format("//book[@id='%d']/price", id);

            Node priceNode = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);

            if (priceNode != null) {
                priceNode.setTextContent(String.valueOf(newPrice));
                saveXML();
            } else {
                System.err.println("Книга с ID " + id + " не найдена.");
            }
        } catch (XPathExpressionException e) {
            System.err.println("Ошибка XPath при изменении цены: " + e.getMessage());
        }
    }


    public void giveBook(int id) {
        if (doc == null) return;
        try {
            String expression = String.format("//book[@id='%d']/@available", id);//найти атрибут 'available' книги с заданным ID

            Node availableAttr = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);//узел атрибута available

            if (availableAttr != null) {
                int available = Integer.parseInt(availableAttr.getNodeValue());

                if (available > 0) {
                    availableAttr.setNodeValue(String.valueOf(available - 1));
                    saveXML();
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка выдачи книги");
                        alert.setHeaderText(null);
                        alert.setContentText("Эта книга недоступна для выдачи (доступно 0 экземпляров).");
                        alert.showAndWait();
                    });
                }
            } else {
                Platform.runLater(() -> showAlert("Ошибка", "Книга с ID " + id + " не найдена."));
            }
        } catch (XPathExpressionException | NumberFormatException e) {
            Platform.runLater(() -> showAlert("Ошибка", "Проблема с данными или XPath: " + e.getMessage()));
        }
    }

    private void showAlert(String title, String text) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(text);
        a.showAndWait();
    }
}