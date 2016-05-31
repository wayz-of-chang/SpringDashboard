package webservices.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    protected Dashboard() {}

    public Dashboard(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Dashboard[id=%d, name='%s']", id, name);
    }
}
