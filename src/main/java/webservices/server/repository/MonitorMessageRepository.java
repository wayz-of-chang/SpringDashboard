package webservices.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import webservices.server.model.MonitorMessage;

import java.util.List;
import java.util.Optional;

public interface MonitorMessageRepository extends MongoRepository<MonitorMessage, Long> {
    Optional<MonitorMessage> findById(long id);

    List<MonitorMessage> findByTimestampGreaterThan(long timestamp);

    List<MonitorMessage> findByTimestampLessThan(long timestamp);
}
