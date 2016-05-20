package webservices;

//imports from the webservice consumer tutorial

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

//imports from the web service producer tutorial
//common to all other tutorials
//imports from the spring-boot tutorial

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext application_context = SpringApplication.run(Application.class, args);

        System.out.println("The following beans are provided by Spring Boot:");
        String[] beanNames = application_context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Pong pong = restTemplate.getForObject("http://localhost:8080/ping", Pong.class);
        log.info(pong.toString());
    }

    @Bean
    public CommandLineRunner demo(DashboardRepository repository) {
        return (args) -> {
            repository.save(new Dashboard("Home"));
            repository.save(new Dashboard("Websites"));

            log.info("Dashboards found with findAll():");
            for (Dashboard dashboard : repository.findAll()) {
                log.info(dashboard.toString());
            }

            Dashboard dashboard = repository.findOne(1L);
            log.info("Dashboard found with findOne(1L):");
            log.info(dashboard.toString());

            log.info("Dashboard found with findByName('Home'):");
            for (Dashboard home : repository.findByName("Home")) {
                log.info(home.toString());
            }
        };
    }
}