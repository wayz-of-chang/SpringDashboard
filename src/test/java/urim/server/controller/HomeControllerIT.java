package urim.server.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import urim.server.BaseIT;

import java.net.URL;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class HomeControllerIT extends BaseIT {

    private ResponseEntity<String> response;

    @Before
    public void setUp() throws Exception {
        template = new TestRestTemplate();
    }

    @Test
    public void getIndex() throws Exception {
        this.base = new URL("http://localhost:" + port);
        this.response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), containsString("Urim - Dashboard"));
    }

    @Test
    public void getCsrf() throws Exception {
        this.base = new URL("http://localhost:" + port + "/csrf");
        this.response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), containsString("CSRF Refresh"));
    }

    @Test
    public void getPong() throws Exception {
        this.base = new URL("http://localhost:" + port + "/pong");
        this.response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), containsString("Hello, World"));

        this.base = new URL("http://localhost:" + port + "/pong?name=home");
        this.response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), containsString("Hello, home"));
    }
}