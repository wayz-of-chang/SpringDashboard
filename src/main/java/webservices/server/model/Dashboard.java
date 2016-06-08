package webservices.server.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "dashboard_monitor",
            joinColumns = @JoinColumn(name = "dashboard_id"),
            inverseJoinColumns = @JoinColumn(name = "monitor_id")
    )
    private Set<Monitor> monitors;

    public Dashboard() {}

    public Dashboard(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Dashboard[id=%d, name='%s']", id, name);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<Monitor> getMonitors() {
        return this.monitors;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMonitors(Set<Monitor> monitors) {
        this.monitors = monitors;
    }

}