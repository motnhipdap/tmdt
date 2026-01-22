package com.dev.dungcony.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

@Configuration
@Order(1) // Chạy trước các config khác
public class FlywayConfig {

    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);

    @Autowired
    private DataSource dataSource;

    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String[] locations;

    @Value("${spring.flyway.baseline-on-migration:false}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.validate-on-migrate:true}")
    private boolean validateOnMigrate;

    @Value("${flyway.repair-on-validate-error:false}")
    private boolean repairOnValidateError;

    @Bean
    public Flyway flyway() {
        logger.info("=== CREATING FLYWAY BEAN MANUALLY ===");
        logger.info("Flyway locations: {}", java.util.Arrays.toString(locations));
        logger.info("Flyway baseline on migrate: {}", baselineOnMigrate);
        logger.info("Flyway validate on migrate: {}", validateOnMigrate);
        logger.info("Flyway repair on validate error: {}", repairOnValidateError);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(baselineOnMigrate)
                .validateOnMigrate(validateOnMigrate)
                .load();

        // Nếu được phép, repair trước khi migrate
        if (repairOnValidateError) {
            try {
                flyway.validate();
                logger.info("=== FLYWAY VALIDATION PASSED ===");
            } catch (Exception e) {
                logger.warn("=== FLYWAY VALIDATION FAILED - Attempting repair ===");
                logger.warn("Error: {}", e.getMessage());
                try {
                    flyway.repair();
                    logger.info("=== FLYWAY REPAIR COMPLETED - Retrying validation ===");
                    flyway.validate();
                    logger.info("=== FLYWAY VALIDATION PASSED AFTER REPAIR ===");
                } catch (Exception repairError) {
                    logger.error("=== FLYWAY REPAIR/VALIDATION FAILED ===", repairError);
                    // Nếu repair vẫn fail, tắt validation tạm thời
                    logger.warn("Disabling validation to continue...");
                    flyway = Flyway.configure()
                            .dataSource(dataSource)
                            .locations(locations)
                            .baselineOnMigrate(baselineOnMigrate)
                            .validateOnMigrate(false)
                            .load();
                }
            }
        }

        // Chạy migration
        try {
            flyway.migrate();
            logger.info("=== FLYWAY MIGRATION COMPLETED ===");
        } catch (Exception e) {
            logger.error("=== FLYWAY MIGRATION FAILED ===", e);
            throw e;
        }

        logger.info("=== FLYWAY BEAN CREATED SUCCESSFULLY ===");
        return flyway;
    }
}
