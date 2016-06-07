package webservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import webservices.Message;
import webservices.server.DashboardParameters;
import webservices.server.MonitorParameters;
import webservices.server.model.Dashboard;
import webservices.server.model.User;
import webservices.server.service.DashboardService;
import webservices.server.service.UserService;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class DashboardController {

    final AtomicLong counter = new AtomicLong();
    private final DashboardService service;
    private final UserService userService;

    @Autowired
    public DashboardController(DashboardService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
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

    @MessageMapping("/stats")
    @SendTo("/topic/greetings")
    public Message statistics(MonitorParameters parameters) throws Exception {
        return new Message(counter.incrementAndGet(), String.format("hello, %s", parameters.getName()), parameters.getName(), parameters);
    }
}
