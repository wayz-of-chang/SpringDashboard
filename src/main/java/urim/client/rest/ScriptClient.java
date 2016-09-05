package urim.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import urim.Message;
import urim.client.task.ScriptTask;

@RestController
public class ScriptClient extends Client {

    @RequestMapping(value = "/script", method = RequestMethod.GET)
    public Message script(@RequestParam(value="name", defaultValue="") String name) {
        return new Message(counter.incrementAndGet(), new ScriptTask().getStats(name), this.name, new Parameters("script", this.name, name));
    }
}
