package urim.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import urim.server.parameters.MonitorParameters;
import urim.server.service.DashboardService;
import urim.server.service.MonitorService;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MonitorControllerTest extends BaseTest {

    private MockMvc mvc;
    private ObjectMapper mapper;

    @Mock
    private MonitorService service;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private MonitorController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void getCreate() throws Exception {
        MonitorParameters params = new MonitorParameters();
        when(service.create()).thenAnswer(new Answer<Monitor>() {
            @Override
            public Monitor answer(InvocationOnMock invocation) {
                Monitor returnMonitor = new Monitor();
                return returnMonitor;
            }
        });
        mvc.perform(post("/monitors/create").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("settings")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("create monitor")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")))
            .andExpect(content().string(containsString("dashboardId")))
            .andExpect(content().string(containsString("url")))
            .andExpect(content().string(containsString("protocol")))
            .andExpect(content().string(containsString("monitorType")))
            .andExpect(content().string(containsString("script")))
            .andExpect(content().string(containsString("parser")))
            .andExpect(content().string(containsString("chart")));
    }

    @Test
    public void getCopy() throws Exception {
    }

    @Test
    public void getGet() throws Exception {
        MonitorParameters params = new MonitorParameters();
        params.setDashboardId(7);
        when(dashboardService.getDashboardById(anyLong())).thenAnswer(new Answer<Optional<Dashboard>>() {
            @Override
            public Optional<Dashboard> answer(InvocationOnMock invocation) {
                Dashboard returnDashboard = new Dashboard("Example");
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
                returnDashboard.setMonitors(monitors);
                return Optional.of(returnDashboard);
            }
        });
        mvc.perform(post("/monitors/get").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
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
            .andExpect(content().string(containsString("dashboardId")))
            .andExpect(content().string(containsString("url")))
            .andExpect(content().string(containsString("protocol")))
            .andExpect(content().string(containsString("monitorType")))
            .andExpect(content().string(containsString("script")))
            .andExpect(content().string(containsString("parser")))
            .andExpect(content().string(containsString("chart")));
    }

    @Test
    public void getUpdate() throws Exception {
        MonitorParameters params = new MonitorParameters();
        params.setName("New name");
        params.setId(12);
        params.setUrl("http://aaa.com:8080");
        params.setMonitorType(MonitorSetting.Types.system.name());
        params.setStatType(MonitorSetting.Stats.fs.name());
        params.setChart(MonitorSetting.Charts.bar.name());
        params.setParser("return response.data;");
        params.setInterval(MonitorSetting.Intervals.m15.name());
        params.setProtocol(MonitorSetting.Protocols.rest.name());
        when(service.getMonitorById(anyLong())).thenAnswer(new Answer<Optional<Monitor>>() {
            @Override
            public Optional<Monitor> answer(InvocationOnMock invocation) {
                Monitor returnMonitor = new Monitor();
                returnMonitor.setName("Old name");
                returnMonitor.setSettings(new HashSet<MonitorSetting>());
                return Optional.of(returnMonitor);
            }
        });
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Monitor returnMonitor = (Monitor) args[0];
                MonitorParameters params = (MonitorParameters) args[1];
                HashSet<MonitorSetting> settings = new HashSet<MonitorSetting>();
                settings.add(new MonitorSetting(MonitorSetting.Setting.URL, params.getUrl()));
                settings.add(new MonitorSetting(MonitorSetting.Setting.TYPE, params.getMonitorType()));
                settings.add(new MonitorSetting(MonitorSetting.Setting.STAT, params.getStatType()));
                settings.add(new MonitorSetting(MonitorSetting.Setting.CHART, params.getChart()));
                settings.add(new MonitorSetting(MonitorSetting.Setting.PARSER, params.getParser()));
                settings.add(new MonitorSetting(MonitorSetting.Setting.INTERVAL, params.getInterval()));
                settings.add(new MonitorSetting(MonitorSetting.Setting.PROTOCOL, params.getProtocol()));
                returnMonitor.setSettings(settings);
                return null;
            }
        }).when(service).setSettings(any(Monitor.class), any(MonitorParameters.class));
        mvc.perform(post("/monitors/update_settings").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("New name")))
            .andExpect(content().string(containsString("settings")))
            .andExpect(content().string(containsString("URL")))
            .andExpect(content().string(containsString("http://aaa.com:8080")))
            .andExpect(content().string(containsString("TYPE")))
            .andExpect(content().string(containsString("system")))
            .andExpect(content().string(containsString("STAT")))
            .andExpect(content().string(containsString("fs")))
            .andExpect(content().string(containsString("CHART")))
            .andExpect(content().string(containsString("bar")))
            .andExpect(content().string(containsString("PARSER")))
            .andExpect(content().string(containsString("return response.data;")))
            .andExpect(content().string(containsString("INTERVAL")))
            .andExpect(content().string(containsString("m15")))
            .andExpect(content().string(containsString("PROTOCOL")))
            .andExpect(content().string(containsString("rest")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("update monitor")))
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
        MonitorParameters params = new MonitorParameters();
        params.setId(13);
        Dashboard returnDashboard = new Dashboard();
        returnDashboard.setName("Example");
        when(service.getMonitorById(anyLong())).thenAnswer(new Answer<Optional<Monitor>>() {
            @Override
            public Optional<Monitor> answer(InvocationOnMock invocation) {
                Monitor returnMonitor = new Monitor();
                returnMonitor.setName("Old name");
                returnMonitor.setSettings(new HashSet<MonitorSetting>());
                return Optional.of(returnMonitor);
            }
        });
        when(dashboardService.getDashboardById(anyLong())).thenAnswer(new Answer<Optional<Dashboard>>() {
            @Override
            public Optional<Dashboard> answer(InvocationOnMock invocation) {
                Dashboard returnDashboard = new Dashboard("Example");
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
                returnDashboard.setMonitors(monitors);
                return Optional.of(returnDashboard);
            }
        });
        mvc.perform(post("/monitors/delete").content(mapper.writer().writeValueAsString(params)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().string(containsString("id")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("Old name")))
            .andExpect(content().string(containsString("settings")))
            .andExpect(content().string(containsString("source")))
            .andExpect(content().string(containsString("delete monitor")))
            .andExpect(content().string(containsString("parameters")))
            .andExpect(content().string(containsString("interval")))
            .andExpect(content().string(containsString("name")))
            .andExpect(content().string(containsString("statType")))
            .andExpect(content().string(containsString("server")))
            .andExpect(content().string(containsString("port")))
            .andExpect(content().string(containsString("type")))
            .andExpect(content().string(containsString("dashboardId")));
    }
}