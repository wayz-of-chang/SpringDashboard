package webservices.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.client.task.ScriptTask;

@RestController
public class ScriptClient extends Client {

    @RequestMapping("/script")
    public Message script(@RequestParam(value="name", defaultValue="") String name) {
        return new ScriptTask().getStats("0", name, counter.incrementAndGet());
    }
}
