package webservices.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import webservices.Message;
import webservices.server.model.CurrentUser;
import webservices.server.model.User;
import webservices.server.model.UserSetting;
import webservices.server.parameters.UserParameters;
import webservices.server.service.UserDetailsService;
import webservices.server.service.UserService;
import webservices.server.service.UserSettingService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Profile("server")
public class UserController {

    final AtomicLong counter = new AtomicLong();
    private final UserService service;
    private final UserSettingService userSettingService;
    private final UserDetailsService detailsService;

    @Autowired
    public UserController(UserService service, UserSettingService userSettingService, UserDetailsService detailsService) {
        this.service = service;
        this.userSettingService = userSettingService;
        this.detailsService = detailsService;
    }

    @RequestMapping(value="/users/create", method=RequestMethod.POST)
    public Message create(@RequestBody UserParameters parameters) throws Exception {
        User user;
        try {
            user = service.create(parameters.getUsername(), parameters.getPassword(), parameters.getRole(), parameters.getEmail());
            return new Message(counter.incrementAndGet(), user, "create user", parameters);
        } catch (Exception e) {
            throw new Exception("Could not create user: " + e.getMessage());
        }
    }

    @RequestMapping(value="/users/login", method=RequestMethod.POST)
    public Message login(@RequestBody UserParameters parameters) throws Exception {
        String errorMessage = "";
        try {
            CurrentUser user = detailsService.loadUserByUsername(parameters.getUsername());
            if (!user.comparePasswords(parameters.getPassword())) {
                errorMessage = "Invalid password";
            }
            if (errorMessage.equals("")) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, parameters.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
                return new Message(counter.incrementAndGet(), user, "login user", parameters);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        throw new Exception("Could not log in: " + errorMessage);
    }

    @RequestMapping(value="/users/update_settings", method=RequestMethod.POST)
    public Message update_settings(@RequestBody UserParameters parameters) throws Exception {
        String errorMessage;
        try {
            UserSetting userSetting = userSettingService.getUserSettingByUserId(parameters.getUserId()).orElse(new UserSetting(parameters.getUserId()));
            userSetting.setCurrentDashboard(parameters.getCurrentDashboard());
            userSetting.setMonitorOrder(parameters.getMonitorOrder());
            userSetting = userSettingService.save(userSetting);
            return new Message(counter.incrementAndGet(), userSetting, "update user settings", parameters);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        throw new Exception("Could not update user settings: " + errorMessage);
    }

    @RequestMapping(value="/users/get", method=RequestMethod.POST)
    public Message get_user(@RequestBody UserParameters parameters) throws Exception {
        String errorMessage;
        try {
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //CurrentUser user = detailsService.loadUserByUsername(parameters.getUsername());
            return new Message(counter.incrementAndGet(), user, "get current user", parameters);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        throw new Exception("Could not retrieve current user: " + errorMessage);
    }

    @RequestMapping(value="/users/export", method=RequestMethod.POST)
    public Message export_dashboards(@RequestBody UserParameters parameters) throws Exception {
        String errorMessage;
        try {
            CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Message(counter.incrementAndGet(), service.exportUser(user.getUser(), userSettingService.getUserSettingByUserId(user.getUser().getId()).get()), "export user", parameters);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        throw new Exception("Could not export user: " + errorMessage);
    }

    @RequestMapping(value="/users/import", method=RequestMethod.POST)
    public Message import_dashboards(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
        String errorMessage;
        if (!file.isEmpty()) {
            try {
                StringBuilder output = new StringBuilder();
                String line;

                InputStreamReader outputStreamReader = new InputStreamReader(file.getInputStream());
                BufferedReader outputBufferedReader = new BufferedReader(outputStreamReader);

                while ((line = outputBufferedReader.readLine()) != null) {
                    output.append(line);
                }

                ObjectMapper mapper = new ObjectMapper();
                HashMap<String, Object> u = mapper.readValue(output.toString(), new TypeReference<HashMap<String, Object>>() {});
                CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                service.importUser(user.getUser(), user.getUserSetting(), u);

                return new Message(counter.incrementAndGet(), output.toString(), "import user", new UserParameters());
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
            throw new Exception("Could not import: " + errorMessage);
        }
        throw new Exception("Uploaded file was empty");
    }
}
