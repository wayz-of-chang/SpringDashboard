package urim.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urim.server.model.Dashboard;
import urim.server.model.Monitor;
import urim.server.model.MonitorSetting;
import urim.server.repository.DashboardRepository;
import urim.server.repository.MonitorRepository;
import urim.server.repository.MonitorSettingRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DashboardService {
    private final DashboardRepository repository;
    private final MonitorRepository monitorRepository;
    private final MonitorSettingRepository monitorSettingRepository;

    @Autowired
    public DashboardService(DashboardRepository repository, MonitorRepository monitorRepository, MonitorSettingRepository monitorSettingRepository) {
        this.repository = repository;
        this.monitorRepository = monitorRepository;
        this.monitorSettingRepository = monitorSettingRepository;
    }

    public Optional<Dashboard> getDashboardById(long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    public Optional<Dashboard> getDashboardByName(String name, long userId) {
        return repository.findByName(name, userId);
    }

    public Iterable<Dashboard> getAllDashboards() {
        return repository.findAll();
    }

    public Dashboard create(String name) {
        Dashboard dashboard = new Dashboard();
        dashboard.setName(name);
        return repository.save(dashboard);
    }

    public Dashboard copy(ArrayList<Long> monitorOrder, long id) {
        Dashboard dashboard = new Dashboard();
        Dashboard referenceDashboard = getDashboardById(id).get();
        if (referenceDashboard != null) {
            dashboard.setName(referenceDashboard.getName());
            Set<Monitor> monitors = new HashSet<Monitor>();
            for (int i = 0; i < monitorOrder.size(); i++) {
                Monitor m = copyMonitor(monitorOrder.get(i));
                monitors.add(m);
                monitorOrder.set(i, m.getId());
            }
            dashboard.setMonitors(monitors);
            repository.save(dashboard);
        }
        return dashboard;
    }

    // This method is almost a copy of MonitorService.copy
    private Monitor copyMonitor(long id) {
        Monitor monitor = new Monitor();
        Monitor referenceMonitor = monitorRepository.findOne(id);
        if (referenceMonitor != null) {
            monitor.setName(referenceMonitor.getName());
            Set<MonitorSetting> settings = new HashSet<MonitorSetting>();
            Set<MonitorSetting> referenceSettings = referenceMonitor.getSettings();
            for (MonitorSetting referenceSetting: referenceSettings) {
                MonitorSetting setting = new MonitorSetting(referenceSetting.getKey(), referenceSetting.getValue());
                monitorSettingRepository.save(setting);
                settings.add(setting);
            }
            monitor.setSettings(settings);
            monitorRepository.save(monitor);
        }
        return monitor;
    }

    public void addMonitor(long id, long monitorId) {
        Dashboard dashboard = repository.findOne(id);
        Monitor monitor = monitorRepository.findOne(monitorId);
        dashboard.getMonitors().add(monitor);
        repository.save(dashboard);
    }

    public Dashboard save(Dashboard dashboard) {
        return repository.save(dashboard);
    }

    public void remove(Dashboard dashboard) {
        repository.delete(dashboard);
    }

    public void remove(long id) {
        repository.delete(id);
    }

}
