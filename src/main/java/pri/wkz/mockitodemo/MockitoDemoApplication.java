package pri.wkz.mockitodemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class MockitoDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockitoDemoApplication.class, args);
    }

}
