package webservices.server.repository;

import org.springframework.data.repository.CrudRepository;
import webservices.server.model.Monitor;

public interface MonitorRepository extends CrudRepository<Monitor, Long> {
}
