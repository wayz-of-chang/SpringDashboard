package urim.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urim.server.model.UserSetting;
import urim.server.repository.UserSettingRepository;

import java.util.Optional;

@Service
public class UserSettingService {
    private final UserSettingRepository repository;

    @Autowired
    public UserSettingService(UserSettingRepository repository) {
        this.repository = repository;
    }

    public Optional<UserSetting> getUserSettingByUserId(long userId) {
        return Optional.ofNullable(repository.findByUserId(userId).orElse(null));
    }

    public Iterable<UserSetting> getAllUserSettings() {
        return repository.findAll();
    }

    public UserSetting create(long userId) {
        UserSetting userSetting = new UserSetting();
        userSetting.setUserId(userId);
        return repository.save(userSetting);
    }

    public UserSetting save(UserSetting userSetting) {
        return repository.save(userSetting);
    }
}
