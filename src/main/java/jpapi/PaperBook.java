package jpapi;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(schema = "BOOKSTORE")
class PaperBook extends Book {
    //@Column
    private int weight;

    public PaperBook(String string, String string2, List<Person> authors, Publisher aPress, float f, int weight) {
    	super(string, string2, authors, aPress, f);
        this.weight = weight;
    }

    public PaperBook() {

    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}