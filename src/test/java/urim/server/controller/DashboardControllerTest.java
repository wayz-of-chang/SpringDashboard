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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urim.server.BaseTest;
import urim.server.model.Dashboard;
import urim.server.model.Monitor;
import urim.server.model.MonitorSetting;
import urim.server.model.User;
import urim.server.parameters.DashboardParameters;
import urim.server.service.DashboardService;
import urim.server.service.UserService;
import urim.server.service.UserSettingService;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DashboardControllerTest extends BaseTest {

    private MockMvc mvc;
    private ObjectMapper mapper;

    @Mock
    private DashboardService service;

    @Mock
    private UserService userService;

    @Mock
    private UserSettingService userSettingService;

    @InjectMocks
    private DashboardController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void getCreate() throws Exception {
        DashboardParameters params = new DashboardParameters();
        params.setName("Example");
        when(service.create(anyString())).thenAnswer(new Answer<Dashboard>() {
            @Override
            public Dashboard answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Dashboard returnDashboard = new Dashboard();
                returnDashboard.setName((String) args[0]);
                return returnDashboard;
            }
        });
        mvc.perform(post("/dashboards/create").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("Example")))
            .andExpect(content().string(containsString("monitors")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("create dashboard")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")));
    }

    @Test
    public void getCopy() throws Exception {
    }

    @Test
    public void getGet() throws Exception {
        DashboardParameters params = new DashboardParameters();
        params.setUserId(10L);
        when(userService.getUserById(anyLong())).thenAnswer(new Answer<Optional<User>>() {
            @Override
            public Optional<User> answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                User returnUser = new User();
                returnUser.setUsername("user name");
                returnUser.setPassword("password123");
                returnUser.setRole(User.Role.R_USER);
                returnUser.setEmail("abc@def.com");
                HashSet<Dashboard> dashboards = new HashSet<Dashboard>();
                Dashboard example = new Dashboard("Example");
                HashSet<Monitor> monitors = new HashSet<Monitor>();
                Monitor monitorOne = new Monitor("aaa");
                HashSet<MonitorSetting> settings = new HashSet<MonitorSetting>();
                MonitorSetting settingUrl = new MonitorSetting(MonitorSetting.Setting.URL, "http://aaa.com:8080");
                settings.add(settingUrl);
                MonitorSetting settingType = new MonitorSetting(MonitorSetting.Setting.TYPE, MonitorSetting.Types.system.name());
                settings.add(settingType);
                MonitorSetting settingStat = new MonitorSetting(MonitorSetting.Setting.STAT, MonitorSetting.Stats.cpu.name());
                settings.add(settingStat);
                MonitorSetting settingChart = new MonitorSetting(MonitorSetting.Setting.CHART, MonitorSetting.Charts.gauge.name());
                settings.add(settingChart);
                MonitorSetting settingParser = new MonitorSetting(MonitorSetting.Setting.PARSER, "return response.data;");
                settings.add(settingParser);
                MonitorSetting settingInterval = new MonitorSetting(MonitorSetting.Setting.INTERVAL, MonitorSetting.Intervals.s30.name());
                settings.add(settingInterval);
                MonitorSetting settingProtocol = new MonitorSetting(MonitorSetting.Setting.PROTOCOL, MonitorSetting.Protocols.rest.name());
                settings.add(settingProtocol);
                monitorOne.setSettings(settings);
                monitors.add(monitorOne);
                Monitor monitorTwo = new Monitor("bbb");
                settings = new HashSet<MonitorSetting>();
                settingUrl = new MonitorSetting(MonitorSetting.Setting.URL, "http://bbb.com:8080");
                settings.add(settingUrl);
                settingType = new MonitorSetting(MonitorSetting.Setting.TYPE, MonitorSetting.Types.script.name());
                settings.add(settingType);
                settingStat = new MonitorSetting(MonitorSetting.Setting.SCRIPT, "bbb.sh");
                settings.add(settingStat);
                settingChart = new MonitorSetting(MonitorSetting.Setting.CHART, MonitorSetting.Charts.status.name());
                settings.add(settingChart);
                settingParser = new MonitorSetting(MonitorSetting.Setting.PARSER, "return response.data;");
                settings.add(settingParser);
                settingInterval = new MonitorSetting(MonitorSetting.Setting.INTERVAL, MonitorSetting.Intervals.m1.name());
                settings.add(settingInterval);
                settingProtocol = new MonitorSetting(MonitorSetting.Setting.PROTOCOL, MonitorSetting.Protocols.mq.name());
                settings.add(settingProtocol);
                monitorTwo.setSettings(settings);
                monitors.add(monitorTwo);
                example.setMonitors(monitors);
                dashboards.add(example);
                Dashboard exampleTwo = new Dashboard("Example 2");
                monitors = new HashSet<Monitor>();
                monitorOne = new Monitor("ccc");
                settings = new HashSet<MonitorSetting>();
                settingUrl = new MonitorSetting(MonitorSetting.Setting.URL, "http://ccc.com:8080");
                settings.add(settingUrl);
                settingType = new MonitorSetting(MonitorSetting.Setting.TYPE, MonitorSetting.Types.system.name());
                settings.add(settingType);
                settingStat = new MonitorSetting(MonitorSetting.Setting.STAT, MonitorSetting.Stats.ram.name());
                settings.add(settingStat);
                settingChart = new MonitorSetting(MonitorSetting.Setting.CHART, MonitorSetting.Charts.gauge.name());
                settings.add(settingChart);
                settingParser = new MonitorSetting(MonitorSetting.Setting.PARSER, "return response.data;");
                settings.add(settingParser);
                settingInterval = new MonitorSetting(MonitorSetting.Setting.INTERVAL, MonitorSetting.Intervals.s5.name());
                settings.add(settingInterval);
                settingProtocol = new MonitorSetting(MonitorSetting.Setting.PROTOCOL, MonitorSetting.Protocols.mq.name());
                settings.add(settingProtocol);
                monitorOne.setSettings(settings);
                monitors.add(monitorOne);
                monitorTwo = new Monitor("ddd");
                settings = new HashSet<MonitorSetting>();
                settingUrl = new MonitorSetting(MonitorSetting.Setting.URL, "http://ddd.com:8080");
                settings.add(settingUrl);
                settingType = new MonitorSetting(MonitorSetting.Setting.TYPE, MonitorSetting.Types.script.name());
                settings.add(settingType);
                settingStat = new MonitorSetting(MonitorSetting.Setting.SCRIPT, "ddd.bat");
                settings.add(settingStat);
                settingChart = new MonitorSetting(MonitorSetting.Setting.CHART, MonitorSetting.Charts.bar.name());
                settings.add(settingChart);
                settingParser = new MonitorSetting(MonitorSetting.Setting.PARSER, "return 'abc';");
                settings.add(settingParser);
                settingInterval = new MonitorSetting(MonitorSetting.Setting.INTERVAL, MonitorSetting.Intervals.d1.name());
                settings.add(settingInterval);
                settingProtocol = new MonitorSetting(MonitorSetting.Setting.PROTOCOL, MonitorSetting.Protocols.rest.name());
                settings.add(settingProtocol);
                monitorTwo.setSettings(settings);
                monitors.add(monitorTwo);
                exampleTwo.setMonitors(monitors);
                dashboards.add(exampleTwo);
                returnUser.setDashboards(dashboards);
                return Optional.of(returnUser);
            }
        });
        mvc.perform(post("/dashboards/get").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("Example")))
            .andExpect(content().string(containsString("monitors")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("aaa")))
            .andExpect(content().string(containsString("settings")))
            .andExpect(content().string(containsString("key")))
            .andExpect(content().string(containsString("TYPE")))
            .andExpect(content().string(containsString("value")))
            .andExpect(content().string(containsString("system")))
            .andExpect(content().string(containsString("URL")))
            .andExpect(content().string(containsString("http://aaa.com:8080")))
            .andExpect(content().string(containsString("CHART")))
            .andExpect(content().string(containsString("gauge")))
            .andExpect(content().string(containsString("STAT")))
            .andExpect(content().string(containsString("cpu")))
            .andExpect(content().string(containsString("PARSER")))
            .andExpect(content().string(containsString("return response.data;")))
            .andExpect(content().string(containsString("INTERVAL")))
            .andExpect(content().string(containsString("s30")))
            .andExpect(content().string(containsString("PROTOCOL")))
            .andExpect(content().string(containsString("rest")))
            .andExpect(content().string(containsString("bbb")))
            .andExpect(content().string(containsString("script")))
            .andExpect(content().string(containsString("http://bbb.com:8080")))
            .andExpect(content().string(containsString("status")))
            .andExpect(content().string(containsString("SCRIPT")))
            .andExpect(content().string(containsString("bbb.sh")))
            .andExpect(content().string(containsString("PARSER")))
            .andExpect(content().string(containsString("return response.data;")))
            .andExpect(content().string(containsString("m1")))
            .andExpect(content().string(containsString("mq")))
            .andExpect(content().string(containsString("Example 2")))
            .andExpect(content().string(containsString("ccc")))
            .andExpect(content().string(containsString("system")))
            .andExpect(content().string(containsString("http://ccc.com:8080")))
            .andExpect(content().string(containsString("gauge")))
            .andExpect(content().string(containsString("ram")))
            .andExpect(content().string(containsString("return response.data;")))
            .andExpect(content().string(containsString("s5")))
            .andExpect(content().string(containsString("mq")))
            .andExpect(content().string(containsString("ddd")))
            .andExpect(content().string(containsString("script")))
            .andExpect(content().string(containsString("http://ddd.com:8080")))
            .andExpect(content().string(containsString("bar")))
            .andExpect(content().string(containsString("ddd.bat")))
            .andExpect(content().string(containsString("return 'abc';")))
            .andExpect(content().string(containsString("d1")))
            .andExpect(content().string(containsString("rest")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("get dashboard")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")))
            .andExpect(content().string(containsString("userId")));
    }


    @Test
    public void getEdit() throws Exception {
        DashboardParameters params = new DashboardParameters();
        params.setName("New name");
        when(service.getDashboardById(anyLong())).thenAnswer(new Answer<Optional<Dashboard>>() {
            @Override
            public Optional<Dashboard> answer(InvocationOnMock invocation) {
                Dashboard returnDashboard = new Dashboard();
                returnDashboard.setName("Old name");
                return Optional.of(returnDashboard);
            }
        });
        when(service.save(any(Dashboard.class))).thenAnswer(new Answer<Dashboard>() {
            @Override
            public Dashboard answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return (Dashboard) args[0];
            }
        });
        mvc.perform(post("/dashboards/edit").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("New name")))
            .andExpect(content().string(containsString("monitors")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("edit dashboard")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")));
    }

    @Test
    public void getDelete() throws Exception {
        DashboardParameters params = new DashboardParameters();
        params.setName("Example");
        Dashboard returnDashboard = new Dashboard();
        returnDashboard.setName("Example");
        when(service.getDashboardById(anyLong())).thenAnswer(new Answer<Optional<Dashboard>>() {
            @Override
            public Optional<Dashboard> answer(InvocationOnMock invocation) {
                return Optional.of(returnDashboard);
            }
        });
        when(userService.getUserById(anyLong())).thenAnswer(new Answer<Optional<User>>() {
            @Override
            public Optional<User> answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                User returnUser = new User();
                returnUser.setUsername("user name");
                returnUser.setPassword("password123");
                returnUser.setRole(User.Role.R_USER);
                returnUser.setEmail("abc@def.com");
                HashSet<Dashboard> dashboards = new HashSet<Dashboard>();
                dashboards.add(returnDashboard);
                returnUser.setDashboards(dashboards);
                return Optional.of(returnUser);
            }
        });
        mvc.perform(post("/dashboards/delete").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("Example")))
            .andExpect(content().string(containsString("monitors")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("delete dashboard")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")))
            .andExpect(content().string(containsString("userId")));
    }
}