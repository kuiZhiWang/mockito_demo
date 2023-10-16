package pri.wkz.mockitodemo.demos.repository;

import org.springframework.stereotype.Repository;
import pri.wkz.mockitodemo.demos.domain.SysUser;

/**
 * @author kuiZhi Wang
 */
@Repository
public interface SysUserRepository extends CommonRepository<SysUser, Long> {

    long countByEmail(String email);
}
