package urim.client.task;

import org.springframework.beans.factory.annotation.Value;
import urim.Message;
import urim.client.mq.Parameters;

public class Task {

    @Value("${name}")
    String name;
    static final String template = "%s";

    public Message getStats(String key, String value, long counter) {
        return new Message(counter, String.format(template, value), name, new Parameters(key, "ping", name));
    }
}
