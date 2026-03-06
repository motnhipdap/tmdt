package com.dev.dungcony.commons.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);

    @Autowired
    private DataSource dataSource;

    @Value("${spring.flyway.locations}")
    private String[] locations;

    @Value("${spring.flyway.baseline-on-migration:false}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.validate-on-migrate:true}")
    private boolean validateOnMigrate;

    @Bean
    public Flyway flyway() {
        logger.info("=== INITIALIZING FLYWAY ===");

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(baselineOnMigrate)
                .validateOnMigrate(validateOnMigrate)
                .load();

        flyway.migrate();
        logger.info("=== FLYWAY MIGRATION COMPLETED ===");
        return flyway;
    }
}
