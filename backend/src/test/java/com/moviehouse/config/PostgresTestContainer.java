package com.moviehouse.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {

    private static final PostgreSQLContainer<?> CONTAINER;

    static {
        CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");

        CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return CONTAINER;
    }
}