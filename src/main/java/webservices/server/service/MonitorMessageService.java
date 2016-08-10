package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservices.Message;
import webservices.server.model.MonitorMessage;
import webservices.server.model.MonitorSetting;
import webservices.server.repository.MonitorMessageRepository;

import java.util.Optional;

@Service
public class MonitorMessageService {
    private final MonitorMessageRepository repository;

    @Autowired
    public MonitorMessageService(MonitorMessageRepository repository) {
        this.repository = repository;
    }

    public Optional<MonitorMessage> getMonitorMessageById(long id) {
        return Optional.ofNullable(repository.findById(id).orElse(null));
    }

    public Iterable<MonitorMessage> getMonitorMessagesBeforeTimestamp(long timestamp) {
        return repository.findByTimestampLessThan(timestamp);
    }

    public Iterable<MonitorMessage> getMonitorMessagesAfterTimestamp(long timestamp) {
        return repository.findByTimestampGreaterThan(timestamp);
    }

    public Iterable<MonitorMessage> getAllMonitorMessages() {
        return repository.findAll();
    }

    public MonitorMessage create(long monitorId, MonitorSetting.Protocols protocol, Message message) {
        MonitorMessage monitorMessage = new MonitorMessage(monitorId, protocol, message);
        return repository.save(monitorMessage);
    }

    public MonitorMessage save(MonitorMessage monitorMessage) {
        return repository.save(monitorMessage);
    }

    public void remove(MonitorMessage monitorMessage) {
        repository.delete(monitorMessage);
    }

    public void remove(long id) {
        repository.delete(id);
    }

}
