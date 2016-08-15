package urim.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import urim.Message;
import urim.client.task.ScriptTask;

@RestController
public class ScriptClient extends Client {

    @RequestMapping("/script")
    public Message script(@RequestParam(value="name", defaultValue="") String name) {
        return new ScriptTask().getStats("0", name, counter.incrementAndGet());
    }
}
