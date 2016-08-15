package urim.server.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicLong;

@Controller
@Profile("server")
public class HomeController {

    final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/csrf")
    public String csrf(Model model) {
        return "csrf";
    }
}
