package pri.wkz.mockitodemo.demos.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.wkz.mockitodemo.demos.config.InitConfig;
import pri.wkz.mockitodemo.demos.domain.SysUser;
import pri.wkz.mockitodemo.demos.repository.SysUserRepository;
import pri.wkz.mockitodemo.demos.security.AuthContext;
import pri.wkz.mockitodemo.demos.service.MessengerService;
import pri.wkz.mockitodemo.demos.service.SysUserService;
import pri.wkz.mockitodemo.demos.service.VerifyCodeService;
import pri.wkz.mockitodemo.demos.views.ApiResponse;

import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kuiZhi Wang
 */
@RestController
@RequestMapping("/api/sys-user")
public class SysUserEndpoint {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private InitConfig.XXXClient xxxClient;

    @PostMapping
    public SysUser createSysUser(@RequestBody SysUserDTO sysUserDTO) {
        return sysUserService.save(sysUserDTO);
    }

    @Data
    public static class SysUserDTO {
        private String email;
    }

    @PostMapping("/phone/send-verify-code")
    public ApiResponse<Boolean> sendVerifyCode(@RequestBody SendVerifyCodeDTO sendVerifyCodeDTO) {
        Long currentUserId = AuthContext.currentUserId();
        SysUser sysUser = sysUserRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("Un authentication."));
        if (!Objects.equals(sysUser.getEmail(), sendVerifyCodeDTO.getEmail())) {
            return ApiResponse.of(110401, "Email not match.");
        }
        String phone = sendVerifyCodeDTO.getPhone();
        if (ObjectUtils.isEmpty(phone) || !phone.matches("\\+\\d+")) {
            return ApiResponse.of(110402, "Invalid phone.");
        }
        Random random = new Random();
        String code = Stream.of(10, 10, 10, 10)
                .map(random::nextInt)
                .map(Object::toString)
                .collect(Collectors.joining(""));

        verifyCodeService.saveVerifyCode(phone, code);

        boolean result = messengerService.sendSms("", phone);

        return ApiResponse.ok(result);
    }

    @Data
    public static class SendVerifyCodeDTO {
        private String email;
        private String phone;
    }

}
