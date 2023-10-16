package pri.wkz.mockitodemo.demos.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pri.wkz.mockitodemo.demos.domain.SysCache;

/**
 * @author kuiZhi Wang
 */
@Repository
public interface SysCacheRepository extends CommonRepository<SysCache, Long> {
    @Query(value = "select * from sys_cache  where `name` like ?1 and expired > :#{T(System).currentTimeMillis()} order by id desc limit 1",
            nativeQuery = true)
    SysCache findEnableByKey(String name);

}
