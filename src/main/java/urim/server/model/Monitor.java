package urim.server.model;

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

    public long getIntervalSeconds(String interval) {
        long toReturn;
        try {
            toReturn = getIntervalSeconds(MonitorSetting.Intervals.valueOf(interval));
        } catch (IllegalArgumentException e) {
            toReturn = 86400000;
        }
        return toReturn;
    }

    public long getIntervalSeconds(MonitorSetting.Intervals interval) {
        long multiplier = 1;
        switch (interval) {
            case d1:
                multiplier *= 12; //1 day = 2 hours * 12
            case h2:
                multiplier *= 2; //2 hours = 1 hour * 2
            case h1:
                multiplier *= 2; //1 hour = 30 minutes * 2
            case m30:
                multiplier *= 2; //30 minutes = 15 minutes * 2
            case m15:
                multiplier *= 3; //15 minutes = 5 minutes * 3
            case m5:
                multiplier *= 5; //5 minutes = 1 minute * 5
            case m1:
                multiplier *= 2; //1 minute = 30 seconds * 2
            case s30:
                multiplier *= 3; //30 seconds = 10 seconds * 3
            case s10:
                multiplier *= 2; //10 seconds = 5 seconds * 2
            case s5:
                multiplier *= 5; //5 seconds = 1 second * 5
                multiplier *= 1000; //milliseconds in one second
        }
        return multiplier;
    }

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
