package urim.server.repository;

import org.springframework.data.repository.CrudRepository;
import urim.server.model.MonitorSetting;

public interface MonitorSettingRepository extends CrudRepository<MonitorSetting, Long> {
}
