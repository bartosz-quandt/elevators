package com.fortum.codechallenge.elevators.backend;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fortum.codechallenge.elevators.backend.config.ConfigProperties;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * Preconfigured Spring Application boot class.
 */
@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class ElevatorApplication {
    
    @Autowired
    private ConfigProperties configProperties;

    /**
     * Start method that will be invoked when starting the Spring context.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }

    /**
     * Create a default thread pool for your convenience.
     *
     * @return Executor thread pool
     */
    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(configProperties.getNumberOfElevators());
    }

    /**
     * Create an event bus for your convenience.
     *
     * @return EventBus for async task execution
     */
    @Bean
    public EventBus eventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }
}
