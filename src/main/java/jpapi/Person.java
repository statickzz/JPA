package jpapi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.CascadeType;
import java.util.*;

@Entity
// annotation necessary for all tables to be associated with the bookstore schema
@Table(schema = "BOOKSTORE")
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int serial;
    private String fname;
    private String lname;

    @ManyToMany(mappedBy = "persons", cascade = {CascadeType.DETACH, CascadeType.PERSIST})
    private Collection<Book> books;

    public Person() {

    }

    public Person(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if (books == null) {
            books = new ArrayList<Book>();
        }
        books.add(book);
    }
}