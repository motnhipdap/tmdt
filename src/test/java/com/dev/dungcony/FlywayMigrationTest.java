package com.dev.dungcony;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:db/migration",
        "spring.flyway.baseline-on-migration=true"
})
class FlywayMigrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private Flyway flyway;

    @Test
    void checkFlywayMigrations() throws Exception {
        // Đợi một chút để Flyway có thời gian chạy (nếu có)
        Thread.sleep(1000);
        System.out.println("\n=== FLYWAY MIGRATION STATUS ===");

        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {

            // Kiểm tra xem bảng flyway_schema_history có tồn tại không
            boolean tableExists = false;
            try {
                ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) FROM information_schema.tables " +
                                "WHERE table_schema = DATABASE() AND table_name = 'flyway_schema_history'");
                if (rs.next() && rs.getInt(1) > 0) {
                    tableExists = true;
                }
            } catch (Exception e) {
                System.out.println("❌ Error checking flyway_schema_history table: " + e.getMessage());
            }

            if (tableExists) {
                System.out.println("✅ Bảng flyway_schema_history tồn tại");

                // Lấy danh sách các migration đã chạy
                ResultSet rs = stmt.executeQuery(
                        "SELECT version, description, type, installed_on, success " +
                                "FROM flyway_schema_history " +
                                "ORDER BY installed_rank");

                System.out.println("\n📋 Danh sách migrations đã chạy:");
                System.out.println("Version | Description | Type | Installed On | Success");
                System.out.println("--------------------------------------------------------");

                int count = 0;
                while (rs.next()) {
                    count++;
                    String version = rs.getString("version");
                    String description = rs.getString("description");
                    String type = rs.getString("type");
                    String installedOn = rs.getString("installed_on");
                    boolean success = rs.getBoolean("success");

                    String status = success ? "✅" : "❌";
                    System.out.println(String.format("%s | %s | %s | %s | %s",
                            version, description, type, installedOn, status));
                }

                if (count == 0) {
                    System.out.println("⚠️  Chưa có migration nào được chạy!");
                } else {
                    System.out.println("\n✅ Tổng số migrations đã chạy: " + count);
                }
            } else {
                System.out.println("❌ Bảng flyway_schema_history KHÔNG tồn tại!");
                System.out.println("   → Flyway chưa chạy migration nào hoặc có lỗi khi khởi tạo");
            }

            // Kiểm tra các bảng đã được tạo
            System.out.println("\n📊 Kiểm tra các bảng trong database:");
            ResultSet tables = stmt.executeQuery(
                    "SELECT table_name FROM information_schema.tables " +
                            "WHERE table_schema = DATABASE() " +
                            "AND table_type = 'BASE TABLE' " +
                            "ORDER BY table_name");

            int tableCount = 0;
            while (tables.next()) {
                tableCount++;
                System.out.println("  - " + tables.getString("table_name"));
            }

            if (tableCount == 0) {
                System.out.println("  ⚠️  Không có bảng nào trong database!");
            } else {
                System.out.println("\n✅ Tổng số bảng: " + tableCount);
            }
        }

        // Kiểm tra Flyway bean
        if (flyway != null) {
            System.out.println("\n✅ Flyway bean đã được khởi tạo");
            System.out.println("   Locations: " + flyway.getConfiguration().getLocations());
        } else {
            System.out.println("\n❌ Flyway bean không tồn tại!");
        }

        System.out.println("\n=== END ===");
    }
}
