package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservices.server.model.Monitor;
import webservices.server.model.MonitorSetting;
import webservices.server.parameters.MonitorParameters;
import webservices.server.repository.MonitorRepository;
import webservices.server.repository.MonitorSettingRepository;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MonitorService {
    private final MonitorRepository repository;
    private final MonitorSettingRepository settingRepository;

    @Autowired
    public MonitorService(MonitorRepository repository, MonitorSettingRepository settingRespository) {
        this.repository = repository;
        this.settingRepository = settingRespository;
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

    public void setSettings(Monitor monitor, MonitorParameters parameters) {
        Set<MonitorSetting> settings = monitor.getSettings();
        EnumSet<MonitorSetting.Setting> missingSettings = EnumSet.allOf(MonitorSetting.Setting.class);

        for (MonitorSetting setting: settings) {
            MonitorSetting.Setting key = setting.getKey();
            missingSettings.remove(key);
            switch (key) {
                case URL:
                    setting.setValue(parameters.getUrl());
                    break;
                case TYPE:
                    setting.setValue(parameters.getMonitorType());
                    break;
                case STAT:
                    setting.setValue(parameters.getStatType());
                    break;
                case PARSER:
                    setting.setValue(parameters.getParser());
                    break;
                case CHART:
                    setting.setValue(parameters.getChart());
                    break;
                case PROTOCOL:
                    setting.setValue(parameters.getProtocol());
                    break;
                case INTERVAL:
                    setting.setValue(parameters.getInterval());
                    break;
                default:
                    //TODO: add error-handling for invalid setting
            }
        }

        for (MonitorSetting.Setting key: missingSettings) {
            MonitorSetting setting = new MonitorSetting();
            switch (key) {
                case URL:
                    setting = new MonitorSetting(key, parameters.getUrl());
                    break;
                case TYPE:
                    setting = new MonitorSetting(key, parameters.getMonitorType());
                    break;
                case STAT:
                    setting = new MonitorSetting(key, parameters.getStatType());
                    break;
                case PARSER:
                    setting = new MonitorSetting(key, parameters.getParser());
                    break;
                case CHART:
                    setting = new MonitorSetting(key, parameters.getChart());
                    break;
                case PROTOCOL:
                    setting = new MonitorSetting(key, parameters.getProtocol());
                    break;
                case INTERVAL:
                    setting = new MonitorSetting(key, parameters.getInterval());
                    break;
                default:
                    //TODO: add error-handling for invalid setting
            }
            if (setting.getKey() != null) {
                settingRepository.save(setting);
                settings.add(setting);
            }
        }
        repository.save(monitor);
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
