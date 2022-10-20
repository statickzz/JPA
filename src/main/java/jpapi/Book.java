package jpapi;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.InheritanceType;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import java.util.*;

/**
 * Domain class representing a book
 */
@Entity
// annotation necessary for all tables to be associated with the bookstore schema
@Table(schema = "BOOKSTORE")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Book {
    @Id
    private String isbn;
    private String title;
    private float price;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
    private Publisher publisher;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
    private Collection<Person> persons;

    // default cst needed by JPA
    public Book() {
    }

    public Book(String isbn, String title, Collection<Person> persons, Publisher publisher, float price) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.publisher = publisher;
        this.persons = persons;

        // add the book to the publisher's list of books
        publisher.addBook(this);

        // add the book to the persons' list of books
        for (Person person : persons) {
            person.addBook(this);
        }
    }

    public Object getId() {
        return this.getIsbn();
    }

    public void setId(Object id) {
        this.isbn = (String) id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public float getPrice() {
        return price;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Book book = (Book) object;
        return Float.compare(book.price, price) == 0 && Objects.equals(isbn, book.isbn) && Objects.equals(title, book.title) && Objects.equals(publisher, book.publisher) && Objects.equals(persons, book.persons);
    }

    @Override
    public String toString() {
        return "Book{" + "isbn='" + isbn + '\'' + ", title='" + title + '\'' + ", price=" + price + ", publisher=" + publisher + ", persons=" + persons + '}';
    }

    @Override
    public int hashCode() {
        int result = isbn.hashCode();
        return result;
    }

    public Collection<Person> getAuthors() {
        return persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
