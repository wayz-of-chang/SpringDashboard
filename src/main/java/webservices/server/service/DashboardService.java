package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservices.server.model.Dashboard;
import webservices.server.model.Monitor;
import webservices.server.repository.DashboardRepository;
import webservices.server.repository.MonitorRepository;

import java.util.Optional;

@Service
public class DashboardService {
    private final DashboardRepository repository;
    private final MonitorRepository monitorRepository;

    @Autowired
    public DashboardService(DashboardRepository repository, MonitorRepository monitorRepository) {
        this.repository = repository;
        this.monitorRepository = monitorRepository;
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
