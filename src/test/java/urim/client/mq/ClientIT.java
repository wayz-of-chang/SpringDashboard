package urim.client.mq;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import urim.BaseIT;
import urim.Message;
import urim.data.ScriptData;
import urim.data.SystemData;

import java.net.URL;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ClientIT extends BaseIT {
    private Client client;
    private Message response;
    private ResponseEntity<String> restResponse;
    private String scriptExtension;

    @Before
    public void setUp() throws Exception {
        this.template = new TestRestTemplate();
        this.client = new Client();
        String osName = System.getProperty("os.name");
        if (osName.equals("Windows NT") || osName.equals("Windows 95") || osName.contains("Windows")) {
            this.scriptExtension = "bat";
        } else if (osName.contains("Linux") || osName.contains("Mac OS X")) {
            this.scriptExtension = "sh";
        }
    }

    @Test
    public void getStats() throws Exception {
        this.response = this.client.getStats("aaa", Client.TaskTypes.ping, "", 1);
        assertThat((String) this.response.getData(), containsString(""));
        assertThat(this.response.getId(), equalTo(1L));
        assertThat(this.response.getParameters().getName(), containsString("aaa"));
        assertThat(this.response.getParameters().getStatType(), containsString("default"));
        assertThat(this.response.getParameters().getType().name(), containsString("MQ"));

        this.response = this.client.getStats("bbb", Client.TaskTypes.ping, "hello", 2);
        assertThat((String) this.response.getData(), containsString("hello"));
        assertThat(this.response.getId(), equalTo(2L));
        assertThat(this.response.getParameters().getName(), containsString("bbb"));
        assertThat(this.response.getParameters().getStatType(), containsString("default"));

        this.response = this.client.getStats("ccc", Client.TaskTypes.system, "", 3);
        assertThat((SystemData) this.response.getData(), isA(SystemData.class));
        assertThat(this.response.getId(), equalTo(3L));
        assertThat(this.response.getParameters().getName(), containsString("ccc"));
        assertThat(this.response.getParameters().getStatType(), containsString("system"));
        assertThat(this.response.getParameters().getType().name(), containsString("MQ"));

        this.response = this.client.getStats("ddd", Client.TaskTypes.script, "", 4);
        assertThat(((ScriptData) this.response.getData()).getOutput().get(0), containsString("hello, world"));
        assertThat(this.response.getId(), equalTo(4L));
        assertThat(this.response.getParameters().getName(), containsString("ddd"));
        assertThat(this.response.getParameters().getStatType(), containsString("script"));
        assertThat(this.response.getParameters().getType().name(), containsString("MQ"));

        this.response = this.client.getStats("eee", Client.TaskTypes.script, String.format("%s.%s", "ping-perl", this.scriptExtension), 5);
        assertThat(((ScriptData) this.response.getData()).getOutput().get(0), containsString("hello, world"));
        assertThat(((ScriptData) this.response.getData()).getReturnValue(), equalTo(0));
        assertThat(this.response.getId(), equalTo(5L));
        assertThat(this.response.getParameters().getName(), containsString("eee"));
        assertThat(((urim.client.mq.Parameters) this.response.getParameters()).getValue(), containsString("ping-perl"));

        this.response = this.client.getStats("fff", Client.TaskTypes.script, String.format("%s", "hello"), 6);
        assertThat(((ScriptData) this.response.getData()).getError().get(0), containsString("Script hello could not be found"));
        assertThat(((ScriptData) this.response.getData()).getReturnValue(), equalTo(-1));
        assertThat(this.response.getId(), equalTo(6L));
        assertThat(this.response.getParameters().getName(), containsString("fff"));
    }

    @Test
    public void startStopMonitor() throws Exception {
        String key = "123";
        String interval = "1000";
        String type = "ping";
        String name = "blah";
        this.base = new URL(String.format("http://localhost:%s/start?key=%s&interval=%s&type=%s&name=%s", Integer.toString(this.port), key, interval, type, name));
        this.restResponse = this.template.getForEntity(this.base.toString(), String.class);
        assertThat(this.restResponse.getBody(), containsString("id"));
        assertThat(this.restResponse.getBody(), containsString("1"));
        assertThat(this.restResponse.getBody(), containsString("data"));
        assertThat(this.restResponse.getBody(), containsString("started monitoring, 123"));
        assertThat(this.restResponse.getBody(), containsString("source"));
        assertThat(this.restResponse.getBody(), containsString("blah"));
        assertThat(this.restResponse.getBody(), containsString("parameters"));
        assertThat(this.restResponse.getBody(), containsString("interval"));
        assertThat(this.restResponse.getBody(), containsString("name"));
        assertThat(this.restResponse.getBody(), containsString("123"));
        assertThat(this.restResponse.getBody(), containsString("statType"));
        assertThat(this.restResponse.getBody(), containsString("start"));
        assertThat(this.restResponse.getBody(), containsString("server"));
        assertThat(this.restResponse.getBody(), containsString("port"));
        assertThat(this.restResponse.getBody(), containsString("type"));
        assertThat(this.restResponse.getBody(), containsString("MQ"));

        this.base = new URL(String.format("http://localhost:%s/stop?key=%s", Integer.toString(this.port), key));
        this.restResponse = this.template.getForEntity(this.base.toString(), String.class);
        assertThat(this.restResponse.getBody(), containsString("id"));
        assertThat(this.restResponse.getBody(), containsString("data"));
        assertThat(this.restResponse.getBody(), containsString("stopped monitoring, 123"));
        assertThat(this.restResponse.getBody(), containsString("source"));
        assertThat(this.restResponse.getBody(), containsString("parameters"));
        assertThat(this.restResponse.getBody(), containsString("interval"));
        assertThat(this.restResponse.getBody(), containsString("name"));
        assertThat(this.restResponse.getBody(), containsString("123"));
        assertThat(this.restResponse.getBody(), containsString("statType"));
        assertThat(this.restResponse.getBody(), containsString("stop"));
        assertThat(this.restResponse.getBody(), containsString("server"));
        assertThat(this.restResponse.getBody(), containsString("port"));
        assertThat(this.restResponse.getBody(), containsString("type"));
        assertThat(this.restResponse.getBody(), containsString("MQ"));
    }
}
