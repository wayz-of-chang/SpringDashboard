package webservices.server.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "dashboard_monitor",
            joinColumns = @JoinColumn(name = "monitor_id"),
            inverseJoinColumns = @JoinColumn(name = "dashboard_id")
    )
    private Dashboard dashboard;

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

    public Dashboard getDashboard() {
        return this.dashboard;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

}
