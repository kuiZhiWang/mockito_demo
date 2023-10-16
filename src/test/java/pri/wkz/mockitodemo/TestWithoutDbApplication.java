package pri.wkz.mockitodemo;

import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pri.wkz.mockitodemo.demos.config.InitConfig;
import pri.wkz.mockitodemo.demos.repository.SysCacheRepository;
import pri.wkz.mockitodemo.demos.repository.SysUserRepository;

/**
 * 不需要数据库连接的集成测试环境
 * @author kuiZhi Wang
 */
// 声明排除jpa相关依赖的自动注入
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
})
@ComponentScan(
        basePackages = "pri.wkz.mockitodemo.demos",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = InitConfig.class),
        })
public class TestWithoutDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestWithoutDbApplication.class, args);
    }

    // 声明repository的mock bean
    @Bean
    public SysUserRepository sysUserRepository(){
        SysUserRepository mocked = Mockito.mock(SysUserRepository.class);
        return mocked;
    }

    @Bean
    public SysCacheRepository sysCacheRepository(){
        SysCacheRepository mocked = Mockito.mock(SysCacheRepository.class);
        return mocked;
    }
}
