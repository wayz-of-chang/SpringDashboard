package webservices.server.controller;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
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
import webservices.server.service.MonitorMessageService;
import webservices.server.service.MonitorService;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@Profile("server")
public class StompController {

    final AtomicLong counter = new AtomicLong();
    private final MonitorService service;
    private final DashboardService dashboardService;
    private final MonitorMessageService monitorMessageService;
    private HashMap<Long, ScheduledFuture> schedulers = new HashMap<Long, ScheduledFuture>();
    private HashMap<Long, Long> offsets = new HashMap<Long, Long>();

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public StompController(MonitorService service, DashboardService dashboardService, MonitorMessageService monitorMessageService) {
        this.service = service;
        this.dashboardService = dashboardService;
        this.monitorMessageService = monitorMessageService;
    }

    private void getRestStats(long dashboardId, RestTemplate restTemplate, SimpMessagingTemplate template, DashboardService dashboardService, long counter, long offset) throws Exception {
        Dashboard dashboard;
        Set<Monitor> monitors;
        HashMap<Long, Message> responses = new HashMap<Long, Message>();
        HashMap<String, Message> systemResponses = new HashMap<String, Message>();
        try {
            dashboard = dashboardService.getDashboardById(dashboardId).get();
            monitors = dashboard.getMonitors();
            for (Monitor monitor: monitors) {
                HashMap<MonitorSetting.Setting, String> settings = monitor.settingsMap();
                if (settings.getOrDefault(MonitorSetting.Setting.PROTOCOL, "").equals("mq")) {
                    continue;
                }
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

    private void getMqStats(long dashboardId, RestTemplate restTemplate, SimpMessagingTemplate template, DashboardService dashboardService, long counter, long offset) throws Exception {
        Dashboard dashboard;
        Set<Monitor> monitors;
        HashMap<Long, Message> responses = new HashMap<Long, Message>();
        HashMap<String, Message> systemResponses = new HashMap<String, Message>();
        try {
            dashboard = dashboardService.getDashboardById(dashboardId).get();
            monitors = dashboard.getMonitors();
            for (Monitor monitor: monitors) {
                HashMap<MonitorSetting.Setting, String> settings = monitor.settingsMap();
                if (settings.getOrDefault(MonitorSetting.Setting.PROTOCOL, "").equals("rest")) {
                    continue;
                }
                if (settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "").equals("")) {
                    continue;
                }
                if (offset % (monitor.getIntervalSeconds(settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "")) * 4) == 0) {
                    System.out.println("Keeping monitor alive: " + monitor.getName() + "(" + monitor.getId() + "), interval: " + settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "") + ", offset: " + offset);
                    String monitorUrl = "";
                    if(settings != null) {
                        monitorUrl = settings.getOrDefault(MonitorSetting.Setting.URL, "");
                    }
                    if (monitorUrl != null && !monitorUrl.equals("")) {
                        monitorUrl = String.format("%s/start?key=%s&interval=%s&name=%s", monitorUrl, monitor.getId(), monitor.getIntervalSeconds(settings.getOrDefault(MonitorSetting.Setting.INTERVAL, "")), settings.getOrDefault(MonitorSetting.Setting.TYPE, ""), settings.getOrDefault(MonitorSetting.Setting.SCRIPT, ""));
                        Message response = restTemplate.getForObject(monitorUrl, Message.class);
                        //responses.put(monitor.getId(), response);
                    }
                }
                //grab all responses that are less than 5 seconds old; parse them for the monitors with ids in this dashboard, and return those results
                //receiver().getLatch().await(10, TimeUnit.MILLISECONDS);
                //Message response = restTemplate.getForObject(monitorUrl, Message.class);
                //responses.put(monitor.getId(), response);
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
                        long offset = offsets.get(dashboardId).longValue();
                        offsets.put(dashboardId, new Long((offset + 5000) % 86400000));
                        getRestStats(dashboardId, new RestTemplate(), template, dashboardService, counter.incrementAndGet(), offset);
                        getMqStats(dashboardId, new RestTemplate(), template, dashboardService, counter.incrementAndGet(), offset);
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

    @Bean
    public Queue queue() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("x-message-ttl", 15000);
        return new Queue(queueName, false, false, false, args);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter
            listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageQueueReceiver receiver() {
        return new MessageQueueReceiver(monitorMessageService);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}

class MessageQueueReceiver {

    private final MonitorMessageService service;

    private CountDownLatch latch = new CountDownLatch(1);

    public MessageQueueReceiver(MonitorMessageService service) {
        this.service = service;
    }

    public void receiveMessage(Message message) {
        System.out.println("Received <" + message.getId() + ">");
        service.create(Long.parseLong(message.getParameters().getName()), MonitorSetting.Protocols.mq, message);
        //latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
