package com.sky.movieratingservice.configuration;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock() {
        // This ensures that in our application we always get the UTC clock
        return Clock.systemUTC();
    }
}
