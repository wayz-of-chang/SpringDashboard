package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import webservices.server.model.Dashboard;
import webservices.server.model.User;
import webservices.server.model.UserSetting;
import webservices.server.repository.DashboardRepository;
import webservices.server.repository.UserRepository;
import webservices.server.repository.UserSettingRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserSettingRepository userSettingRepository;
    private final DashboardRepository dashboardRepository;

    @Autowired
    public UserService(UserRepository repository, UserSettingRepository userSettingRepository, DashboardRepository dashboardRepository) {
        this.repository = repository;
        this.userSettingRepository = userSettingRepository;
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

}
