package webservices.server.model;

import javax.persistence.*;

@Entity
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    protected Monitor() {}

    public Monitor(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Monitor[id=%d, name='%s']", id, name);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}