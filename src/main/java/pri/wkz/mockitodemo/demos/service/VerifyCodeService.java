package pri.wkz.mockitodemo.demos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pri.wkz.mockitodemo.demos.domain.SysCache;
import pri.wkz.mockitodemo.demos.repository.SysCacheRepository;

/**
 * @author kuiZhi Wang
 */
@Service
public class VerifyCodeService {
    @Autowired
    private SysCacheRepository sysCacheRepository;

    public void saveVerifyCode(String phone,String code){
        SysCache sysCache = new SysCache();
        sysCache.setName("verify-code:" + phone);
        sysCache.setValue(code);
        sysCache.setCreated(System.currentTimeMillis());
        sysCache.setExpired(System.currentTimeMillis() + 5 * 60 * 1000);
        sysCacheRepository.save(sysCache);
    }

    public String getVerifyCode(String phone){
        SysCache sysCache = sysCacheRepository.findEnableByKey("verify-code:" + phone);
        if (sysCache == null) {
            return null;
        }
        return sysCache.getValue();
    }
}
