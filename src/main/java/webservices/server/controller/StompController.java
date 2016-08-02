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
    private HashMap<Long, Long> offsets = new HashMap<Long, Long>();
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public StompController(MonitorService service, DashboardService dashboardService) {
        this.service = service;
        this.dashboardService = dashboardService;
    }

    private void getStats(long dashboardId, RestTemplate restTemplate, SimpMessagingTemplate template, DashboardService dashboardService, long counter) throws Exception {
        Dashboard dashboard;
        Set<Monitor> monitors;
        HashMap<Long, Message> responses = new HashMap<Long, Message>();
        HashMap<String, Message> systemResponses = new HashMap<String, Message>();
        long offset = offsets.get(dashboardId).longValue();
        offsets.put(dashboardId, new Long((offset + 5000) % 86400000));
        try {
            dashboard = dashboardService.getDashboardById(dashboardId).get();
            monitors = dashboard.getMonitors();
            for (Monitor monitor: monitors) {
                HashMap<MonitorSetting.Setting, String> settings = monitor.settingsMap();
                if (settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "").equals("")) {
                    continue;
                }
                if (offset % monitor.getIntervalSeconds(settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "")) != 0) {
                    System.out.println("Skipping monitor: " + monitor.getName() + "(" + monitor.getId() + "), interval: " + settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "") + ", offset: " + offset);
                    continue;
                }
                String monitorUrl = "";
                if(settings != null) {
                    monitorUrl = settings.getOrDefault(MonitorSetting.Setting.URL, "");
                }
                if (monitorUrl != null && !monitorUrl.equals("")) {
                    monitorUrl = String.format("%s/%s?name=%s", monitorUrl, settings.getOrDefault(MonitorSetting.Setting.TYPE, ""), settings.getOrDefault(MonitorSetting.Setting.SCRIPT, ""));
                    boolean systemMonitor = MonitorSetting.Types.valueOf(settings.getOrDefault(MonitorSetting.Setting.TYPE, "")).equals(MonitorSetting.Types.system);
                    if (systemMonitor && systemResponses.get(monitorUrl) != null) {
                        responses.put(monitor.getId(), systemResponses.get(monitorUrl));
                    } else {
                        Message response = restTemplate.getForObject(monitorUrl, Message.class);
                        responses.put(monitor.getId(), response);
                        //System.out.println(monitorUrl);
                        //System.out.println(response.toString());
                        if (systemMonitor) {
                            systemResponses.put(monitorUrl, response);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not get stats: " + e.getMessage());
        }
        template.convertAndSend("/results/" + dashboardId + "/instant", new Message(counter, responses, String.format("monitor results for %s (%s)", dashboard.getName(), Long.toString(dashboardId)), new MonitorParameters()));
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
                        getStats(dashboardId, new RestTemplate(), template, dashboardService, counter.incrementAndGet());
                    } catch(Exception e) {
                        System.out.println("Error occurred while getting stats for " + Long.toString(dashboardId) + ": " + e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            }, 5000);
            schedulers.put(dashboardId, future);
            offsets.put(dashboardId, new Long(0));
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
            offsets.remove(dashboardId);
        }
        return new Message(counter.incrementAndGet(), String.format("stopped monitoring, %s", Long.toString(parameters.getDashboardId())), parameters.getName(), parameters);
    }
}
