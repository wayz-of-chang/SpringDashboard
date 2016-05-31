package webservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.server.UserParameters;
import webservices.server.model.User;
import webservices.server.service.UserService;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {

    final AtomicLong counter = new AtomicLong();
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping(value="/users/create", method=RequestMethod.POST)
    public Message create(@RequestBody UserParameters parameters) throws Exception {
        System.out.println("Username: " + parameters.getUsername());
        System.out.println("Role: " + parameters.getRole());
        User user = service.create(parameters.getUsername(), parameters.getPassword(), parameters.getRole(), parameters.getEmail());
        return new Message(counter.incrementAndGet(), user, "create user", parameters);
    }
}
