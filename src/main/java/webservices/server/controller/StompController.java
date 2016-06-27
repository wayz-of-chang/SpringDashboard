package webservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import webservices.Message;
import webservices.server.model.Dashboard;
import webservices.server.model.Monitor;
import webservices.server.model.MonitorSetting;
import webservices.server.parameters.MonitorParameters;
import webservices.server.service.DashboardService;
import webservices.server.service.MonitorService;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class StompController {

    final AtomicLong counter = new AtomicLong();
    private final MonitorService service;
    private final DashboardService dashboardService;
    private HashMap<Long, ScheduledFuture> schedulers = new HashMap<Long, ScheduledFuture>();
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public StompController(MonitorService service, DashboardService dashboardService) {
        this.service = service;
        this.dashboardService = dashboardService;
    }

    private void getStats(long dashboardId) throws Exception {
        Dashboard dashboard;
        Set<Monitor> monitors;
        HashMap<Long, Message> responses = new HashMap<Long, Message>();
        try {
            dashboard = dashboardService.getDashboardById(dashboardId).get();
            monitors = dashboard.getMonitors();
            RestTemplate restTemplate = new RestTemplate();
            for (Monitor monitor: monitors) {
                HashMap<MonitorSetting.Setting, String> settings = monitor.settingsMap();
                String monitorUrl = "";
                if(settings != null) {
                    monitorUrl = settings.getOrDefault(MonitorSetting.Setting.URL, "");
                }
                if (!monitorUrl.equals("")) {
                    Message response = restTemplate.getForObject(monitorUrl, Message.class);
                    responses.put(monitor.getId(), response);
                    //System.out.println(monitorUrl);
                    //System.out.println(response.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not get stats: " + e.getMessage());
        }
        template.convertAndSend("/results/instant", new Message(counter.incrementAndGet(), responses, String.format("monitor results for %s (%s)", dashboard.getName(), Long.toString(dashboardId)), new MonitorParameters()));
    }


    @MessageMapping("/stats")
    @SendTo("/topic/greetings")
    public Message stats(MonitorParameters parameters) throws Exception {
        return new Message(counter.incrementAndGet(), String.format("hello, %s", parameters.getName()), parameters.getName(), parameters);
    }

    @MessageMapping("/connect")
    @SendTo("/results/instant")
    public Message startMonitoring(MonitorParameters parameters) throws Exception {
        long dashboardId = parameters.getDashboardId();
        if (!schedulers.containsKey(dashboardId)) {
            TaskScheduler scheduler = new ConcurrentTaskScheduler();
            ScheduledFuture future = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        getStats(dashboardId);
                    } catch(Exception e) {
                        System.out.println("Error occurred while getting stats for " + Long.toString(dashboardId) + ": " + e.getMessage());
                    }
                }
            }, 2000);
            schedulers.put(dashboardId, future);
        }
        return new Message(counter.incrementAndGet(), String.format("started monitoring, %s", Long.toString(parameters.getDashboardId())), parameters.getName(), parameters);
    }

    @MessageMapping("/disconnect")
    @SendTo("/results/instant")
    public Message stopMonitoring(MonitorParameters parameters) throws Exception {
        long dashboardId = parameters.getDashboardId();
        if (schedulers.containsKey(dashboardId)) {
            ScheduledFuture future = schedulers.get(dashboardId);
            future.cancel(false);
            schedulers.remove(dashboardId);
        }
        return new Message(counter.incrementAndGet(), String.format("stopped monitoring, %s", Long.toString(parameters.getDashboardId())), parameters.getName(), parameters);
    }
}
