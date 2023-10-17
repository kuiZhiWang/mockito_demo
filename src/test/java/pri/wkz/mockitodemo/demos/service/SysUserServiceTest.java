package pri.wkz.mockitodemo.demos.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pri.wkz.mockitodemo.demos.domain.SysUser;
import pri.wkz.mockitodemo.demos.repository.SysUserRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
class SysUserServiceTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private SysUserService sysUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(sysUserRepository.countByEmail("email1@test.com"))
                .thenReturn(1L);
        Mockito.when(sysUserRepository.findById(1L)).then(invocation -> {
            SysUser sysUser = new SysUser();
            sysUser.setEmail("1");
            return Optional.of(sysUser);
        });

        Mockito.when(sysUserRepository.save(ArgumentMatchers.any(SysUser.class)))
                .then(invocation -> {
                    SysUser sysUser = invocation.getArgument(0);
                    sysUser.setId(1L);
                    return sysUser;
                });
    }

    @Test
    void update() {
        sysUserService.update(1L, "email@test.com");
        Mockito.verify(sysUserRepository, times(1)).save(ArgumentMatchers.any(SysUser.class));
    }

    @Test
    void updateWithIllgealEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sysUserService.update(1L, "email");
        });
    }
}
