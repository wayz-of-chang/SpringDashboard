package urim.server.model;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@ActiveProfiles("server")
public class MonitorTest extends urim.server.BaseTest {

    @Test
    public void getIntervalSeconds() throws Exception {
        Monitor toTest = new Monitor();
        assertThat(toTest.getIntervalSeconds(""),    equalTo(86400000L));
        assertThat(toTest.getIntervalSeconds("10s"), equalTo(86400000L));
        assertThat(toTest.getIntervalSeconds("s15"), equalTo(86400000L));
        assertThat(toTest.getIntervalSeconds("s5"),  equalTo(    5000L));
        assertThat(toTest.getIntervalSeconds("s10"), equalTo(   10000L));
        assertThat(toTest.getIntervalSeconds("s30"), equalTo(   30000L));
        assertThat(toTest.getIntervalSeconds("m1"),  equalTo(   60000L));
        assertThat(toTest.getIntervalSeconds("m5"),  equalTo(  300000L));
        assertThat(toTest.getIntervalSeconds("m15"), equalTo(  900000L));
        assertThat(toTest.getIntervalSeconds("m30"), equalTo( 1800000L));
        assertThat(toTest.getIntervalSeconds("h1"),  equalTo( 3600000L));
        assertThat(toTest.getIntervalSeconds("h2"),  equalTo( 7200000L));
        assertThat(toTest.getIntervalSeconds("d1"),  equalTo(86400000L));

        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.s5),  equalTo(    5000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.s10), equalTo(   10000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.s30), equalTo(   30000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.m1),  equalTo(   60000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.m5),  equalTo(  300000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.m15), equalTo(  900000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.m30), equalTo( 1800000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.h1),  equalTo( 3600000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.h2),  equalTo( 7200000L));
        assertThat(toTest.getIntervalSeconds(MonitorSetting.Intervals.d1),  equalTo(86400000L));
    }
}