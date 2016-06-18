package webservices.server.repository;

import org.springframework.data.repository.CrudRepository;
import webservices.server.model.Monitor;

import java.util.Optional;

public interface MonitorRepository extends CrudRepository<Monitor, Long> {
}
