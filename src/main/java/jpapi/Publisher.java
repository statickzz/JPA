package jpapi;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.*;

@Entity
// annotation necessary for all tables to be associated with the bookstore schema
@Table(schema = "BOOKSTORE")
class Publisher {
    @Id
    private String name;

    @OneToMany(mappedBy = "publisher", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}) // ou ALL?
    private Collection<Book> books;

    // default cst needed by JPA
    public Publisher() {
    }

    public Publisher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
