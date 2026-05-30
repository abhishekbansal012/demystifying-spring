package com.frontalx.demystifying_spring.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Demonstrates @Profile — beans that only exist in specific environments.
 *
 * Activate profiles via:
 * - application.properties: spring.profiles.active=dev
 * - Command line: --spring.profiles.active=dev
 * - Environment variable: SPRING_PROFILES_ACTIVE=dev
 *
 * Use cases:
 * - Different database configs per environment
 * - Mock services in dev, real services in prod
 * - Extra logging/debugging beans in dev
 */
@Configuration
public class ProfileConfig {

    public interface DataSourceInfo {
        String getConnectionInfo();
        String getProfile();
    }

    @Bean
    @Profile("dev")
    public DataSourceInfo devDataSource() {
        return new DataSourceInfo() {
            @Override
            public String getConnectionInfo() {
                return "H2 in-memory database (jdbc:h2:mem:devdb)";
            }

            @Override
            public String getProfile() {
                return "dev";
            }
        };
    }

    @Bean
    @Profile("prod")
    public DataSourceInfo prodDataSource() {
        return new DataSourceInfo() {
            @Override
            public String getConnectionInfo() {
                return "PostgreSQL production database (jdbc:postgresql://prod-host:5432/app)";
            }

            @Override
            public String getProfile() {
                return "prod";
            }
        };
    }

    @Bean
    @Profile("default")
    public DataSourceInfo defaultDataSource() {
        return new DataSourceInfo() {
            @Override
            public String getConnectionInfo() {
                return "H2 in-memory database (default profile — no profile explicitly set)";
            }

            @Override
            public String getProfile() {
                return "default";
            }
        };
    }
}
