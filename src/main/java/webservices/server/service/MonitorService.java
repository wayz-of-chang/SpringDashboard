package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservices.server.model.Monitor;
import webservices.server.repository.MonitorRepository;

import java.util.Optional;

@Service
public class MonitorService {
    private final MonitorRepository repository;

    @Autowired
    public MonitorService(MonitorRepository repository) {
        this.repository = repository;
    }

    public Optional<Monitor> getMonitorById(long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    public Iterable<Monitor> getAllMonitors() {
        return repository.findAll();
    }

    public Monitor create() {
        Monitor monitor = new Monitor();
        return repository.save(monitor);
    }

    public Monitor save(Monitor monitor) {
        return repository.save(monitor);
    }

    public void remove(Monitor monitor) {
        repository.delete(monitor);
    }

    public void remove(long id) {
        repository.delete(id);
    }

}
