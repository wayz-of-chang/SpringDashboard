package webservices.client.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;

import java.util.concurrent.atomic.AtomicLong;

@RestController("RestClient")
@Profile("client")
class Client {

    @Value("${name}")
    String name;
    static final String template = "%s";
    final AtomicLong counter = new AtomicLong();

    @RequestMapping("/ping")
    public Message ping(@RequestParam(value="value", defaultValue="ping") String value) {
        return new Message(counter.incrementAndGet(), String.format(template, value), name, new Parameters(name,
                "ping"));
    }
}
