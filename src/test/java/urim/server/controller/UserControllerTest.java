package urim.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urim.server.BaseTest;
import urim.server.model.CurrentUser;
import urim.server.model.User;
import urim.server.model.UserSetting;
import urim.server.parameters.UserParameters;
import urim.server.service.UserDetailsService;
import urim.server.service.UserService;
import urim.server.service.UserSettingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseTest {

    private MockMvc mvc;
    private ObjectMapper mapper;

    @Mock
    private UserService service;

    @Mock
    private UserSettingService userSettingService;

    @Mock
    private UserDetailsService detailsService;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void getCreate() throws Exception {
        UserParameters params = new UserParameters();
        params.setUsername("user name");
        params.setPassword("password123");
        params.setRole("r_user");
        params.setEmail("abc@def.com");
        when(service.create(anyString(), anyString(), anyString(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) {
                User returnUser = new User();
                returnUser.setUsername(params.getUsername());
                returnUser.setPassword(new BCryptPasswordEncoder().encode(params.getPassword()));
                returnUser.setRole(User.Role.R_USER);
                returnUser.setEmail(params.getEmail());
                return returnUser;
            }
        });
        mvc.perform(post("/users/create").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("username")))
            .andExpect(content().string(containsString("user name")))
            .andExpect(content().string(containsString("password")))
            .andExpect(content().string(containsString("role")))
            .andExpect(content().string(containsString(User.Role.R_USER.name())))
            .andExpect(content().string(containsString("email")))
            .andExpect(content().string(containsString("abc@def.com")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("create user")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")));
    }

    @Test
    public void getLogin() throws Exception {
        UserParameters params = new UserParameters();
        params.setUsername("user name");
        params.setPassword("password123");
        when(detailsService.loadUserByUsername(anyString())).thenAnswer(new Answer<CurrentUser>() {
            @Override
            public CurrentUser answer(InvocationOnMock invocation) {
                User returnUser = new User();
                returnUser.setUsername(params.getUsername());
                returnUser.setPassword(new BCryptPasswordEncoder().encode(params.getPassword()));
                returnUser.setRole(User.Role.R_USER);
                return new CurrentUser(returnUser);
            }
        });
        mvc.perform(post("/users/login").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("username")))
            .andExpect(content().string(containsString("user name")))
            .andExpect(content().string(containsString("password")))
            .andExpect(content().string(containsString("role")))
            .andExpect(content().string(containsString(User.Role.R_USER.name())))
            .andExpect(content().string(containsString("email")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("login user")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")));
    }

    @Test(expected=Exception.class)
    public void getLoginFailed() throws Exception {
        UserParameters params = new UserParameters();
        params.setUsername("user name");
        params.setPassword("password123");
        when(detailsService.loadUserByUsername(anyString())).thenAnswer(new Answer<CurrentUser>() {
            @Override
            public CurrentUser answer(InvocationOnMock invocation) {
                User returnUser = new User();
                returnUser.setUsername(params.getUsername());
                returnUser.setPassword(params.getPassword());
                returnUser.setRole(User.Role.R_USER);
                return new CurrentUser(returnUser);
            }
        });
        mvc.perform(post("/users/login").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void getUpdateSettings() throws Exception {
        HashMap<Long, ArrayList<Long>> monitorOrder = new HashMap<Long, ArrayList<Long>>();
        ArrayList<Long> monitorThree = new ArrayList<Long>();
        monitorThree.add(4L);
        monitorThree.add(5L);
        monitorThree.add(6L);
        ArrayList<Long> monitorSeven = new ArrayList<Long>();
        monitorSeven.add(8L);
        monitorSeven.add(9L);
        monitorSeven.add(10L);
        monitorSeven.add(11L);
        monitorSeven.add(12L);
        monitorSeven.add(13L);
        monitorSeven.add(14L);
        monitorOrder.put(3L, monitorThree);
        monitorOrder.put(7L, monitorSeven);
        UserParameters params = new UserParameters();
        params.setUsername("user name");
        params.setPassword("password123");
        params.setCurrentDashboard(5);
        params.setMonitorOrder(monitorOrder);
        when(userSettingService.getUserSettingByUserId(anyLong())).thenAnswer(new Answer<Optional<UserSetting>>() {
            @Override
            public Optional<UserSetting> answer(InvocationOnMock invocation) {
                HashMap<Long, ArrayList<Long>> monitorOrder = new HashMap<Long, ArrayList<Long>>();
                UserSetting userSetting = new UserSetting(1, 2, UserSetting.Theme.ANGULAR, monitorOrder);
                Optional<UserSetting> returnUserSetting = Optional.of(userSetting);
                return Optional.ofNullable(returnUserSetting.orElse(null));
            }
        });
        when(userSettingService.save(any(UserSetting.class))).thenAnswer(new Answer<UserSetting>() {
            @Override
            public UserSetting answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return (UserSetting) args[0];
            }
        });
        mvc.perform(post("/users/update_settings").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("currentDashboard")))
            .andExpect(content().string(containsString("5")))
            .andExpect(content().string(containsString("theme")))
            .andExpect(content().string(containsString("BOOTSTRAP")))
            .andExpect(content().string(containsString("monitorOrder")))
            .andExpect(content().string(containsString("{\"3\":[4,5,6],\"7\":[8,9,10,11,12,13,14]}}")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("update user settings")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")));
    }

    @Test
    public void getTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, World")));

        mvc.perform(MockMvcRequestBuilders.get("/test?name=user").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, user")));
    }
}