package jpapi;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.*;

@Entity
@Table(schema = "BOOKSTORE")
class EBook extends Book {
    //@Column
    private String downloadURL;
    //@Column
    private int size;

    public EBook(String string, String string2, List<Person> authors, Publisher aPress, float f, String dl, int sz) {
        super(string, string2, authors, aPress, f);
        this.downloadURL = dl;
        this.size=sz;
    }

    public EBook() {

    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}