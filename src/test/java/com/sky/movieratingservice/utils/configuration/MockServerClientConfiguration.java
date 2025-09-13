package com.sky.movieratingservice.utils.configuration;

import static com.sky.movieratingservice.utils.BaseIntegrationTest.mockServerContainer;

import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockServerClientConfiguration {

    @Bean
    public static MockServerClient mockServerClient() {
        return new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort()
        );
    }
}
