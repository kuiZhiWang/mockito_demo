package pri.wkz.mockitodemo.demos.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pri.wkz.mockitodemo.TestApplication;
import pri.wkz.mockitodemo.demos.domain.SysUser;
import pri.wkz.mockitodemo.demos.repository.SysUserRepository;
import pri.wkz.mockitodemo.demos.security.AuthFilter;
import pri.wkz.mockitodemo.demos.service.MessengerService;
import pri.wkz.mockitodemo.demos.service.VerifyCodeService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 集成测试
 */
@Slf4j
// 指定spring boot 启动类
@SpringBootTest(classes = TestApplication.class)
// 两个注解，在测试完成之后回滚事务，保证测试库纯净
@Rollback(value = true)
@Transactional
public class SysUserEndpointIntegrationTest {
    /**
     * 注入 springboot的 web context对象
     */
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;
    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 声明一个MessengerService的mock对象，并将这个对象注入到spring容器中
     */
    @MockBean
    private MessengerService messengerService;

    private Long authedUserId;


    @BeforeEach
    void beforeEach() {
        // 初始化测试数据
        SysUser sysUser = new SysUser();
        sysUser.setEmail("verify_code_test1@test.com");
        SysUser saved = sysUserRepository.save(sysUser);
        authedUserId = saved.getId();

        // 构建mock mvc对象
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                // mock 请求正常情况下不会经过系统声明的filter，但是有些需要认证的接口必须要经过filter认证
                // 所以需要将认证filter加入到mock mvc中
                .addFilter(authFilter)
                .build();
        // mock messengerService的sendSms方法，所有参数传入都返回true
        when(messengerService.sendSms(anyString(), anyString())).thenReturn(true);
    }


    @Test
    void sendVerifyCodeSuccess() throws Exception {
        String phone = "+8612345678901";
        String payload = buildPayload("verify_code_test1@test.com", phone);

        mockPostRequest(payload)
                // 断言响应json里包含 code字段，并且字段值为200
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                // 断言响应json里包含 data，并且字段值为 true
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true))
        // 打印请求，可以打印也可以不打印
//                .andDo(MockMvcResultHandlers.print())
        ;
        // 断言数据库里是否保存了验证码
        String verifyCode = verifyCodeService.getVerifyCode(phone);
        Assertions.assertNotNull(verifyCode);
        // 断言 messengerService.sendSms() 调用了一次
        verify(messengerService, times(1)).sendSms(anyString(), anyString());
        log.info("pass");
    }

    @Test
    void sendVerifyCodeFailedCauseEmailNotMatch() throws Exception {
        String phone = "+8612345678901";
        String payload = buildPayload(" ", phone);
        mockPostRequest(payload)
                // 断言响应json里包含 code字段，并且字段值为110401
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(110401))
                // 断言响应json里包含 msg，并且字段值为 Email not match.
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Email not match."))
                // 打印请求
//                .andDo(MockMvcResultHandlers.print())
        ;
        // 断言 messengerService.sendSms() 被调用了0次
        verify(messengerService, times(0)).sendSms(anyString(), anyString());
    }

    // ============== utils =================

    /**
     * 构造mock请求的json请求体
     *
     * @param email
     * @param phone
     * @return
     */
    private String buildPayload(String email, String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("phone", phone);
        return jsonObject.toJSONString();
    }

    /**
     * 发送mock post请求
     *
     * @param payload 请求体
     * @return
     * @throws Exception
     */
    private ResultActions mockPostRequest(String payload) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/sys-user/phone/send-verify-code")
                        // 请求体
                        .content(payload)
                        // 请求头，认证的请求头
                        .header("debug-user", String.valueOf(authedUserId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                // 验证请求http状态码是否200
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
