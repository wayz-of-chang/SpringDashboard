package webservices.server.repository;

import org.springframework.data.repository.CrudRepository;
import webservices.server.model.MonitorSetting;

public interface MonitorSettingRepository extends CrudRepository<MonitorSetting, Long> {
}
