package com.example.jmsdb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.jmsdb.module",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class)})
@EnableTransactionManagement
public class DatabaseConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="dataSource")
    public DataSource dataSource() {

        String propertyPrefix = "spring.datasource.";

        if (environment.getProperty( propertyPrefix + "url") == null && environment.getProperty(propertyPrefix + "databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            "cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(environment.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(environment.getProperty( propertyPrefix + "dataSourceClassName"));
        if (environment.getProperty(propertyPrefix +"url") == null || "".equals(environment.getProperty( propertyPrefix + "url"))) {
            config.addDataSourceProperty("databaseName", environment.getProperty(propertyPrefix + "databaseName"));
            config.addDataSourceProperty("serverName", environment.getProperty(propertyPrefix + "serverName"));
        } else {
            config.addDataSourceProperty("url", environment.getProperty(propertyPrefix +"url"));
        }
        config.addDataSourceProperty("user", environment.getProperty(propertyPrefix +"username"));
        config.addDataSourceProperty("password", environment.getProperty(propertyPrefix +"password"));
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(environment.getProperty(propertyPrefix +"max-active", Integer.class, 2));
        config.setConnectionTimeout(150000);
        return new HikariDataSource(config);

    }

////    @Bean
//    public Flyway flyway() {
//        log.debug("Running database migrations");
//        Flyway flyway = new Flyway();
//        flyway.setDataSource(dataSource());
//        //flyway.setInitOnMigrate(true);
//        flyway.setOutOfOrder(true);
//        flyway.setValidateOnMigrate(false);
//        flyway.repair();
//        flyway.migrate();
//        return flyway;
//    }
}
