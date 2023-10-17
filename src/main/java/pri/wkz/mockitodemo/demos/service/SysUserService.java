package pri.wkz.mockitodemo.demos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pri.wkz.mockitodemo.demos.controller.SysUserEndpoint;
import pri.wkz.mockitodemo.demos.domain.SysUser;
import pri.wkz.mockitodemo.demos.repository.SysUserRepository;

import java.util.regex.Pattern;

/**
 * @author kuiZhi Wang
 */
@Service
@Transactional
public class SysUserService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@]+@[\\w-_.+]+$");
    @Autowired
    private SysUserRepository sysUserRepository;

    public SysUser save(SysUserEndpoint.SysUserDTO sysUserDTO) {
        String email = sysUserDTO.getEmail();
        Assert.hasText(email, "Email can not be empty.");
        Assert.isTrue(EMAIL_PATTERN.matcher(email).matches(), "Email format is not correct.");
        Assert.isTrue(sysUserRepository.countByEmail(email) == 0, "Email already exists.");

        SysUser sysUser = new SysUser();
        sysUser.setEmail(email);
        return sysUserRepository.save(sysUser);
    }


    public SysUser update(Long id, String email) {
        Assert.hasText(email, "Email can not be empty.");
//        Assert.isTrue(EMAIL_PATTERN.matcher(email).matches(), "Email format is not correct.");
        Assert.isTrue(sysUserRepository.countByEmail(email) == 0, "Email already exists.");

        SysUser sysUser = sysUserRepository.findById(id).orElseThrow(() -> new RuntimeException("Un authentication."));
        sysUser.setEmail(email);
        return sysUserRepository.save(sysUser);
    }
}
