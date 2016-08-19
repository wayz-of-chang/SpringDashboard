package urim.server.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urim.server.BaseTest;
import urim.server.service.UserDetailsService;
import urim.server.service.UserService;
import urim.server.service.UserSettingService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseTest {

    private MockMvc mvc;

    @Mock
    UserService service;

    @Mock
    UserSettingService userSettingService;

    @Mock
    UserDetailsService userDetailsService;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new UserController(service, userSettingService, userDetailsService)).build();
    }

    @Test
    public void getTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));

        mvc.perform(MockMvcRequestBuilders.get("/test?name=user").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, user")));
    }
}