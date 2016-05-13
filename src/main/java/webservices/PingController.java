package webservices;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class PingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/ping")
    public Ping ping(@RequestParam(value="name", defaultValue="World") String name) {
        return new Ping(counter.incrementAndGet(), String.format(template, name));
    }
}