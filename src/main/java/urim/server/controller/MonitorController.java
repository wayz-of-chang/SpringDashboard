package urim.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import urim.Message;
import urim.server.model.Dashboard;
import urim.server.model.Monitor;
import urim.server.parameters.MonitorParameters;
import urim.server.service.DashboardService;
import urim.server.service.MonitorService;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Profile("server")
public class MonitorController {

    final AtomicLong counter = new AtomicLong();
    private final MonitorService service;
    private final DashboardService dashboardService;

    @Autowired
    public MonitorController(MonitorService service, DashboardService dashboardService) {
        this.service = service;
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value = "/monitors/create", method = RequestMethod.POST)
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

    @RequestMapping(value = "/monitors/copy", method = RequestMethod.POST)
    public Message copy(@RequestBody MonitorParameters parameters) throws Exception {
        Monitor monitor;
        try {
            monitor = service.copy(parameters.getId());
            dashboardService.addMonitor(parameters.getDashboardId(), monitor.getId());
            return new Message(counter.incrementAndGet(), monitor, "copy monitor", parameters);
        } catch (Exception e) {
            throw new Exception("Could not copy monitor: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/monitors/get", method = RequestMethod.POST)
    public Message get(@RequestBody MonitorParameters parameters) throws Exception {
        Dashboard dashboard;
        try {
            dashboard = dashboardService.getDashboardById(parameters.getDashboardId()).get();
            return new Message(counter.incrementAndGet(), dashboard.getMonitors(), "get monitor", parameters);
        } catch (Exception e) {
            throw new Exception("Could not get monitors: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/monitors/update_settings", method = RequestMethod.POST)
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

    @RequestMapping(value = "/monitors/delete", method = RequestMethod.POST)
    public Message delete(@RequestBody MonitorParameters parameters) throws Exception {
        Monitor monitor;
        Dashboard dashboard;
        try {
            monitor = service.getMonitorById(parameters.getId()).get();
            dashboard = dashboardService.getDashboardById(parameters.getDashboardId()).get();
            Set <Monitor> monitors = dashboard.getMonitors();
            if (monitors.remove(monitor)) {
                dashboardService.save(dashboard);
                service.remove(monitor);
            }
            return new Message(counter.incrementAndGet(), monitor, "delete monitor", parameters);
        } catch (Exception e) {
            throw new Exception("Could not delete monitor: " + e.getMessage());
        }
    }
}