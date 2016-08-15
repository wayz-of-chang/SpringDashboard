package urim.server.repository;

import org.springframework.data.repository.CrudRepository;
import urim.server.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String name);
}
