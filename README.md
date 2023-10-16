## 单元测试
单元测试是指不启动spring容器的测试，只对某个方法的逻辑进行测试。

测试过程中所有外部调用全部mock了，比如数据库查询，网络请求等，只专注于测试代码里的逻辑。

可参考 `pri.wkz.mockitodemo.demos.service.SysUserServiceUnitTest` 内的单元测试。
里面有注释讲解数据库查询是如何mock的。

## 集成测试
集成测试是指启动spring容器的测试，可以测试容器内的很多逻辑。

同样这里也可以选择mock掉数据库查询，网络请求等；
也可以选择不mock，直接启动一个docker数据库进行查询。

强烈建议集成测试的时候，`@SpringBootTest(classes = XXXApplication.class)`注解里尽量填写测试环境声明的
application，这样可以灵活调整测试环境的配置策略，并且不影响正常代码。

### 不调用数据库的集成测试

详见 `pri.wkz.mockitodemo.TestWithoutDbApplication`，需要注意：
1. 声明一个独立的Application，并且需要排除DataSourceAutoConfiguration.class自动注入
2. 对系统内所有用到的repository方法都需要提供一个mock对象，否则spring 容器无法启动
3. 如果程序自己声明了 entitymanager，需要自行排除这些bean的注入

### 调用数据库的集成测试
集成测试因为要启动spring容器，如果mock数据库需要手动声明很多mock repository。
而且如果不测试数据库逻辑，很多数据库相关的错误难以发现。
所以更推荐集成测试调用真实数据库。

调用数据库的集成测试配置要点：
#### 1. 编写配置文件
- 集成测试推荐使用测试环境独立的配置文件，并建议分本地和自动测试两个profile的配置文件。
通过主文件的`spring.profiles.active=xxx`控制使用哪个环境的配置。
- 数据库ddl配置建议使用自动创建表，并且运行结束删除，这个可以保证测试数据库的纯净：
```properties
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop
```

#### 2. 编写测试代码
集成测试测试接口的话，可以参考 `pri.wkz.mockitodemo.demos.controller.SysUserEndpointIntegrationTest`
里面有注释讲解如何发送一个mock请求。

注意要点：
- 测试类上的`@Rollback(value = true)`注解是控制测试方法结束后是否回滚数据库，这个注解需要配合
`@Transactional` 一起使用才能生效。作用也是保证测试数据库的纯净。
- mock请求是不会经过系统内声明的filter的，如果有认证的需求，需要自己手动将filer加到MockMvc对象中。

### 一些特殊的坑的处理
#### 1. 测试可以正常执行，但是mvn test命令就会失败
并且报错：The forked VM terminated without saying properly goodbye. VM crash or System.exit called ?

这个是因为测试使用的jdk的路径里有中文或者特殊字符，复制一份jdk到一个纯英语路径即可。

#### 2. 集成测试中，有一些数据敏感（有账号密码）的bean初始化失败了，导致spring容器启动失败
比如profile-backend中，项目启动会从数据库读取cognitoClient的账号密码并初始化，测试环境不可能上传账号密码到git，
所以需要将这个cognitoClient用mock bean替换掉。

这里用这个demo服务的 `pri.wkz.mockitodemo.demos.config.InitConfig.XXXClient`举例

第一步，从spring容器中排除这个bean，在application上配置 `@ComponentScan`
```java
@SpringBootApplication
@ComponentScan(
        // 排除bean的同时，需要扫描的包路径
        basePackages = "pri.wkz.mockitodemo.demos",
        // 声明需要排除的bean
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = InitConfig.class),
        })
public class TestApplication {
```
第二步，在系统内注入一个mock bean
```java
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
```
