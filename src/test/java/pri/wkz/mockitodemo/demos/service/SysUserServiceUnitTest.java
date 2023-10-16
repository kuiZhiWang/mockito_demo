package pri.wkz.mockitodemo.demos.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pri.wkz.mockitodemo.demos.domain.SysUser;
import pri.wkz.mockitodemo.demos.repository.SysUserRepository;
import pri.wkz.mockitodemo.demos.controller.SysUserEndpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * SysUserService 单元测试
 */
@Slf4j
public class SysUserServiceUnitTest {
    /**
     * 使用 @Mock 注解，mock 掉 SysUserRepository <br/>
     * 在 MockitoAnnotations.openMocks(this) 方法调用后，sysUserRepository会被注入一个mock对象<br/>
     * 这个mock对象可以在测试方法中使用，所有方法不会真正执行，并且返回null
     */
    @Mock
    private SysUserRepository sysUserRepository;
    /**
     * 使用@InjectMocks 注解，会将 @Mock 注解的对象注入到 SysUserService 中
     * sysUserService实例是调用SysUserService的构造方法创建的，所以sysUserService的方法会真实执行
     */
    @InjectMocks
    private SysUserService sysUserService;

    @BeforeEach
    void beforeEach() {
        // 注入所有@Mock注解的对象
        MockitoAnnotations.openMocks(this);
        // 对mock对象的countByEmail方法进行mock，该声明的意思是 当调用 countByEmail 方法时，并且参数传入exists@test.com，返回1L
        when(sysUserRepository.countByEmail("exists@test.com"))
                .thenReturn(1L);
        // 对mock对象的save方法进行mock，该声明的意思是 当调用 save 方法时，返回参数传入的对象，并且设置id为1L
        when(sysUserRepository.save(any(SysUser.class)))
                .then(invocation -> {
                    // 取用mock方法传入的参数
                    SysUser sysUser = invocation.getArgument(0);
                    log.debug("save sysUser: {}", sysUser);
                    sysUser.setId(1L);
                    return sysUser;
                });
    }

    @Test
    void saveFailedWithEmptyEmail() {
        SysUserEndpoint.SysUserDTO sysUserDTO = createUser("");
        // 对抛出的异常类型进行断言
        Assertions.assertThrows(IllegalArgumentException.class, () -> sysUserService.save(sysUserDTO));
        log.info("pass");
    }

    @Test
    void saveFailedWithIllegalEmail() {
        SysUserEndpoint.SysUserDTO sysUserDTO = createUser("abc");
        // 对抛出的异常类型以及异常信息进行断言
        Assertions.assertThrows(IllegalArgumentException.class, () -> sysUserService.save(sysUserDTO),
                "Email format is not correct.");
        log.info("pass");
    }

    @Test
    void saveFailedWithExistedEmail() {
        SysUserEndpoint.SysUserDTO sysUserDTO = createUser("exists@test.com");
        // 对抛出的异常断言
        Assertions.assertThrows(IllegalArgumentException.class, () -> sysUserService.save(sysUserDTO),
                "Email already exists.");
        log.info("pass");
    }

    @Test
    void saveSuccess() {
        SysUserEndpoint.SysUserDTO sysUserDTO = createUser("test@test.com");
        SysUser sysUser = sysUserService.save(sysUserDTO);
        // 对返回的对象断言
        Assertions.assertNotNull(sysUser.getId());
        // 断言 sysUserRepository.save() 方法被调用了一次
        verify(sysUserRepository, times(1)).save(any(SysUser.class));
        log.info("pass");
    }


    // =========== utils ===============

    private SysUserEndpoint.SysUserDTO createUser(String email) {
        SysUserEndpoint.SysUserDTO sysUserDTO = new SysUserEndpoint.SysUserDTO();
        sysUserDTO.setEmail(email);
        return sysUserDTO;
    }
}
