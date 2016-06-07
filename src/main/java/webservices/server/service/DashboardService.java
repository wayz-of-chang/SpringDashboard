package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import webservices.server.model.Dashboard;
import webservices.server.model.User;
import webservices.server.repository.DashboardRepository;
import webservices.server.repository.UserRepository;

import java.util.Optional;

@Service
public class DashboardService {
    private final DashboardRepository repository;

    @Autowired
    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
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
}
