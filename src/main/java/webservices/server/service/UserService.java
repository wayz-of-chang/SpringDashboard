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
public class UserService {
    private final UserRepository repository;
    private final DashboardRepository dashboardRepository;

    @Autowired
    public UserService(UserRepository repository, DashboardRepository dashboardRepository) {
        this.repository = repository;
        this.dashboardRepository = dashboardRepository;
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

    public User create(String username, String password, String role, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(User.Role.valueOf(role));
        user.setEmail(email);
        return repository.save(user);
    }

    public void addDashboard(long id, long dashboardId) {
        User user = repository.findOne(id);
        Dashboard dashboard = dashboardRepository.findOne(dashboardId);
        user.getDashboards().add(dashboard);
        repository.save(user);
    }
}
