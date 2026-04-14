-- ============================================================
-- Seed data for Product module
-- Database: PostgreSQL
-- Run AFTER schema.sql
-- ============================================================

-- =========================== CATEGORIES ===========================

-- Level 0: Root categories
INSERT INTO tbl_categories (id, name, code, img_url, description, status, parent_id, is_leaf, level, path, version)
VALUES
    (1,  'Thời trang nam',    'MEN',    'https://placehold.co/200x200?text=Men',      'Quần áo nam',        'ACTIVE', NULL, FALSE, 0, '1',       0),
    (2,  'Thời trang nữ',    'WOMEN',  'https://placehold.co/200x200?text=Women',    'Quần áo nữ',         'ACTIVE', NULL, FALSE, 0, '2',       0),
    (3,  'Phụ kiện',         'ACC',    'https://placehold.co/200x200?text=Acc',      'Phụ kiện thời trang', 'ACTIVE', NULL, FALSE, 0, '3',       0),
    (4,  'Giày dép',         'SHOES',  'https://placehold.co/200x200?text=Shoes',    'Giày dép các loại',   'ACTIVE', NULL, FALSE, 0, '4',       0);

-- Level 1: Subcategories
INSERT INTO tbl_categories (id, name, code, img_url, description, status, parent_id, is_leaf, level, path, version)
VALUES
    (5,  'Áo thun nam',      'MTS',    'https://placehold.co/200x200?text=TShirt',   'Áo thun nam các loại','ACTIVE', 1, TRUE,  1, '1/5',     0),
    (6,  'Quần jeans nam',   'MJN',    'https://placehold.co/200x200?text=Jeans',    'Quần jeans nam',      'ACTIVE', 1, TRUE,  1, '1/6',     0),
    (7,  'Áo khoác nam',     'MJK',    'https://placehold.co/200x200?text=Jacket',   'Áo khoác nam',        'ACTIVE', 1, TRUE,  1, '1/7',     0),
    (8,  'Áo thun nữ',      'WTS',    'https://placehold.co/200x200?text=WTShirt',  'Áo thun nữ các loại','ACTIVE', 2, TRUE,  1, '2/8',     0),
    (9,  'Váy đầm',         'WDR',    'https://placehold.co/200x200?text=Dress',    'Váy đầm nữ',         'ACTIVE', 2, TRUE,  1, '2/9',     0),
    (10, 'Quần nữ',         'WPN',    'https://placehold.co/200x200?text=WPants',   'Quần nữ các loại',   'ACTIVE', 2, TRUE,  1, '2/10',    0),
    (11, 'Mũ & Nón',        'HAT',    'https://placehold.co/200x200?text=Hat',      'Mũ nón thời trang',  'ACTIVE', 3, TRUE,  1, '3/11',    0),
    (12, 'Túi xách',        'BAG',    'https://placehold.co/200x200?text=Bag',      'Túi xách các loại',  'ACTIVE', 3, TRUE,  1, '3/12',    0),
    (13, 'Giày thể thao',   'SNK',    'https://placehold.co/200x200?text=Sneaker',  'Giày sneaker',        'ACTIVE', 4, TRUE,  1, '4/13',    0),
    (14, 'Dép & Sandal',    'SDL',    'https://placehold.co/200x200?text=Sandal',   'Dép và sandal',       'ACTIVE', 4, TRUE,  1, '4/14',    0);

SELECT setval('tbl_categories_id_seq', 14);

-- =========================== PROVIDERS ===========================

INSERT INTO tbl_providers (id, name, code, description, email, phone, status, logo, version)
VALUES
    (1,  'Nike',        'NIKE',   'Thương hiệu thể thao hàng đầu thế giới',     'contact@nike.com',        '0901000001', 'FAMOUS',   'https://placehold.co/100x100?text=Nike',      0),
    (2,  'Adidas',      'ADIDAS', 'Thương hiệu thể thao toàn cầu từ Đức',       'contact@adidas.com',      '0901000002', 'FAMOUS',   'https://placehold.co/100x100?text=Adidas',    0),
    (3,  'Uniqlo',      'UNIQLO', 'Thời trang cơ bản chất lượng cao từ Nhật',    'contact@uniqlo.com',      '0901000003', 'FAMOUS',   'https://placehold.co/100x100?text=Uniqlo',    0),
    (4,  'Zara',        'ZARA',   'Thời trang nhanh đến từ Tây Ban Nha',         'contact@zara.com',        '0901000004', 'FAMOUS',   'https://placehold.co/100x100?text=Zara',      0),
    (5,  'H&M',         'HM',     'Thời trang bình dân từ Thụy Điển',            'contact@hm.com',          '0901000005', 'ACTIVE',   'https://placehold.co/100x100?text=HM',        0),
    (6,  'Coolmate',    'COOL',   'Thương hiệu thời trang nam Việt Nam',         'hello@coolmate.me',       '0901000006', 'ACTIVE',   'https://placehold.co/100x100?text=Coolmate',  0),
    (7,  'Routine',     'RTN',    'Thời trang nam thiết kế tối giản',            'contact@routine.vn',      '0901000007', 'ACTIVE',   'https://placehold.co/100x100?text=Routine',   0),
    (8,  'Ivy Moda',    'IVY',    'Thời trang nữ cao cấp Việt Nam',              'contact@ivymoda.com',     '0901000008', 'ACTIVE',   'https://placehold.co/100x100?text=IvyModa',   0),
    (9,  'Vans',        'VANS',   'Giày thời trang đường phố từ Mỹ',            'contact@vans.com',        '0901000009', 'ACTIVE',   'https://placehold.co/100x100?text=Vans',      0),
    (10, 'Converse',    'CVS',    'Giày thời trang biểu tượng từ Mỹ',           'contact@converse.com',    '0901000010', 'FAMOUS',   'https://placehold.co/100x100?text=Converse',  0);

SELECT setval('tbl_providers_id_seq', 10);

-- =========================== SIZES ===========================

INSERT INTO tbl_sizes (id, size, weight, height)
VALUES
    (1, 'S',    0.2, 65),
    (2, 'M',    0.25, 70),
    (3, 'L',    0.3, 75),
    (4, 'XL',   0.35, 80),
    (5, 'XXL',  0.4, 85),
    (6, 'XXXL', 0.45, 90);

SELECT setval('tbl_sizes_id_seq', 6);

-- =========================== PRODUCTS ===========================

INSERT INTO tbl_products (id, name, code, description, quantity_sold, price, status, rated, category_id, provider_id, version, img, video)
VALUES
    -- Áo thun nam (category_id = 5)
    (1,  'Áo thun nam cổ tròn basic',         'MTS-001', 'Áo thun cotton 100% thoáng mát, form regular fit',                   1250, 299000,  'BESTSELLER',   4.8, 5, 6, 0, 'https://placehold.co/400x400?text=Tshirt+Basic',      NULL),
    (2,  'Áo thun nam Dri-FIT',               'MTS-002', 'Áo thun thể thao công nghệ Dri-FIT thoát ẩm nhanh',                  870, 890000,  'ACTIVE',       4.5, 5, 1, 0, 'https://placehold.co/400x400?text=Dri-FIT',           NULL),
    (3,  'Áo thun nam Trefoil',               'MTS-003', 'Áo thun nam logo Trefoil cổ điển phong cách streetwear',              620, 750000,  'ACTIVE',       4.3, 5, 2, 0, 'https://placehold.co/400x400?text=Trefoil',           NULL),
    (4,  'Áo thun nam Supima Cotton',          'MTS-004', 'Áo thun Supima Cotton cao cấp mềm mại co giãn tốt',                  450, 390000,  'ACTIVE',       4.6, 5, 3, 0, 'https://placehold.co/400x400?text=Supima',            NULL),
    (5,  'Áo thun nam oversize graphic',       'MTS-005', 'Áo thun oversize in họa tiết graphic trẻ trung',                     310, 350000,  'ON_SALE',      4.2, 5, 7, 0, 'https://placehold.co/400x400?text=Graphic+Tee',       NULL),

    -- Quần jeans nam (category_id = 6)
    (6,  'Quần jeans nam slim fit',            'MJN-001', 'Quần jeans co giãn form slim fit trẻ trung',                         980, 590000,  'BESTSELLER',   4.7, 6, 4, 0, 'https://placehold.co/400x400?text=Slim+Jeans',        NULL),
    (7,  'Quần jeans nam straight',            'MJN-002', 'Quần jeans ống đứng phong cách cổ điển',                             560, 490000,  'ACTIVE',       4.4, 6, 5, 0, 'https://placehold.co/400x400?text=Straight+Jeans',    NULL),
    (8,  'Quần jeans nam regular',             'MJN-003', 'Quần jeans regular fit thoải mái cho mọi hoạt động',                 720, 450000,  'ACTIVE',       4.5, 6, 3, 0, 'https://placehold.co/400x400?text=Regular+Jeans',     NULL),

    -- Áo khoác nam (category_id = 7)
    (9,  'Áo khoác gió nam nhẹ',              'MJK-001', 'Áo khoác gió siêu nhẹ chống nước nhẹ tiện lợi',                     430, 690000,  'ACTIVE',       4.6, 7, 1, 0, 'https://placehold.co/400x400?text=Windbreaker',       NULL),
    (10, 'Áo khoác hoodie nam',               'MJK-002', 'Áo hoodie nỉ bông ấm áp có mũ trùm đầu',                           890, 550000,  'BESTSELLER',   4.8, 7, 6, 0, 'https://placehold.co/400x400?text=Hoodie',            NULL),
    (11, 'Áo khoác bomber nam',               'MJK-003', 'Áo bomber phong cách thời trang đường phố',                          280, 790000,  'ACTIVE',       4.3, 7, 7, 0, 'https://placehold.co/400x400?text=Bomber',            NULL),

    -- Áo thun nữ (category_id = 8)
    (12, 'Áo thun nữ cổ tròn basic',          'WTS-001', 'Áo thun nữ cotton mềm mại thoáng mát',                              1100, 250000, 'BESTSELLER',   4.7, 8, 5, 0, 'https://placehold.co/400x400?text=W+Basic+Tee',      NULL),
    (13, 'Áo thun nữ crop top',               'WTS-002', 'Áo crop top trẻ trung năng động phối đồ dễ dàng',                    670, 280000,  'ACTIVE',       4.4, 8, 4, 0, 'https://placehold.co/400x400?text=Crop+Top',          NULL),
    (14, 'Áo thun nữ peplum',                 'WTS-003', 'Áo peplum thanh lịch phù hợp đi làm đi chơi',                       340, 420000,  'ACTIVE',       4.5, 8, 8, 0, 'https://placehold.co/400x400?text=Peplum',            NULL),

    -- Váy đầm (category_id = 9)
    (15, 'Đầm suông công sở',                 'WDR-001', 'Đầm suông thanh lịch phù hợp môi trường công sở',                   520, 680000,  'ACTIVE',       4.6, 9, 8, 0, 'https://placehold.co/400x400?text=Office+Dress',      NULL),
    (16, 'Đầm maxi hoa nhí',                  'WDR-002', 'Đầm maxi họa tiết hoa nhí phong cách vintage',                      380, 590000,  'ON_SALE',      4.3, 9, 4, 0, 'https://placehold.co/400x400?text=Maxi+Dress',        NULL),
    (17, 'Đầm body ôm sát',                   'WDR-003', 'Đầm bodycon tôn dáng quyến rũ cho buổi tiệc',                       290, 750000,  'ACTIVE',       4.4, 9, 8, 0, 'https://placehold.co/400x400?text=Bodycon',           NULL),

    -- Quần nữ (category_id = 10)
    (18, 'Quần baggy nữ',                     'WPN-001', 'Quần baggy ống rộng thoải mái thời trang',                           810, 390000,  'ACTIVE',       4.5, 10, 5, 0, 'https://placehold.co/400x400?text=Baggy',            NULL),
    (19, 'Quần legging nữ',                   'WPN-002', 'Quần legging co giãn 4 chiều tập gym yoga',                          950, 350000,  'BESTSELLER',   4.7, 10, 1, 0, 'https://placehold.co/400x400?text=Legging',          NULL),

    -- Mũ & Nón (category_id = 11)
    (20, 'Nón lưỡi trai logo',                'HAT-001', 'Nón lưỡi trai thêu logo thời trang unisex',                         1500, 450000, 'BESTSELLER',   4.6, 11, 1, 0, 'https://placehold.co/400x400?text=Cap+Nike',          NULL),
    (21, 'Mũ bucket unisex',                  'HAT-002', 'Mũ bucket phong cách đường phố che nắng tốt',                       680, 350000,  'ACTIVE',       4.3, 11, 2, 0, 'https://placehold.co/400x400?text=Bucket+Hat',        NULL),

    -- Túi xách (category_id = 12)
    (22, 'Balo thời trang nam',               'BAG-001', 'Balo laptop chống nước ngăn chứa rộng rãi',                          730, 890000,  'ACTIVE',       4.5, 12, 1, 0, 'https://placehold.co/400x400?text=Backpack',          NULL),
    (23, 'Túi tote nữ canvas',                'BAG-002', 'Túi tote vải canvas phong cách minimalist',                          420, 290000,  'ACTIVE',       4.4, 12, 3, 0, 'https://placehold.co/400x400?text=Tote+Bag',          NULL),

    -- Giày thể thao (category_id = 13)
    (24, 'Giày Air Max 90',                   'SNK-001', 'Giày thể thao huyền thoại đệm Air thoải mái',                       1100, 3290000, 'BESTSELLER',  4.9, 13, 1, 0, 'https://placehold.co/400x400?text=Air+Max+90',        NULL),
    (25, 'Giày Ultraboost',                   'SNK-002', 'Giày chạy bộ công nghệ Boost êm ái nhẹ nhàng',                       870, 3890000, 'ACTIVE',      4.8, 13, 2, 0, 'https://placehold.co/400x400?text=Ultraboost',        NULL),
    (26, 'Giày Old Skool',                    'SNK-003', 'Giày classic với sọc trắng biểu tượng bên hông',                     950, 1890000, 'BESTSELLER',  4.7, 13, 9, 0, 'https://placehold.co/400x400?text=Old+Skool',         NULL),
    (27, 'Giày Chuck Taylor',                 'SNK-004', 'Giày Converse cổ cao biểu tượng thời trang',                         1200, 1590000, 'BESTSELLER', 4.8, 13, 10, 0, 'https://placehold.co/400x400?text=Chuck+Taylor',     NULL),

    -- Dép & Sandal (category_id = 14)
    (28, 'Dép quai ngang Nike',               'SDL-001', 'Dép quai ngang êm ái cho ngày nghỉ thoải mái',                      1800, 790000,  'ACTIVE',      4.5, 14, 1, 0, 'https://placehold.co/400x400?text=Nike+Slide',        NULL),
    (29, 'Sandal Adilette',                   'SDL-002', 'Sandal quai ngang phong cách thể thao cổ điển',                      1300, 690000,  'ACTIVE',      4.4, 14, 2, 0, 'https://placehold.co/400x400?text=Adilette',          NULL),
    (30, 'Dép xỏ ngón nam',                   'SDL-003', 'Dép xỏ ngón nhẹ nhàng tiện lợi cho mùa hè',                         600, 190000,  'ON_SALE',     4.1, 14, 6, 0, 'https://placehold.co/400x400?text=Flip+Flop',         NULL);

SELECT setval('tbl_products_id_seq', 30);

-- =========================== ITEMS (product-size inventory) ===========================

-- Áo thun nam basic (product 1) - all sizes
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (1, 1, 120, 'AVAILABLE'), (1, 2, 200, 'AVAILABLE'), (1, 3, 180, 'AVAILABLE'),
    (1, 4, 90,  'AVAILABLE'), (1, 5, 40,  'AVAILABLE'), (1, 6, 15,  'AVAILABLE');

-- Áo thun Dri-FIT (product 2)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (2, 1, 60,  'AVAILABLE'), (2, 2, 100, 'AVAILABLE'), (2, 3, 80,  'AVAILABLE'),
    (2, 4, 50,  'AVAILABLE'), (2, 5, 20,  'AVAILABLE');

-- Áo thun Trefoil (product 3)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (3, 1, 45,  'AVAILABLE'), (3, 2, 70,  'AVAILABLE'), (3, 3, 55,  'AVAILABLE'),
    (3, 4, 30,  'AVAILABLE');

-- Áo thun Supima (product 4)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (4, 1, 80,  'AVAILABLE'), (4, 2, 120, 'AVAILABLE'), (4, 3, 100, 'AVAILABLE'),
    (4, 4, 60,  'AVAILABLE'), (4, 5, 25,  'AVAILABLE');

-- Áo thun oversize graphic (product 5)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (5, 2, 50,  'AVAILABLE'), (5, 3, 70,  'AVAILABLE'), (5, 4, 80,  'AVAILABLE'),
    (5, 5, 40,  'AVAILABLE'), (5, 6, 20,  'AVAILABLE');

-- Quần jeans slim fit (product 6)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (6, 1, 40,  'AVAILABLE'), (6, 2, 90,  'AVAILABLE'), (6, 3, 110, 'AVAILABLE'),
    (6, 4, 60,  'AVAILABLE'), (6, 5, 20,  'AVAILABLE');

-- Quần jeans straight (product 7)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (7, 2, 55, 'AVAILABLE'), (7, 3, 70, 'AVAILABLE'), (7, 4, 45, 'AVAILABLE');

-- Quần jeans regular (product 8)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (8, 1, 30, 'AVAILABLE'), (8, 2, 80, 'AVAILABLE'), (8, 3, 100, 'AVAILABLE'),
    (8, 4, 70, 'AVAILABLE'), (8, 5, 35, 'AVAILABLE');

-- Áo khoác gió (product 9)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (9, 2, 40, 'AVAILABLE'), (9, 3, 60, 'AVAILABLE'), (9, 4, 50, 'AVAILABLE');

-- Áo hoodie (product 10)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (10, 1, 30, 'AVAILABLE'), (10, 2, 80, 'AVAILABLE'), (10, 3, 110, 'AVAILABLE'),
    (10, 4, 70, 'AVAILABLE'), (10, 5, 25, 'AVAILABLE');

-- Áo bomber (product 11)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (11, 2, 35, 'AVAILABLE'), (11, 3, 50, 'AVAILABLE'), (11, 4, 40, 'AVAILABLE');

-- Áo thun nữ basic (product 12)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (12, 1, 150, 'AVAILABLE'), (12, 2, 180, 'AVAILABLE'), (12, 3, 120, 'AVAILABLE'),
    (12, 4, 50,  'AVAILABLE');

-- Áo crop top (product 13)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (13, 1, 90, 'AVAILABLE'), (13, 2, 100, 'AVAILABLE'), (13, 3, 60, 'AVAILABLE');

-- Áo peplum (product 14)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (14, 1, 40, 'AVAILABLE'), (14, 2, 65, 'AVAILABLE'), (14, 3, 50, 'AVAILABLE'),
    (14, 4, 20, 'AVAILABLE');

-- Đầm suông (product 15)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (15, 1, 35, 'AVAILABLE'), (15, 2, 55, 'AVAILABLE'), (15, 3, 45, 'AVAILABLE');

-- Đầm maxi (product 16)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (16, 1, 25, 'AVAILABLE'), (16, 2, 40, 'AVAILABLE'), (16, 3, 35, 'AVAILABLE');

-- Đầm body (product 17)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (17, 1, 30, 'AVAILABLE'), (17, 2, 45, 'AVAILABLE'), (17, 3, 20, 'AVAILABLE');

-- Quần baggy nữ (product 18)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (18, 1, 60, 'AVAILABLE'), (18, 2, 90, 'AVAILABLE'), (18, 3, 70, 'AVAILABLE'),
    (18, 4, 30, 'AVAILABLE');

-- Quần legging (product 19)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (19, 1, 100, 'AVAILABLE'), (19, 2, 150, 'AVAILABLE'), (19, 3, 120, 'AVAILABLE'),
    (19, 4, 60,  'AVAILABLE');

-- Nón lưỡi trai (product 20) — one-size, map to M
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (20, 2, 300, 'AVAILABLE');

-- Mũ bucket (product 21)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (21, 2, 200, 'AVAILABLE');

-- Balo (product 22) — one-size, map to L
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (22, 3, 150, 'AVAILABLE');

-- Túi tote (product 23)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (23, 3, 180, 'AVAILABLE');

-- Giày Air Max 90 (product 24) — shoe sizes mapped: S=38, M=40, L=42, XL=44
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (24, 1, 40, 'AVAILABLE'), (24, 2, 80, 'AVAILABLE'), (24, 3, 90, 'AVAILABLE'),
    (24, 4, 50, 'AVAILABLE');

-- Giày Ultraboost (product 25)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (25, 1, 35, 'AVAILABLE'), (25, 2, 60, 'AVAILABLE'), (25, 3, 70, 'AVAILABLE'),
    (25, 4, 40, 'AVAILABLE');

-- Giày Old Skool (product 26)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (26, 1, 50, 'AVAILABLE'), (26, 2, 90, 'AVAILABLE'), (26, 3, 80, 'AVAILABLE'),
    (26, 4, 45, 'AVAILABLE');

-- Giày Chuck Taylor (product 27)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (27, 1, 60, 'AVAILABLE'), (27, 2, 100, 'AVAILABLE'), (27, 3, 110, 'AVAILABLE'),
    (27, 4, 55, 'AVAILABLE'), (27, 5, 20, 'AVAILABLE');

-- Dép quai ngang Nike (product 28)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (28, 1, 80, 'AVAILABLE'), (28, 2, 120, 'AVAILABLE'), (28, 3, 100, 'AVAILABLE'),
    (28, 4, 60, 'AVAILABLE');

-- Sandal Adilette (product 29)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (29, 1, 70, 'AVAILABLE'), (29, 2, 100, 'AVAILABLE'), (29, 3, 90, 'AVAILABLE'),
    (29, 4, 50, 'AVAILABLE');

-- Dép xỏ ngón (product 30)
INSERT INTO tbl_items (product_id, size_id, quantity, status) VALUES
    (30, 2, 0, 'OUT_OF_STOCK'), (30, 3, 80, 'AVAILABLE'), (30, 4, 60, 'AVAILABLE');
