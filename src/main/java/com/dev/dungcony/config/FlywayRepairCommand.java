package com.dev.dungcony.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Command để repair Flyway checksum
 * Chạy: java -jar app.jar --flyway.repair=true
 */
@Component
@ConditionalOnProperty(name = "flyway.repair", havingValue = "true")
public class FlywayRepairCommand implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FlywayRepairCommand.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        logger.info("=== FLYWAY REPAIR STARTING ===");
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .load();
            
            flyway.repair();
            logger.info("=== FLYWAY REPAIR COMPLETED SUCCESSFULLY ===");
        } catch (Exception e) {
            logger.error("=== FLYWAY REPAIR FAILED ===", e);
            throw new RuntimeException("Flyway repair failed", e);
        }
    }
}
