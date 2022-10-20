package jpapi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BookstoreObjectTest {

    /** Test to check the object graph */
    public void testObjects() {
        Publisher aPress = new Publisher("APRESS");
        Person mKeith = new Person("Mike","Keith");
        Person mSchincariol = new Person("Merrick","Schincariol" );
        List<Person> authors = Arrays.asList(mKeith,mSchincariol);
        Collection<Book> books = new HashSet<>();
        PaperBook JPAPbook = new PaperBook("978-1430219569",
                "JPA 2: Mastering the Java™ Persistence API",
                authors,
                aPress,
                37.49f,
                250
        );
        books.add(JPAPbook);
        // check author relationship
        assert(JPAPbook.getAuthors().containsAll(authors));
        assert(authors.containsAll(JPAPbook.getAuthors()));
        assert(mKeith.getBooks().containsAll(books));
        assert(mSchincariol.getBooks().containsAll(books));
        // check publisher relationship
        assert(JPAPbook.getPublisher() == aPress);
        assert(aPress.getBooks().containsAll(books));
        // the ISBN is different for an ebook
        EBook JPAEbook = new EBook("978-1430219569XX",
                "JPA 2: Mastering the Java™ Persistence API",
                authors,
                aPress,
                37.49f,
                "https://ebook.org/978-1430219569XX",
                125
        );
        books.add(JPAEbook);
        // check author relationship
        assert(JPAEbook.getAuthors().containsAll(authors));
        assert(authors.containsAll(JPAEbook.getAuthors()));
        assert(mKeith.getBooks().containsAll(books));
        assert(mSchincariol.getBooks().containsAll(books));
        // check publisher relationship
        assert(JPAEbook.getPublisher() == aPress);
        assert(aPress.getBooks().containsAll(books));
    }
}
