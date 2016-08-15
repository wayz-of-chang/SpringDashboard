package urim.server.repository;

import org.springframework.data.repository.CrudRepository;
import urim.server.model.Dashboard;

import java.util.Optional;

public interface DashboardRepository extends CrudRepository<Dashboard, Long> {
    Optional<Dashboard> findByName(String name, long userId);
}
