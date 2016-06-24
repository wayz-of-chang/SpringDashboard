package webservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.server.parameters.MonitorParameters;
import webservices.server.model.Dashboard;
import webservices.server.model.Monitor;
import webservices.server.service.DashboardService;
import webservices.server.service.MonitorService;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MonitorController {

    final AtomicLong counter = new AtomicLong();
    private final MonitorService service;
    private final DashboardService dashboardService;

    @Autowired
    public MonitorController(MonitorService service, DashboardService dashboardService) {
        this.service = service;
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value="/monitors/create", method=RequestMethod.POST)
    public Message create(@RequestBody MonitorParameters parameters) throws Exception {
        Monitor monitor;
        try {
            monitor = service.create();
            dashboardService.addMonitor(parameters.getDashboardId(), monitor.getId());
            return new Message(counter.incrementAndGet(), monitor, "create monitor", parameters);
        } catch (Exception e) {
            throw new Exception("Could not create monitor: " + e.getMessage());
        }
    }

    @RequestMapping(value="/monitors/get", method=RequestMethod.POST)
    public Message get(@RequestBody MonitorParameters parameters) throws Exception {
        Dashboard dashboard;
        try {
            dashboard = dashboardService.getDashboardById(parameters.getDashboardId()).get();
            return new Message(counter.incrementAndGet(), dashboard.getMonitors(), "get monitor", parameters);
        } catch (Exception e) {
            throw new Exception("Could not get monitors: " + e.getMessage());
        }
    }

    @RequestMapping(value="/monitors/update_settings", method=RequestMethod.POST)
    public Message update(@RequestBody MonitorParameters parameters) throws Exception {
        Monitor monitor;
        try {
            monitor = service.getMonitorById(parameters.getId()).get();
            monitor.setName(parameters.getName());
            service.setSettings(monitor, parameters);
            service.save(monitor);
            return new Message(counter.incrementAndGet(), monitor, "update monitor", parameters);
        } catch (Exception e) {
            throw new Exception("Could not get monitors: " + e.getMessage());
        }
    }

}
