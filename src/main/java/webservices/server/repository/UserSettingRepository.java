package webservices.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import webservices.server.model.UserSetting;

import java.util.Optional;

public interface UserSettingRepository extends MongoRepository<UserSetting, Long> {
    Optional<UserSetting> findByUserId(long userId);
}
