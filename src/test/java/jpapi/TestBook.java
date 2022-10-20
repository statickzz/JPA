package jpapi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestBook {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPABookStore");
    ;
    private static EntityManager em = emf.createEntityManager();

    /**
     * Method (fixture) called before each test
     */
    public void setUp() {

    }

    /**
     * Method (fixture) called after each test
     */
    public void tearDown() {
        // cleaning the database
        Query delBookQuery = em.createQuery("DELETE FROM Book b");
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        delBookQuery.executeUpdate();
        tx.commit();
    }

    /* la fonction de test a ete supprime, la classe book etant devenue abstraite */
}
