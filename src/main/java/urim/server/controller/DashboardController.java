package urim.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import urim.Message;
import urim.server.model.Dashboard;
import urim.server.model.User;
import urim.server.model.UserSetting;
import urim.server.parameters.DashboardParameters;
import urim.server.service.DashboardService;
import urim.server.service.UserService;
import urim.server.service.UserSettingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Profile("server")
public class DashboardController {

    final AtomicLong counter = new AtomicLong();
    private final DashboardService service;
    private final UserService userService;
    private final UserSettingService userSettingService;

    @Autowired
    public DashboardController(DashboardService service, UserService userService, UserSettingService userSettingService) {
        this.service = service;
        this.userService = userService;
        this.userSettingService = userSettingService;
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
            throw new Exception("Could not create dashboard: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/dashboards/copy", method = RequestMethod.POST)
    public Message copy(@RequestBody DashboardParameters parameters) throws Exception {
        Dashboard dashboard;
        try {
            UserSetting referenceUserSetting = userService.getUserSettingById(parameters.getUserId()).get();
            HashMap<Long, ArrayList<Long>> referenceMonitorOrders = null;
            ArrayList<Long> referenceMonitorOrder = null;
            ArrayList<Long> newMonitorOrder = new ArrayList<Long>();
            if (referenceUserSetting != null) {
                referenceMonitorOrders = referenceUserSetting.getMonitorOrder();
            }
            if (referenceMonitorOrders != null) {
                referenceMonitorOrder = referenceMonitorOrders.get(parameters.getId());
                for (Long monitorId: referenceMonitorOrder) {
                    newMonitorOrder.add(new Long(monitorId));
                }
            }
            if (referenceMonitorOrder != null) {
                dashboard = service.copy(newMonitorOrder, parameters.getId());
                userService.addDashboard(parameters.getUserId(), dashboard.getId());
                referenceMonitorOrders.put(dashboard.getId(), newMonitorOrder);
                userSettingService.save(referenceUserSetting);
                HashMap<String, Object> toReturn = new HashMap<String, Object>();
                toReturn.put("dashboard", dashboard);
                toReturn.put("monitor_order", newMonitorOrder);
                return new Message(counter.incrementAndGet(), toReturn, "copy dashboard", parameters);
            } else {
                throw new Exception(String.format("Dashboard %s has no defined monitor order", Long.toString(parameters.getId())));
            }
        } catch (Exception e) {
            throw new Exception("Could not copy dashboard: " + e.getMessage());
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

    @RequestMapping(value="/dashboards/edit", method=RequestMethod.POST)
    public Message edit(@RequestBody DashboardParameters parameters) throws Exception {
        Dashboard dashboard;
        User user;
        try {
            dashboard = service.getDashboardById(parameters.getId()).get();
            dashboard.setName(parameters.getName());
            user = userService.getUserById(parameters.getUserId()).get();
            return new Message(counter.incrementAndGet(), service.save(dashboard), "edit dashboard", parameters);
        } catch (Exception e) {
            throw new Exception("Could not update dashboard: " + e.getMessage());
        }
    }

    @RequestMapping(value="/dashboards/delete", method=RequestMethod.POST)
    public Message delete(@RequestBody DashboardParameters parameters) throws Exception {
        Dashboard dashboard;
        User user;
        try {
            dashboard = service.getDashboardById(parameters.getId()).get();
            user = userService.getUserById(parameters.getUserId()).get();
            Set <Dashboard> dashboards = user.getDashboards();
            if (dashboards.remove(dashboard)) {
                userService.save(user);
                service.remove(dashboard);
            }
            return new Message(counter.incrementAndGet(), dashboard, "delete dashboard", parameters);
        } catch (Exception e) {
            throw new Exception("Could not delete dashboard: " + e.getMessage());
        }
    }
}
