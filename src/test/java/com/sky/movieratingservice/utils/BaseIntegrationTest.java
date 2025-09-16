package com.sky.movieratingservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.movieratingservice.utils.configuration.MySqlConnectionConfiguration;
import java.sql.Connection;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MySqlConnectionConfiguration.class})
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected DataSource dataSource;

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("848569320300.dkr.ecr.eu-west-1.amazonaws.com/dockerhub-cache/library/mysql:8.2.0").asCompatibleSubstituteFor("mysql"));

    static {
        mysqlContainer.start();
    }

    @SneakyThrows
    public void executeQuery(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(sql).execute();
        }
    }

}
