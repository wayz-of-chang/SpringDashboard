package urim.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import urim.server.model.UserSetting;

import java.util.Optional;

public interface UserSettingRepository extends MongoRepository<UserSetting, Long> {
    Optional<UserSetting> findByUserId(long userId);
}
