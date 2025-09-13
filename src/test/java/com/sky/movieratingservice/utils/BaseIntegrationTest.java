package com.sky.movieratingservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.movieratingservice.utils.configuration.MockServerClientConfiguration;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MockServerClientConfiguration.class})
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockServerClient mockServerClient;

    @Autowired
    protected DataSource dataSource;

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("848569320300.dkr.ecr.eu-west-1.amazonaws.com/dockerhub-cache/library/mysql:8.2.0").asCompatibleSubstituteFor("mysql"));

    @Container
    public static final MockServerContainer mockServerContainer = new MockServerContainer(
            DockerImageName.parse("848569320300.dkr.ecr.eu-west-1.amazonaws.com/dockerhub-cache/mockserver/mockserver:5.15.0").asCompatibleSubstituteFor("jamesdbloom/mockserver")
    );

    static {
        mysqlContainer.start();
        mockServerContainer.start();
    }

    @SneakyThrows
    public List<Map<String, Object>> fetchDbQueryResult(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            return resultSetToList(resultSet);
        }
    }

    @SneakyThrows
    public void executeQuery(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(sql).execute();
        }
    }

    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }
}
