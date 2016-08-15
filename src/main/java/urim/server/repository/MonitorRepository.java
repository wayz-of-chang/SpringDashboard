package urim.server.repository;

import org.springframework.data.repository.CrudRepository;
import urim.server.model.Monitor;

public interface MonitorRepository extends CrudRepository<Monitor, Long> {
}
