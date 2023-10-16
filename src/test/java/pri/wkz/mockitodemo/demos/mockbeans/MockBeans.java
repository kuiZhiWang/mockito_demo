package pri.wkz.mockitodemo.demos.mockbeans;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pri.wkz.mockitodemo.demos.config.InitConfig;

import static org.mockito.Mockito.when;

/**
 * @author kuiZhi Wang
 */
@Component
public class MockBeans {

    @Bean
    public InitConfig.XXXClient mockClient() {
        InitConfig.XXXClient mockClient = Mockito.mock(InitConfig.XXXClient.class);
        when(mockClient.connect()).then(invocation -> {
            System.out.println("mock connect");
            return 0;
        });
        return mockClient;
    }
}
