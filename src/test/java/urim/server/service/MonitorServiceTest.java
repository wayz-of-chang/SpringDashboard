package urim.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urim.server.BaseTest;
import urim.server.model.Monitor;
import urim.server.model.MonitorSetting;
import urim.server.parameters.MonitorParameters;
import urim.server.repository.MonitorRepository;
import urim.server.repository.MonitorSettingRepository;

import java.util.HashSet;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class MonitorServiceTest extends BaseTest {

    private MockMvc mvc;
    private ObjectMapper mapper;

    @Mock
    private MonitorRepository repository;

    @Mock
    private MonitorSettingRepository settingRepository;

    @InjectMocks
    private MonitorService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(service).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void setSettings() throws Exception {
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
        Monitor monitor = new Monitor();
        monitor.setName("Old name");
        monitor.setSettings(new HashSet<MonitorSetting>());
        service.setSettings(monitor, params);
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.URL), containsString("http://aaa.com:8080"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.TYPE), containsString("system"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.STAT), containsString("fs"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.CHART), containsString("gauge"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PARSER), containsString("var value = response.data.fsUsed; var unit = 'MB'; var divider = 1000000; if (value > divider * 1000) { unit = 'GB'; divider = divider * 1000; } if (value > divider * 1000) { unit = 'TB'; divider = divider * 1000; } return {value: Math.round(10 * value / divider) / 10, max: response.data.fsTotal / divider, 'unit': unit, mediumThreshold: 0.5, highThreshold: 0.9}"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.INTERVAL), containsString("m15"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PROTOCOL), containsString("rest"));

        params.setName("New name");
        params.setId(12);
        params.setUrl("http://bbb.com:8080");
        params.setMonitorType(MonitorSetting.Types.system.name());
        params.setStatType(MonitorSetting.Stats.cpu.name());
        params.setChart(MonitorSetting.Charts.line.name());
        params.setParser("return response.data;");
        params.setInterval(MonitorSetting.Intervals.h1.name());
        params.setProtocol(MonitorSetting.Protocols.mq.name());
        service.setSettings(monitor, params);
        monitor.resetSettingsMap();
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.URL), containsString("http://bbb.com:8080"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.TYPE), containsString("system"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.STAT), containsString("cpu"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.CHART), containsString("gauge"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PARSER), containsString("return {value: response.data.cpuUsed, max: response.data.cpuTotal, unit: '%', mediumThreshold: 0.5, highThreshold: 0.9}"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.INTERVAL), containsString("h1"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PROTOCOL), containsString("mq"));

        params.setName("New name");
        params.setId(12);
        params.setUrl("http://ccc.com:8080");
        params.setMonitorType(MonitorSetting.Types.script.name());
        params.setScript("hello.bat");
        params.setChart(MonitorSetting.Charts.line.name());
        params.setParser("return response.data;");
        params.setInterval(MonitorSetting.Intervals.s5.name());
        params.setProtocol(MonitorSetting.Protocols.rest.name());
        service.setSettings(monitor, params);
        monitor.resetSettingsMap();
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.URL), containsString("http://ccc.com:8080"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.TYPE), containsString("script"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.STAT), containsString("cpu"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.SCRIPT), containsString("hello.bat"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.CHART), containsString("line"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PARSER), containsString("return response.data;"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.INTERVAL), containsString("s5"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PROTOCOL), containsString("rest"));

        params.setName("New name");
        params.setId(12);
        params.setUrl("http://ccc.com:8080");
        params.setMonitorType(MonitorSetting.Types.system.name());
        params.setStatType(MonitorSetting.Stats.ram.name());
        params.setChart(MonitorSetting.Charts.line.name());
        params.setParser("return response.data;");
        params.setInterval(MonitorSetting.Intervals.s5.name());
        params.setProtocol(MonitorSetting.Protocols.rest.name());
        service.setSettings(monitor, params);
        monitor.resetSettingsMap();
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.URL), containsString("http://ccc.com:8080"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.TYPE), containsString("system"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.STAT), containsString("ram"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.SCRIPT), containsString("hello.bat"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.CHART), containsString("gauge"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PARSER), containsString("var value = response.data.memUsed; var unit = 'MB'; var divider = 1000000; if (value > divider * 1000) { unit = 'GB'; divider = divider * 1000; } if (value > divider * 1000) { unit = 'TB'; divider = divider * 1000; } return {value: Math.round(10 * value / divider) / 10, max: response.data.memTotal / divider, 'unit': unit, mediumThreshold: 0.5, highThreshold: 0.9}"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.INTERVAL), containsString("s5"));
        assertThat(monitor.settingsMap().get(MonitorSetting.Setting.PROTOCOL), containsString("rest"));
    }

}