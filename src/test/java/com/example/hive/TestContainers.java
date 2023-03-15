package com.example.hive;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@ContextConfiguration(loader = TestContainers.class)
@AutoConfigureMockMvc
public class TestContainers extends SpringBootContextLoader {
    static MySQLContainer mySQLContainer;

        static {
            mySQLContainer = new MySQLContainer<>("mysql")
                    .withDatabaseName("hive").withReuse(true);
//                    .withUsername("root")
//                    .withPassword("password");
            mySQLContainer.start();
        }

        @DynamicPropertySource
        static void databaseProperties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
            registry.add("spring.datasource.username", mySQLContainer::getUsername);
            registry.add("spring.datasource.password", mySQLContainer::getPassword);
            registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        }
}
