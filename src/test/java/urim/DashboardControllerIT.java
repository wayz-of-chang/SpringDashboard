package urim;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URL;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class DashboardControllerIT extends BaseIT {

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/greeting");
        template = new TestRestTemplate();
    }

    @Test
    public void getDashboard() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), containsString("Hello, World"));
    }
}