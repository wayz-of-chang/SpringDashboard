package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import webservices.server.model.User;
import webservices.server.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
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
}
