package webservices.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import webservices.server.model.CurrentUser;
import webservices.server.model.User;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService service;

    @Autowired
    public UserDetailsService(UserService service) {
        this.service = service;
    }

    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.getUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s was not found", username)));
        return new CurrentUser(user);
    }
}
