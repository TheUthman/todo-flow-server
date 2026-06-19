package com.uthman.to_do_app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url:#{null}}")
    private String dbUrl;

    @Value("${spring.datasource.username:#{null}}")
    private String dbUsername;

    @Value("${spring.datasource.password:#{null}}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;

    public static class ParsedDatabaseConfig {
        private final String url;
        private final String username;
        private final String password;

        public ParsedDatabaseConfig(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static ParsedDatabaseConfig parseUrl(String dbUrl, String dbUsername, String dbPassword) {
        String url = dbUrl;
        String username = dbUsername;
        String password = dbPassword;

        if (url != null && !url.trim().isEmpty()) {
            url = url.trim();

            // Normalization of prefix
            if (url.startsWith("postgres://")) {
                url = "jdbc:postgresql://" + url.substring("postgres://".length());
            } else if (url.startsWith("postgresql://")) {
                url = "jdbc:postgresql://" + url.substring("postgresql://".length());
            }

            // Normalization of postgresql scheme with embedded credentials: jdbc:postgresql://user:password@host/db
            if (url.startsWith("jdbc:postgresql://")) {
                String remaining = url.substring("jdbc:postgresql://".length());
                int atIndex = remaining.lastIndexOf('@');
                if (atIndex != -1) {
                    String credentials = remaining.substring(0, atIndex);
                    String hostDb = remaining.substring(atIndex + 1);

                    url = "jdbc:postgresql://" + hostDb;

                    int colonIndex = credentials.indexOf(':');
                    if (colonIndex != -1) {
                        username = credentials.substring(0, colonIndex);
                        password = credentials.substring(colonIndex + 1);
                    } else {
                        username = credentials;
                    }
                }
            }
        }

        return new ParsedDatabaseConfig(url, username, password);
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        ParsedDatabaseConfig config = parseUrl(dbUrl, dbUsername, dbPassword);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        return dataSource;
    }
}
