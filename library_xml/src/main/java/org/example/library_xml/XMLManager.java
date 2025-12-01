
package org.example.library_xml;

import javafx.scene.control.Alert;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

    public class XMLManager {

        private Document doc;

        public boolean loadXML() {
            try {
                // ВАЛИДАЦИЯ XSD
                File xmlFile = new File(getClass().getResource("/org/example/library_xml/library.xml").toURI());
                File xsdFile = new File(getClass().getResource("/org/example/library_xml/library.xsd").toURI());

                SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                Schema schema = factory.newSchema(xsdFile);
                Validator validator = schema.newValidator();
                validator.validate(new StreamSource(xmlFile));


                // ПАРСЕР
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(xmlFile);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public List<Book> getBooks() {
            List<Book> list = new ArrayList<>();
            NodeList nodes = doc.getElementsByTagName("book");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element el = (Element) nodes.item(i);

                Book b = new Book(
                        Integer.parseInt(el.getAttribute("id")),
                        Integer.parseInt(el.getAttribute("copies")),
                        Integer.parseInt(el.getAttribute("available")),
                        el.getElementsByTagName("title").item(0).getTextContent(),
                        el.getElementsByTagName("author").item(0).getTextContent(),
                        Integer.parseInt(el.getElementsByTagName("year").item(0).getTextContent()),
                        Double.parseDouble(el.getElementsByTagName("price").item(0).getTextContent()),
                        el.getElementsByTagName("category").item(0).getTextContent()
                );

                list.add(b);
            }
            return list;
        }

        private void saveXML() {
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(doc), new StreamResult(new File("library.xml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<Book> searchBooks(String author, Integer year, String category) {
            List<Book> result = new ArrayList<>();
            for (Book b : getBooks()) {
                boolean match = true;
                if (author != null && !author.isEmpty() && !b.author.equalsIgnoreCase(author)) match = false;
                if (year != null && b.year != year) match = false;
                if (category != null && !category.isEmpty() && !b.category.equalsIgnoreCase(category)) match = false;
                if (match) result.add(b);
            }
            return result;
        }


        public void addBook(Book b) {
            Element root = doc.getDocumentElement();

            Element book = doc.createElement("book");
            book.setAttribute("id", String.valueOf(b.id));
            book.setAttribute("copies", String.valueOf(b.copies));
            book.setAttribute("available", String.valueOf(b.available));

            Element title = doc.createElement("title");
            title.setTextContent(b.title);

            Element author = doc.createElement("author");
            author.setTextContent(b.author);

            Element year = doc.createElement("year");
            year.setTextContent(String.valueOf(b.year));

            Element price = doc.createElement("price");
            price.setTextContent(String.valueOf(b.price));

            Element category = doc.createElement("category");
            category.setTextContent(b.category);

            book.appendChild(title);
            book.appendChild(author);
            book.appendChild(year);
            book.appendChild(price);
            book.appendChild(category);

            root.appendChild(book);

            saveXML();
        }

        public void changePrice(int id, double newPrice) {
            NodeList list = doc.getElementsByTagName("book");

            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);

                if (Integer.parseInt(el.getAttribute("id")) == id) {
                    el.getElementsByTagName("price").item(0).setTextContent(String.valueOf(newPrice));
                    saveXML();
                    return;
                }
            }
        }

        public void giveBook(int id) {
            NodeList list = doc.getElementsByTagName("book");

            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);

                if (Integer.parseInt(el.getAttribute("id")) == id) {

                    int available = Integer.parseInt(el.getAttribute("available"));

                    if (available > 0) {
                        el.setAttribute("available", String.valueOf(available - 1));
                        saveXML();
                    }
                    else{

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка выдачи книги");
                        alert.setHeaderText(null);
                        alert.setContentText("Эта книга недоступна для выдачи (доступно 0 экземпляров).");
                        alert.showAndWait();
                        }
                    return;
                }
            }}
    }

