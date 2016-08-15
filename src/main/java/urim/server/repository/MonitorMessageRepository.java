package urim.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import urim.server.model.MonitorMessage;

import java.util.List;
import java.util.Optional;

public interface MonitorMessageRepository extends MongoRepository<MonitorMessage, Long> {
    Optional<MonitorMessage> findById(long id);

    List<MonitorMessage> findByTimestampGreaterThan(long timestamp);

    List<MonitorMessage> findByTimestampLessThan(long timestamp);
}
