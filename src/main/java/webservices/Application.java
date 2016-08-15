package webservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import webservices.server.model.Dashboard;
import webservices.server.model.Monitor;
import webservices.server.model.User;
import webservices.server.repository.DashboardRepository;
import webservices.server.repository.MonitorRepository;
import webservices.server.repository.UserRepository;

import java.util.Arrays;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Value("${type}")
    private String type;
    @Value("${server.port}")
    private String port;
    private static final String TYPE_SERVER = "SERVER";
    private static final String TYPE_CLIENT = "CLIENT";

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static void initializeServer() {
        log.info("Initializing Server.");
    }

    private static void initializeClient() {
        log.info("Initializing Client.");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("The following beans are provided by Spring Boot:");
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            log.info(beanName);
        }
    }

    @Bean
    public CommandLineRunner initialize() {
        return (args) -> {
            if (type.equals(TYPE_SERVER)) {
                initializeServer();
            } else {
                if (!type.equals(TYPE_CLIENT)) {
                    log.warn("Invalid type specified: " + type + "; Using " + TYPE_CLIENT + " as default.");
                    type = TYPE_CLIENT;
                }
                initializeClient();
            }
        };
    }

    @Bean
    @Profile("server")
    public CommandLineRunner showPersistedData(UserRepository userRepository, DashboardRepository dashboardRepository, MonitorRepository monitorRepository) {
        return (args) -> {
            log.info("All Users:");
            for (User user : userRepository.findAll()) {
                log.info(user.toString());
            }
            log.info("All Dashboards:");
            for (Dashboard dashboard : dashboardRepository.findAll()) {
                log.info(dashboard.toString());
            }
            log.info("All Monitors:");
            for (Monitor monitor : monitorRepository.findAll()) {
                log.info(monitor.toString());
            }
        };
    }
}