package urim.client.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import urim.Message;
import urim.client.task.Task;

import java.util.concurrent.atomic.AtomicLong;

@RestController("RestClient")
@Profile("client")
class Client {

    @Value("${name}")
    String name;
    static final String template = "%s";
    final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Message ping(@RequestParam(value="value", defaultValue="ping") String value) {
        return new Message(counter.incrementAndGet(), new Task().getStats(value), this.name, new Parameters("default", this.name, value));
    }
}
