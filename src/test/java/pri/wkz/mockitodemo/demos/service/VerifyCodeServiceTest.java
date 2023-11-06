package pri.wkz.mockitodemo.demos.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pri.wkz.mockitodemo.demos.domain.SysCache;
import pri.wkz.mockitodemo.demos.repository.SysCacheRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class VerifyCodeServiceTest {
    @InjectMocks
    private VerifyCodeService verifyCodeService;
    @Mock
    private SysCacheRepository sysCacheRepository;
    @Captor
    private ArgumentCaptor<SysCache> saveCaptor;


    @Test
    void saveVerifyCode() {
        String phone = "12345678901";
        String code = "123456";
        verifyCodeService.saveVerifyCode(phone, code);
        // 这行代码和上面的 @Captor注解是一个作用
        ArgumentCaptor<SysCache> saveCaptor = ArgumentCaptor.forClass(SysCache.class);
        verify(sysCacheRepository).save(saveCaptor.capture());
        SysCache value = saveCaptor.getValue();
        assertEquals("verify-code:" + phone, value.getName());
        assertEquals(code, value.getValue());
        assertEquals(5 * 60 * 1000, value.getExpired() - value.getCreated());
    }
}
