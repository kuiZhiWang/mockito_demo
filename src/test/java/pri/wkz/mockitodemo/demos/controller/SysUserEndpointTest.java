package pri.wkz.mockitodemo.demos.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pri.wkz.mockitodemo.TestApplication;
import pri.wkz.mockitodemo.demos.security.AuthFilter;
import pri.wkz.mockitodemo.demos.service.MessengerService;

import static org.mockito.ArgumentMatchers.anyString;

@Slf4j
@SpringBootTest(classes = TestApplication.class)

@Rollback(value = true)
@Transactional
class SysUserEndpointTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @Autowired
    private AuthFilter authFilter;

    @MockBean
    private MessengerService messengerService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(authFilter)
                .build();

        Mockito.when(messengerService.sendSms(anyString(), anyString()))
                .then(invocation -> {
                    String argument = invocation.getArgument(0);
                    return true;
                });
    }

    @Test
    void sendVerifyCode() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/sys-user/phone/send-verify-code")
                        .param("mobile", "12345678901")
                        .content("")
                        .header("","")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
        ;
    }
}
