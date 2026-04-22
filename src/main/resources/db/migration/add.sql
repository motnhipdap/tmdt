-- Xem tất cả constraint của bảng
SELECT constraint_name, constraint_type
FROM information_schema.table_constraints
WHERE table_name = 'tbl_order_items';

-- Xem chi tiết foreign key constraint
SELECT tc.constraint_name,
       tc.table_name,
       kcu.column_name,
       ccu.table_name  AS foreign_table_name,
       ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints tc
         JOIN information_schema.key_column_usage kcu
              ON tc.constraint_name = kcu.constraint_name
         JOIN information_schema.constraint_column_usage ccu
              ON ccu.constraint_name = tc.constraint_name
WHERE tc.table_name = 'tbl_order_items';

DELETE
FROM tbl_orders
WHERE id IN (7, 9, 11, 14);