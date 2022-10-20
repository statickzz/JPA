package jpapi;//package jpapi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestBookstore {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    /**
     * Method (fixture) called before each test
     */
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("JPABookStore");
        em = emf.createEntityManager();
        Publisher aPress = new Publisher("APRESS");
        Person mKeith = new Person("Mike", "Keith");
        Person mSchincariol = new Person("Merrick", "Schincariol");
        List<Person> authors = Arrays.asList(mKeith, mSchincariol);
        PaperBook JPAPbook = new PaperBook("978-1430219569",
                "JPA 2: Mastering the Java™ Persistence API",
                authors,
                aPress,
                37.49f,
                250
        );
        // the ISBN is different for an ebook
        EBook JPAEbook = new EBook("978-1430219569XX",
                "JPA 2: Mastering the Java™ Persistence API",
                authors,
                aPress,
                37.49f,
                "https://ebook.org/978-1430219569XX",
                125
        );
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(JPAPbook);
        em.persist(JPAEbook);
        tx.commit();
    }

    /**
     * Method (fixture) called after each test
     */
    public void tearDown() {
        if (em != null) {
            // cleaning the database
            Query delBookQuery = em.createQuery("DELETE FROM Book b");
            Query delPublisherQuery = em.createQuery("DELETE FROM Publisher p");
            Query delPersonQuery = em.createQuery("DELETE FROM Person p");
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            delBookQuery.executeUpdate();
            delPublisherQuery.executeUpdate();
            delPersonQuery.executeUpdate();
            tx.commit();
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    /**
     * Test inserting JPA books
     */
    public void testInsertBooks() {
        PaperBook pBook = em.find(PaperBook.class, "978-1430219569");
        EBook eBook = em.find(EBook.class, "978-1430219569XX");
        // polymorphic test
        Book book = em.find(Book.class, "978-1430219569");
        // author test
        Iterator<Person> persons = pBook.getAuthors().iterator();
        Person author1 = persons.next();
        Person author2 = persons.next();
        Person testAuthor1 = em.find(Person.class, author1.getSerial());
        Person testAuthor2 = em.find(Person.class, author2.getSerial());
        // publisher test
        Publisher publisher = em.find(Publisher.class, pBook.getPublisher().getName());
        assert (pBook != null);
        assert (eBook != null);
        assert (book != null);
        assert (pBook.getTitle().equals("JPA 2: Mastering the Java™ Persistence API"));
        assert (testAuthor1 != null);
        assert (testAuthor2 != null);
        assert (publisher != null);
    }

    /**
     * Test updating JPA book
     */
    public void testUpdateBook() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        PaperBook bookToUpdate = em.find(PaperBook.class, "978-1430219569");
        Person previousAuthor = bookToUpdate.getAuthors().iterator().next();
        Publisher previousPublisher = bookToUpdate.getPublisher();
        // change the publisher of the book
        Publisher publisher = new Publisher("Manning");
        bookToUpdate.setPublisher(publisher);
        // apply changes
        Book updatedBook = em.merge(bookToUpdate);
        tx.commit();

        // test new values
        assert (publisher.getName().equals(updatedBook.getPublisher().getName()));
        // the previous values are still there
        assert (em.find(Publisher.class, previousPublisher.getName()) != null);
    }

    /**
     * Test deleting JPA book
     */
    public void testDeleteBook() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        PaperBook bookToDelete = em.find(PaperBook.class, "978-1430219569");
        assert (bookToDelete != null);
        Collection<Person> authors = bookToDelete.getAuthors();
        em.remove(bookToDelete);
        Book deletedBook = em.find(PaperBook.class, "978-1430219569");
        tx.commit();

        assert (deletedBook == null);

        // Authors and publisher should not be deleted
        for (Person author : authors) {
            Person testAuthor = em.find(Person.class, author.getSerial());
            assert (testAuthor != null);
        }
        Publisher publisher = em.find(Publisher.class, bookToDelete.getPublisher().getName());
        assert (publisher != null);
    }

    public void testBookInheritance() {
        // polymorphic request
        // we get all the books (ebooks and paper books)
        Query isbnQuery = em.createQuery("SELECT b.isbn FROM Book b");
        List<String> isbns = isbnQuery.getResultList();
        assert ((isbns.get(0).equals("978-1430219569XX") && isbns.get(1).equals("978-1430219569"))
                || (isbns.get(0).equals("978-1430219569") && isbns.get(1).equals("978-1430219569XX")));

        Query authorsQuery = em.createQuery("SELECT p FROM Person p");
        List<Person> allAuthors = authorsQuery.getResultList();
        assert (allAuthors.size() == 2);
    }

    public void testDeletePublisherAndBooks() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Publisher publisher = em.find(Publisher.class, "APRESS");
        em.remove(publisher);
        tx.commit();

        publisher = em.find(Publisher.class, "APRESS");
        // we check the publisher and its books are removed
        assert (publisher == null);

        // If the publisher is deleted then all his books are deleted too
        Query bookQuery = em.createQuery("SELECT b FROM Book b");
        List<String> books = bookQuery.getResultList();
        assert (books.size() == 0);

        // but not the authors...
        Query authorsQuery = em.createQuery("SELECT p FROM Person p");
        List<Person> allAuthors = authorsQuery.getResultList();
        assert (allAuthors.size() == 2);
    }

}
