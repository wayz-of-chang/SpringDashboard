package webservices;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import webservices.server.model.Dashboard;
import webservices.server.repository.DashboardRepository;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Value("${type}")
    private String type;
    private static final String TYPE_SERVER = "SERVER";
    private static final String TYPE_CLIENT = "CLIENT";

    private static final String queueName = "dashboard-mq";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static void initializeServer() {
        System.out.println("Initializing Server.");
        //ApplicationContext application_context = SpringApplication.run(Application.class, args);

        //System.out.println("The following beans are provided by Spring Boot:");
        //String[] beanNames = application_context.getBeanDefinitionNames();
        //Arrays.sort(beanNames);
        //for (String beanName : beanNames) {
        //    System.out.println(beanName);
        //}
    }

    private static void initializeClient() {
        System.out.println("Initializing Client.");
    }

    @Override
    public void run(String... args) throws Exception {
    }

    @Bean
    public CommandLineRunner initialize() {
        return (args) -> {
            if (type.equals(TYPE_SERVER)) {
                initializeServer();
            } else {
                if (!type.equals(TYPE_CLIENT)) {
                    System.out.println("Invalid type specified: " + type + "; Using " + TYPE_CLIENT + " as default.");
                    type = TYPE_CLIENT;
                }
                initializeClient();
            }
        };
    }

    @Bean
    public CommandLineRunner demoRest() throws Exception {
        return (args) -> {
            RestTemplate restTemplate = new RestTemplate();
            Pong pong = restTemplate.getForObject("http://localhost:8080/test", Pong.class);
            log.info(pong.toString());

            System.out.println("Waiting five seconds...");
            Thread.sleep(5000);
            System.out.println("Sending message...");
            rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ!");
            receiver().getLatch().await(10000, TimeUnit.MILLISECONDS);
            context.close();
        };
    }

    @Bean
    public CommandLineRunner demo(DashboardRepository repository) {
        return (args) -> {
            //repository.save(new Dashboard("Home"));
            //repository.save(new Dashboard("Websites"));

            log.info("Dashboards found with findAll():");
            for (Dashboard dashboard : repository.findAll()) {
                log.info(dashboard.toString());
            }

            /*Dashboard dashboard = repository.findOne(1L);
            log.info("Dashboard found with findOne(1L):");
            log.info(dashboard.toString());

            log.info("Dashboard found with findByName('Home'):");
            for (Dashboard home : repository.findByName("Home")) {
                log.info(home.toString());
            }*/
        };
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
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
        return new MessageQueueReceiver();
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}