package urim.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urim.server.model.Monitor;
import urim.server.model.MonitorSetting;
import urim.server.parameters.MonitorParameters;
import urim.server.repository.MonitorRepository;
import urim.server.repository.MonitorSettingRepository;

import java.util.EnumSet;
import java.util.HashSet;
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

    // This method is the reference for DashboardService.copyMonitor
    public Monitor copy(long id) {
        Monitor monitor = new Monitor();
        repository.save(monitor);
        Monitor referenceMonitor = getMonitorById(id).get();
        if (referenceMonitor != null) {
            monitor.setName(referenceMonitor.getName());
            Set<MonitorSetting> settings = new HashSet<MonitorSetting>();
            Set<MonitorSetting> referenceSettings = referenceMonitor.getSettings();
            for (MonitorSetting referenceSetting: referenceSettings) {
                MonitorSetting setting = new MonitorSetting(referenceSetting.getKey(), referenceSetting.getValue());
                settingRepository.save(setting);
                settings.add(setting);
            }
            monitor.setSettings(settings);
        }
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
                case SCRIPT:
                    setting.setValue(parameters.getScript());
                    break;
                case PARSER:
                    setting.setValue(determineParser(parameters.getMonitorType(), parameters.getStatType(), parameters.getParser()));
                    break;
                case CHART:
                    setting.setValue(determineChart(parameters.getMonitorType(), parameters.getStatType(), parameters.getChart()));
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
                case SCRIPT:
                    setting = new MonitorSetting(key, parameters.getScript());
                    break;
                case PARSER:
                    setting = new MonitorSetting(key, determineParser(parameters.getMonitorType(), parameters.getStatType(), parameters.getParser()));
                    break;
                case CHART:
                    setting = new MonitorSetting(key, determineChart(parameters.getMonitorType(), parameters.getStatType(), parameters.getChart()));
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

    protected String determineParser(String monitorType, String statType, String parser) {
        String toReturn = "";
        switch (MonitorSetting.Types.valueOf(monitorType)) {
            case system:
                switch (MonitorSetting.Stats.valueOf(statType)) {
                    case cpu:
                        toReturn = "return {value: response.data.cpuUsed, max: response.data.cpuTotal, unit: '%', mediumThreshold: 0.5, highThreshold: 0.9}";
                        break;
                    case ram:
                        toReturn = "var value = response.data.memUsed; var unit = 'MB'; var divider = 1000000; if (value > divider * 1000) { unit = 'GB'; divider = divider * 1000; } if (value > divider * 1000) { unit = 'TB'; divider = divider * 1000; } return {value: Math.round(10 * value / divider) / 10, max: response.data.memTotal / divider, 'unit': unit, mediumThreshold: 0.5, highThreshold: 0.9}";
                        break;
                    case fs:
                        toReturn = "var value = response.data.fsUsed; var unit = 'MB'; var divider = 1000000; if (value > divider * 1000) { unit = 'GB'; divider = divider * 1000; } if (value > divider * 1000) { unit = 'TB'; divider = divider * 1000; } return {value: Math.round(10 * value / divider) / 10, max: response.data.fsTotal / divider, 'unit': unit, mediumThreshold: 0.5, highThreshold: 0.9}";
                        break;
                    default:
                        //TODO: add error-handling for invalid setting
                }
                break;
            default: //script or other
                toReturn = parser;
        }
        return toReturn;
    }

    protected String determineChart(String monitorType, String statType, String chart) {
        String toReturn = "";
        switch (MonitorSetting.Types.valueOf(monitorType)) {
            case system:
                switch (MonitorSetting.Stats.valueOf(statType)) {
                    case cpu:
                        toReturn = "gauge";
                        break;
                    case ram:
                        toReturn = "gauge";
                        break;
                    case fs:
                        toReturn = "gauge";
                        break;
                    default:
                        //TODO: add error-handling for invalid setting
                }
                break;
            default: //script or other
                toReturn = chart;
        }
        return toReturn;
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
