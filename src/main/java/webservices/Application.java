package webservices;

//imports from the webservice consumer tutorial

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//imports from the web service producer tutorial
//common to all other tutorials
//imports from the spring-boot tutorial

@SpringBootApplication
public class Application implements CommandLineRunner {
    final static String queueName = "dashboard-mq";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @Autowired
    RabbitTemplate rabbitTemplate;

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

        System.out.println("Waiting five seconds...");
        Thread.sleep(5000);
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
        receiver().getLatch().await(10000, TimeUnit.MILLISECONDS);
        context.close();
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

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter
            listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageQueueReceiver receiver() {
        return new MessageQueueReceiver();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}