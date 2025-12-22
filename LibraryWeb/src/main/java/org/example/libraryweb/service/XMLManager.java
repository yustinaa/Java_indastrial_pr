package org.example.libraryweb.service;

import org.example.libraryweb.model.Book;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XMLManager {

    private Document doc;
    private final String XML_FILE_NAME = "library.xml";
    private final String XSD_FILE_NAME = "library.xsd";
    private final String USERS_FILE = "users.xml";

    private XPath xpath = XPathFactory.newInstance().newXPath();

    public XMLManager()
    {
        loadXML();
    }

    public List<Book> loadXML()
    {
        File xmlFile = new File(XML_FILE_NAME);
        if (!xmlFile.exists()) {
            System.err.println("Файл не найден: " + xmlFile.getAbsolutePath());
            doc = null;
            return new ArrayList<>();
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(xmlFile);
            return getBooksXPath();
        } catch (Exception e) {
            System.err.println("Ошибка загрузки XML: " + e.getMessage());
            doc = null;
            return new ArrayList<>();
        }
    }

    public boolean isXmlValid()
    {
        File xmlFile = new File(XML_FILE_NAME);
        File xsdFile = new File(XSD_FILE_NAME);

        if (!xmlFile.exists() || !xsdFile.exists()) return false;

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (Exception e) {
            System.err.println("XSD ошибка: " + e.getMessage());
            return false;
        }
    }

    public List<Book> getBooksXPath()
    {
        List<Book> list = new ArrayList<>();
        if (doc == null) loadXML();
        if (doc == null) return list;

        try {
            NodeList nodes = (NodeList) xpath.evaluate("//book", doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element el = (Element) nodes.item(i);
                Book b = new Book(
                        Integer.parseInt(el.getAttribute("id")),
                        Integer.parseInt(el.getAttribute("copies")),
                        Integer.parseInt(el.getAttribute("available")),
                        xpath.evaluate("title", el),
                        xpath.evaluate("author", el),
                        Integer.parseInt(xpath.evaluate("year", el)),
                        Double.parseDouble(xpath.evaluate("price", el)),
                        xpath.evaluate("category", el)
                );
                list.add(b);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public int getNewBookId()
    {
        List<Book> existingBooks = getBooksXPath();
        return existingBooks.stream().mapToInt(Book::getId).max().orElse(0) + 1;
    }

    public void addBook(Book b) throws Exception
    {
        if (doc == null) loadXML();

        Element root = doc.getDocumentElement();
        Element book = doc.createElement("book");
        book.setAttribute("id", String.valueOf(b.getId()));
        book.setAttribute("copies", String.valueOf(b.getCopies()));
        book.setAttribute("available", String.valueOf(b.getAvailable()));

        createChild(book, "title", b.getTitle());
        createChild(book, "author", b.getAuthor());
        createChild(book, "year", String.valueOf(b.getYear()));
        createChild(book, "price", String.valueOf(b.getPrice()));
        createChild(book, "category", b.getCategory());

        root.appendChild(book);
        saveXML();
    }

    private void createChild(Element parent, String name, String value)
    {
        Element node = doc.createElement(name);
        node.setTextContent(value);
        parent.appendChild(node);
    }

    private void saveXML() throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(XML_FILE_NAME)));
    }

    public void changePrice(int id, double newPrice) throws Exception {
        if (doc == null) loadXML();
        Node node = (Node) xpath.evaluate("//book[@id='" + id + "']/price", doc, XPathConstants.NODE);
        if (node != null) {
            node.setTextContent(String.valueOf(newPrice));
            saveXML();
        }
    }

    public void giveBook(int id) throws Exception {
        if (doc == null) loadXML();
        Node availableAttr = (Node) xpath.evaluate("//book[@id='" + id + "']/@available", doc, XPathConstants.NODE);
        if (availableAttr != null) {
            int available = Integer.parseInt(availableAttr.getNodeValue());
            if (available > 0) {
                availableAttr.setNodeValue(String.valueOf(available - 1));
                saveXML();

            }
        }
    }

    public List<Book> searchBooks(String author, Integer year, String category) {
        List<Book> allBooks = getBooksXPath();
        return allBooks.stream()
                .filter(b -> (author == null || author.isEmpty() || b.getAuthor().toLowerCase().contains(author.toLowerCase())))
                .filter(b -> (year == null || b.getYear() == year))
                .filter(b -> (category == null || category.isEmpty() || b.getCategory().toLowerCase().contains(category.toLowerCase())))
                .toList();
    }

    public void registerUser(String username, String password, String role) throws Exception {
        Document userDoc;
        File file = new File(USERS_FILE);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        if (file.exists()) {
            userDoc = db.parse(file);
        } else {
            userDoc = db.newDocument();
            userDoc.appendChild(userDoc.createElement("users"));
        }

        Element root = userDoc.getDocumentElement();
        Element user = userDoc.createElement("user");

        Element uName = userDoc.createElement("username");
        uName.setTextContent(username);

        Element uPass = userDoc.createElement("password");
        uPass.setTextContent(password); // В идеале здесь нужен BCryptPasswordEncoder

        Element uRole = userDoc.createElement("role");
        uRole.setTextContent(role);

        user.appendChild(uName);
        user.appendChild(uPass);
        user.appendChild(uRole);
        root.appendChild(user);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(userDoc), new StreamResult(file));
    }

    public Element findUserByUsername(String username) throws Exception {
        File file = new File(USERS_FILE);
        if (!file.exists()) return null;

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        NodeList list = doc.getElementsByTagName("user");
        for (int i = 0; i < list.getLength(); i++) {
            Element user = (Element) list.item(i);
            if (user.getElementsByTagName("username").item(0).getTextContent().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<String> getBorrowedBookIds(String username) throws Exception {
        List<String> ids = new ArrayList<>();
        Element userEl = findUserByUsername(username);
        if (userEl != null) {
            NodeList nl = userEl.getElementsByTagName("bookId");
            for (int i = 0; i < nl.getLength(); i++) {
                ids.add(nl.item(i).getTextContent());
            }
        }
        return ids;
    }

    public List<Book> getBooksByIds(List<String> ids) {
        List<Book> all = getBooksXPath();
        return all.stream()
                .filter(b -> ids.contains(String.valueOf(b.getId())))
                .toList();
    }


    public void giveBookToUser(int bookId, String username) throws Exception {
        giveBook(bookId);

        File file = new File(USERS_FILE);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document userDoc = db.parse(file);

        XPath xPath = XPathFactory.newInstance().newXPath();
        Element userEl = (Element) xPath.evaluate("//user[username='" + username + "']", userDoc, XPathConstants.NODE);

        if (userEl != null) {
            NodeList borrowedList = userEl.getElementsByTagName("borrowedBooks");
            Element borrowedBooksEl;

            if (borrowedList.getLength() == 0) {
                borrowedBooksEl = userDoc.createElement("borrowedBooks");
                userEl.appendChild(borrowedBooksEl);
            } else {
                borrowedBooksEl = (Element) borrowedList.item(0);
            }

            Element bookIdEl = userDoc.createElement("bookId");
            bookIdEl.setTextContent(String.valueOf(bookId));
            borrowedBooksEl.appendChild(bookIdEl);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(userDoc), new StreamResult(file));
        }
    }

    public List<String> getAllReaders() throws Exception {
        List<String> readers = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return readers;

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        NodeList list = doc.getElementsByTagName("user");
        for (int i = 0; i < list.getLength(); i++) {
            Element user = (Element) list.item(i);
            String role = user.getElementsByTagName("role").item(0).getTextContent();
            if ("READER".equals(role)) {
                readers.add(user.getElementsByTagName("username").item(0).getTextContent());
            }
        }
        return readers;
    }
}