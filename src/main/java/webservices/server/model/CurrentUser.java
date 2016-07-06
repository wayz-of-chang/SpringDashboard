package webservices.server.model;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User user;
    private UserSetting userSetting;

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public CurrentUser(User user, UserSetting userSetting) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
        this.userSetting = userSetting;
    }

    public User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    public User.Role getRole() {
        return user.getRole();
    }

    public UserSetting getUserSetting() { return userSetting; }

    public boolean comparePasswords(String password) {
        return new BCryptPasswordEncoder().matches(password, this.user.getPassword());
    }

    public void setUserSetting(UserSetting userSetting) { this.userSetting = userSetting; }
}
