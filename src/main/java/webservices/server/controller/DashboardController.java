package webservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.server.DashboardParameters;
import webservices.server.model.Dashboard;
import webservices.server.model.User;
import webservices.server.service.DashboardService;
import webservices.server.service.UserService;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DashboardController {

    final AtomicLong counter = new AtomicLong();
    private final DashboardService service;
    private final UserService userService;

    @Autowired
    public DashboardController(DashboardService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @RequestMapping(value="/dashboards/create", method=RequestMethod.POST)
    public Message create(@RequestBody DashboardParameters parameters) throws Exception {
        Dashboard dashboard;
        User user;
        try {
            dashboard = service.create(parameters.getName());
            userService.addDashboard(parameters.getUserId(), dashboard.getId());
            return new Message(counter.incrementAndGet(), dashboard, "create dashboard", parameters);
        } catch (Exception e) {
            throw new Exception("Could not create user: " + e.getMessage());
        }
    }

    @RequestMapping(value="/dashboards/get", method=RequestMethod.POST)
    public Message get(@RequestBody DashboardParameters parameters) throws Exception {
        User user;
        try {
            user = userService.getUserById(parameters.getUserId()).get();
            return new Message(counter.incrementAndGet(), user.getDashboards(), "get dashboard", parameters);
        } catch (Exception e) {
            throw new Exception("Could not get dashboards: " + e.getMessage());
        }
    }
}
