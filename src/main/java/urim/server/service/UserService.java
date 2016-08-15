package urim.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import urim.server.model.*;
import urim.server.repository.*;

import java.util.*;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserSettingRepository userSettingRepository;
    private final DashboardRepository dashboardRepository;
    private final MonitorRepository monitorRepository;
    private final MonitorSettingRepository monitorSettingRepository;

    @Autowired
    public UserService(UserRepository repository, UserSettingRepository userSettingRepository, DashboardRepository dashboardRepository, MonitorRepository monitorRepository, MonitorSettingRepository monitorSettingRepository) {
        this.repository = repository;
        this.userSettingRepository = userSettingRepository;
        this.dashboardRepository = dashboardRepository;
        this.monitorRepository = monitorRepository;
        this.monitorSettingRepository = monitorSettingRepository;
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    public Optional<User> getUserByName(String name) {
        return repository.findByUsername(name);
    }

    public Iterable<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<UserSetting> getUserSettingById(long id) { return userSettingRepository.findByUserId(id); }

    public User create(String username, String password, String role, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(User.Role.valueOf(role));
        user.setEmail(email);
        user = repository.save(user);
        UserSetting userSetting = new UserSetting();
        userSetting.setUserId(user.getId());
        userSettingRepository.save(userSetting);
        return user;
    }

    public void addDashboard(long id, long dashboardId) {
        User user = repository.findOne(id);
        Dashboard dashboard = dashboardRepository.findOne(dashboardId);
        user.getDashboards().add(dashboard);
        repository.save(user);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public HashMap<String, Object> exportUser(User user, UserSetting userSetting) {
        HashMap<String, Object> u = new HashMap<String, Object>();
        u.put("dashboards", setDashboards(user.getDashboards(), userSetting.getMonitorOrder()));
        return u;
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
        for (MonitorSetting.Setting setting: MonitorSetting.Setting.values()) {
            m.put(setting.name(), monitor.settingsMap().getOrDefault(setting, null));
        }
        return m;
    }

    public void importUser(User user, UserSetting userSettings, HashMap<String, Object> u) {
        Set<Dashboard> existingDashboards = user.getDashboards();
        ArrayList<Dashboard> dashboards = importDashboards(userSettings, (ArrayList<HashMap<String, Object>>) u.get("dashboards"));
        for (Dashboard d: dashboards) {
            existingDashboards.add(d);
        }
        user.setDashboards(existingDashboards);
        repository.save(user);
        userSettingRepository.save(userSettings);
    }

    private ArrayList<Dashboard> importDashboards(UserSetting userSettings, ArrayList<HashMap<String, Object>> dashboards) {
        ArrayList<Dashboard> d = new ArrayList<Dashboard>();
        HashMap<Long, ArrayList<Long>> monitorOrder = userSettings.getMonitorOrder();
        for (HashMap<String, Object> dashboard: dashboards) {
            d.add(importDashboard(monitorOrder, dashboard));
        }
        userSettings.setMonitorOrder(monitorOrder);
        return d;
    }

    private Dashboard importDashboard(HashMap<Long, ArrayList<Long>> monitorOrder, HashMap<String, Object> dashboard) {
        System.out.println(dashboard.get("name"));
        Dashboard d = new Dashboard((String) dashboard.get("name"));
        ArrayList<Long> m = new ArrayList<Long>();
        Set<Monitor> existingMonitors = importMonitors(m, (ArrayList<HashMap<String, String>>) dashboard.get("monitors"));
        d.setMonitors(existingMonitors);
        dashboardRepository.save(d);
        monitorOrder.put(d.getId(), m);
        return d;
    }

    private Set<Monitor> importMonitors(ArrayList<Long> monitorOrder, ArrayList<HashMap<String, String>> monitors) {
        HashSet<Monitor> m = new HashSet<Monitor>();
        for (HashMap<String, String> monitor: monitors) {
            Monitor toSave = importMonitor(monitor);
            m.add(toSave);
            monitorOrder.add(toSave.getId());
        }
        return m;
    }

    private Monitor importMonitor(HashMap<String, String> monitor) {
        System.out.println(String.format(" %s", monitor.get("name")));
        Monitor m = new Monitor((String) monitor.get("name"));
        Set<MonitorSetting> settings = new HashSet<MonitorSetting>();
        for (MonitorSetting.Setting setting: MonitorSetting.Setting.values()) {
            System.out.println(String.format("  %s: %s", setting.name(), monitor.get(setting.name())));
            MonitorSetting s = new MonitorSetting(setting, (String) monitor.get(setting.name()));
            monitorSettingRepository.save(s);
            settings.add(s);
        }
        m.setSettings(settings);
        monitorRepository.save(m);
        return m;
    }
}
