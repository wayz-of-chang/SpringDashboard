package webservices.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import webservices.Message;
import webservices.server.Parameters;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class DashboardController {

    final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @MessageMapping("/stats")
    @SendTo("/topic/greetings")
    public Message statistics(Parameters parameters) throws Exception {
        return new Message(counter.incrementAndGet(), String.format("hello, %s", parameters.getName()), parameters.getName(), parameters);
    }
}
