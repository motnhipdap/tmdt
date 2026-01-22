-- Script để kiểm tra Flyway migrations đã chạy
-- Chạy script này trong MySQL để xem các migration đã được thực thi

-- 1. Kiểm tra bảng flyway_schema_history có tồn tại không
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✅ Bảng flyway_schema_history tồn tại'
        ELSE '❌ Bảng flyway_schema_history KHÔNG tồn tại'
    END AS status
FROM information_schema.tables 
WHERE table_schema = DATABASE() 
AND table_name = 'flyway_schema_history';

-- 2. Xem danh sách các migration đã chạy
SELECT 
    installed_rank AS 'Thứ tự',
    version AS 'Version',
    description AS 'Mô tả',
    type AS 'Loại',
    script AS 'File SQL',
    installed_on AS 'Thời gian chạy',
    execution_time AS 'Thời gian (ms)',
    success AS 'Thành công'
FROM flyway_schema_history
ORDER BY installed_rank;

-- 3. Đếm số migration đã chạy
SELECT 
    COUNT(*) AS 'Tổng số migrations',
    SUM(CASE WHEN success = 1 THEN 1 ELSE 0 END) AS 'Thành công',
    SUM(CASE WHEN success = 0 THEN 1 ELSE 0 END) AS 'Thất bại'
FROM flyway_schema_history;

-- 4. Kiểm tra các bảng đã được tạo trong database
SELECT 
    table_name AS 'Tên bảng',
    table_rows AS 'Số dòng',
    create_time AS 'Thời gian tạo'
FROM information_schema.tables 
WHERE table_schema = DATABASE() 
AND table_type = 'BASE TABLE'
AND table_name != 'flyway_schema_history'
ORDER BY table_name;
