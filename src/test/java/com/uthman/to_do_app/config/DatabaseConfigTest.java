package com.uthman.to_do_app.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigTest {

    @Test
    void testStandardJdbcUrlWithoutCredentials() {
        DatabaseConfig.ParsedDatabaseConfig config = DatabaseConfig.parseUrl(
                "jdbc:postgresql://localhost:5432/todo_flow",
                "default_user",
                "default_pass"
        );

        assertEquals("jdbc:postgresql://localhost:5432/todo_flow", config.getUrl());
        assertEquals("default_user", config.getUsername());
        assertEquals("default_pass", config.getPassword());
    }

    @Test
    void testJdbcUrlWithCredentials() {
        DatabaseConfig.ParsedDatabaseConfig config = DatabaseConfig.parseUrl(
                "jdbc:postgresql://todo_flow_user:20BxuIH5fAD2rS3SXwDGkuCE8UOdp6tZ@dpg-d8qd0ifavr4c738900f0-a.virginia-postgres.render.com/todo_flow",
                "ignored_user",
                "ignored_pass"
        );

        assertEquals("jdbc:postgresql://dpg-d8qd0ifavr4c738900f0-a.virginia-postgres.render.com/todo_flow", config.getUrl());
        assertEquals("todo_flow_user", config.getUsername());
        assertEquals("20BxuIH5fAD2rS3SXwDGkuCE8UOdp6tZ", config.getPassword());
    }

    @Test
    void testPostgresSchemeUrlWithCredentials() {
        DatabaseConfig.ParsedDatabaseConfig config = DatabaseConfig.parseUrl(
                "postgres://todo_flow_user:20BxuIH5fAD2rS3SXwDGkuCE8UOdp6tZ@dpg-d8qd0ifavr4c738900f0-a.virginia-postgres.render.com/todo_flow",
                "ignored_user",
                "ignored_pass"
        );

        assertEquals("jdbc:postgresql://dpg-d8qd0ifavr4c738900f0-a.virginia-postgres.render.com/todo_flow", config.getUrl());
        assertEquals("todo_flow_user", config.getUsername());
        assertEquals("20BxuIH5fAD2rS3SXwDGkuCE8UOdp6tZ", config.getPassword());
    }

    @Test
    void testPostgresqlSchemeUrlWithCredentials() {
        DatabaseConfig.ParsedDatabaseConfig config = DatabaseConfig.parseUrl(
                "postgresql://todo_flow_user:20BxuIH5fAD2rS3SXwDGkuCE8UOdp6tZ@dpg-d8qd0ifavr4c738900f0-a.virginia-postgres.render.com/todo_flow",
                "ignored_user",
                "ignored_pass"
        );

        assertEquals("jdbc:postgresql://dpg-d8qd0ifavr4c738900f0-a.virginia-postgres.render.com/todo_flow", config.getUrl());
        assertEquals("todo_flow_user", config.getUsername());
        assertEquals("20BxuIH5fAD2rS3SXwDGkuCE8UOdp6tZ", config.getPassword());
    }

    @Test
    void testNullUrl() {
        DatabaseConfig.ParsedDatabaseConfig config = DatabaseConfig.parseUrl(
                null,
                "default_user",
                "default_pass"
        );

        assertNull(config.getUrl());
        assertEquals("default_user", config.getUsername());
        assertEquals("default_pass", config.getPassword());
    }
}
