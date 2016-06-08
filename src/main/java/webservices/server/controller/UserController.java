package webservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.server.UserParameters;
import webservices.server.model.CurrentUser;
import webservices.server.model.User;
import webservices.server.service.UserDetailsService;
import webservices.server.service.UserService;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {

    final AtomicLong counter = new AtomicLong();
    private final UserService service;
    private final UserDetailsService detailsService;

    @Autowired
    public UserController(UserService service, UserDetailsService detailsService) {
        this.service = service;
        this.detailsService = detailsService;
    }

    @RequestMapping(value="/users/create", method=RequestMethod.POST)
    public Message create(@RequestBody UserParameters parameters) throws Exception {
        User user;
        try {
            user = service.create(parameters.getUsername(), parameters.getPassword(), parameters.getRole(), parameters.getEmail());
            return new Message(counter.incrementAndGet(), user, "create user", parameters);
        } catch (Exception e) {
            throw new Exception("Could not create user: " + e.getMessage());
        }
    }

    @RequestMapping(value="/users/login", method=RequestMethod.POST)
    public Message login(@RequestBody UserParameters parameters) throws Exception {
        String errorMessage = "";
        try {
            CurrentUser user = detailsService.loadUserByUsername(parameters.getUsername());
            if (!user.comparePasswords(parameters.getPassword())) {
                errorMessage = "Invalid password";
            }
            if (errorMessage.equals("")) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, parameters.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
                return new Message(counter.incrementAndGet(), user, "login user", parameters);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        throw new Exception("Could not log in: " + errorMessage);
    }
}
