package webservices.server.service;

import org.springframework.data.repository.CrudRepository;
import webservices.server.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUsername(String username);
}
