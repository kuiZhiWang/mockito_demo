package pri.wkz.mockitodemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pri.wkz.mockitodemo.demos.config.InitConfig;

/**
 * <p>1. 简单的单元测试，mock数据库进行测试，详见 {@link pri.wkz.mockitodemo.demos.service.SysUserServiceUnitTest}</p>
 * <p>2. 调用数据库的集成测试，详见{@link pri.wkz.mockitodemo.demos.controller.SysUserEndpointIntegrationTest}</p>
 */
@SpringBootApplication
// 声明排除扫描InitConfig.class
@ComponentScan(
        basePackages = "pri.wkz.mockitodemo.demos",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = InitConfig.class),
        })
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
