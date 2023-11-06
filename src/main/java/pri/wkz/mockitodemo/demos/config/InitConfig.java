package pri.wkz.mockitodemo.demos.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author kuiZhi Wang
 */
@Slf4j
@Component
public class InitConfig {
    public InitConfig() {
        log.info("InitConfig init.");
    }

    @Bean
    public XXXClient xxxClient(
            @Value("${xxx.username:}") String username,
            @Value("${xxx.password:}") String password) {
        XXXClient xxxClient = new XXXClient(username, password);
        // init 逻辑，一顿连接
        xxxClient.connect();
        return xxxClient;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XXXClient {
        private String username;
        private String password;

        public int connect() {
            Assert.hasText(username, "xxx.username can not be empty.");
            Assert.hasText(password, "xxx.password can not be empty.");
            return 1;
        }
    }
}
