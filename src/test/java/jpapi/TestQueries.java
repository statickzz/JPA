package jpapi;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

public class TestQueries {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPABookStore");
    ;
    private static EntityManager em = emf.createEntityManager();

    public void setUp() {
        emf = Persistence.createEntityManagerFactory("JPABookStore");
        em = emf.createEntityManager();
        Publisher aPress = new Publisher("APRESS");
        Person mKeith = new Person("Mike", "Keith");
        Person mSchincariol = new Person("Merrick", "Schincariol");
        List<Person> authors = Arrays.asList(mKeith, mSchincariol);
        List<Person> authors2 = Arrays.asList(mKeith);
        PaperBook JPAPbook = new PaperBook("978-1430219569",
                "JPA 2: Mastering the Java™ Persistence API",
                authors2,
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
/*
    public void testQuery1() {
        // give Person objects that are authors of at least one book

        String qStr = "select p.fname from Person p where exists (select b.isbn from Book b where b.persons = p.serial)";
        TypedQuery<Person> tq = em.createQuery(qStr, Person.class);
        List<Person> list = tq.getResultList();

        // with criteria API
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> p = cq.from(Person.class);
        Join<Person, Book> b = p.join("persons");
        cq.select(p).where(cb.exists((Subquery<?>) cb.createQuery(Book.class).where(cb.equal(b.get("persons"), p.get("serial")))));

        TypedQuery<Person> tq2 = em.createQuery(cq);
        List<Person> list2 = tq2.getResultList();
    }
*/
    public void testQuery2() {
        // give the books that have at least 2 authors
        String qStr = "select b from Book b where size(b.persons) > 1";
        TypedQuery<Book> tq = em.createQuery(qStr, Book.class);
        List<Book> list = tq.getResultList();

        // assertion if list index 0 is 978-1430219569XX
        assert(list.get(0).getIsbn().equals("978-1430219569XX") && list.size() == 1);


        //with criteria API
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> b = cq.from(Book.class);
        cq.select(b).where(cb.gt(cb.size(b.get("persons")), 1));

        TypedQuery<Book> tq2 = em.createQuery(cq);
        List<Book> list2 = tq2.getResultList();
    }
/*
    public void testQuery3() {
        // give, for each Person object, the average price of the books of which it is the author.
        String qStr = "select p.fname, avg(b.price) from Person p, Book b where b.persons = p.serial group by p.fname";
        TypedQuery<Object[]> tq = em.createQuery(qStr, Object[].class);
        List<Object[]> list = tq.getResultList();

        //with criteria API
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Person> p = cq.from(Person.class);
        Join<Person, Book> b = p.join("persons");
        cq.multiselect(p.get("fname"), cb.avg(b.get("price"))).groupBy(p.get("fname"));

        TypedQuery<Object[]> tq2 = em.createQuery(cq);
        List<Object[]> list2 = tq2.getResultList();
    }
    */
}
