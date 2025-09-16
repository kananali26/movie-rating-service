package com.sky.movieratingservice.utils.configuration;

import static com.sky.movieratingservice.utils.BaseIntegrationTest.mysqlContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MySqlConnectionConfiguration {

    static {
        System.setProperty("DB_URL", mysqlContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", mysqlContainer.getUsername());
        System.setProperty("DB_PASSWORD", mysqlContainer.getPassword());
    }

    @Bean
    @SneakyThrows
    public Connection testDbConnection() {
        String jdbcUrl = mysqlContainer.getJdbcUrl();
        String username = mysqlContainer.getUsername();
        String password = mysqlContainer.getPassword();
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
