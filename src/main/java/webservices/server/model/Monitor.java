package webservices.server.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Set;

@Entity
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "name", nullable = true)
    private String name;

    private HashMap<MonitorSetting.Setting, String> settingsMap;

    public Monitor() {}

    public Monitor(String name) {
        this.name = name;
    }

    @OneToMany(orphanRemoval = true)
    @JoinTable(
            name = "monitor_monitor_setting",
            joinColumns = @JoinColumn(name = "monitor_id"),
            inverseJoinColumns = @JoinColumn(name = "monitor_setting_id")
    )
    private Set<MonitorSetting> settings;

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

    public Set<MonitorSetting> getSettings() { return this.settings; }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSettings(Set<MonitorSetting> settings) { this.settings = settings; }

    public HashMap<MonitorSetting.Setting, String> settingsMap() {
        if (settingsMap == null) {
            settingsMap = new HashMap<MonitorSetting.Setting, String>();
            Set<MonitorSetting> settings = getSettings();
            for (MonitorSetting setting: settings) {
                settingsMap.put(setting.getKey(), setting.getValue());
            }
        }
        return settingsMap;
    }

}
