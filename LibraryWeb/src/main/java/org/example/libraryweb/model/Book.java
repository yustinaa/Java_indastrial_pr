package org.example.libraryweb.model;

public class Book {
        public int id;
        public int copies;
        public int available;
        public String title;
        public String author;
        public int year;
        public double price;
        public String category;

        public Book(int id, int copies, int available, String title, String author,
                    int year, double price, String category) {

            this.id = id;
            this.copies = copies;
            this.available = available;
            this.title = title;
            this.author = author;
            this.year = year;
            this.price = price;
            this.category = category;
        }

    public int getId() { return id; }
    public int getCopies() { return copies; }
    public int getAvailable() { return available; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public void setPrice(double price) { this.price = price; }
    public void setAvailable(int available) { this.available = available; }
}
