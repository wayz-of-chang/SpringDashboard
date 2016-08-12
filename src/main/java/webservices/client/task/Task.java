package webservices.client.task;

import org.springframework.beans.factory.annotation.Value;
import webservices.Message;
import webservices.client.mq.Parameters;

public class Task {

    @Value("${name}")
    String name;
    static final String template = "%s";

    public Message getStats(String key, String value, long counter) {
        return new Message(counter, String.format(template, value), name, new Parameters(key, "ping", name));
    }
}
