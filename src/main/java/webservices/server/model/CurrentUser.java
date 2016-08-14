package webservices.server.model;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User user;
    private UserSetting userSetting;

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public CurrentUser(User user, UserSetting userSetting) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
        this.userSetting = userSetting;
    }

    public User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    public User.Role getRole() {
        return user.getRole();
    }

    public UserSetting getUserSetting() { return userSetting; }

    public boolean comparePasswords(String password) {
        return new BCryptPasswordEncoder().matches(password, this.user.getPassword());
    }

    public void setUserSetting(UserSetting userSetting) { this.userSetting = userSetting; }

    public HashMap<String, Object> exportUser() {
        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("dashboards", setDashboards(getUser().getDashboards(), getUserSetting().getMonitorOrder()));
        return user;
    }

    private ArrayList<HashMap<String, Object>> setDashboards(Set<Dashboard> dashboards, HashMap<Long, ArrayList<Long>> monitorOrder) {
        ArrayList<HashMap<String, Object>> d = new ArrayList<HashMap<String, Object>>();
        HashMap<Long, Dashboard> dashboardMap = new HashMap<Long, Dashboard>();
        for (Dashboard dash: dashboards) {
            dashboardMap.put(dash.getId(), dash);
        }
        for (Long dashboardId: monitorOrder.keySet()) {
            if (dashboardMap.get(dashboardId) != null) {
                d.add(reformatDashboard(dashboardMap.get(dashboardId), monitorOrder.get(dashboardId)));
            }
        }
        return d;
    }

    private HashMap<String, Object> reformatDashboard(Dashboard dashboard, ArrayList<Long> monitorOrder) {
        HashMap<String, Object> d = new HashMap<String, Object>();
        d.put("name", dashboard.getName());
        d.put("monitors", setMonitors(dashboard.getMonitors(), monitorOrder));
        return d;
    }

    private ArrayList<HashMap<String, String>> setMonitors(Set<Monitor> monitors, ArrayList<Long> monitorOrder) {
        ArrayList<HashMap<String, String>> m = new ArrayList<HashMap<String, String>>();
        HashMap<Long, Monitor> monitorMap = new HashMap<Long, Monitor>();
        for (Monitor mon: monitors) {
            monitorMap.put(mon.getId(), mon);
        }
        for (Long monitorId: monitorOrder) {
            if (monitorMap.get(monitorId) != null) {
                m.add(reformatMonitor(monitorMap.get(monitorId)));
            }
        }
        return m;
    }

    private HashMap<String, String> reformatMonitor(Monitor monitor) {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("name", monitor.getName());
        for(MonitorSetting.Setting setting: MonitorSetting.Setting.values()) {
            m.put(setting.name(), monitor.settingsMap().getOrDefault(setting, null));
        }
        return m;
    }

    public void importUser(HashMap<String, Object> user) {

    }
}
