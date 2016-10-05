package urim.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import urim.server.model.CurrentUser;
import urim.server.model.UserSetting;
import urim.server.service.UserSettingService;

import java.util.concurrent.atomic.AtomicLong;

@Controller
@Profile("server")
public class HomeController {

    final AtomicLong counter = new AtomicLong();
    private final UserSettingService userSettingService;

    @Autowired
    public HomeController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        try {
            CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserSetting userSetting = userSettingService.getUserSettingByUserId(user.getId()).get();
            switch (userSetting.getTheme()) {
                case ANGULAR:
                    return "index-ng";
                case BOOTSTRAP:
                    return "index";
                default:
                    return "index";
            }
        } catch (Exception e) {
        }
        return "index";
    }

    @RequestMapping(value = "/bs", method = RequestMethod.GET)
    public String indexbs(Model model) {
        return "index";
    }

    @RequestMapping(value = "/ng", method = RequestMethod.GET)
    public String indexng(Model model) {
        return "index-ng";
    }

    @RequestMapping(value = "/md", method = RequestMethod.GET)
    public String indexmd(Model model) {
        return "index-md";
    }

    @RequestMapping(value = "/csrf", method = RequestMethod.GET)
    public String csrf(Model model) {
        return "csrf";
    }

    @RequestMapping(value = "/pong", method = RequestMethod.GET)
    public String pong(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "ping";
    }

    @RequestMapping(value = "/mocha", method = RequestMethod.GET)
    public String mocha(Model model) {
        return "mocha";
    }
}
