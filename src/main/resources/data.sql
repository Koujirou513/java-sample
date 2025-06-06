-- 管理者ユーザー挿入
INSERT INTO users (name, email, password, is_admin, is_active) VALUES
    ('admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl.k5uGdTCS', TRUE, TRUE)
    ('staff1', 'staff1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl.k5uGdTCS', FALSE, TRUE);

INSERT INTO items (name, price, shelf_life_day, description, is_active, created_at, updated_at) VALUES 
    ('ホワイトビール', 900, 15, '爽やかで軽やかな口当たりのベルギースタイルホワイトビール', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ラガー', 800, 15, 'すっきりとした飲み口で親しみやすいプレミアムラガー', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ペールエール', 1000, 15, 'ホップの香りと苦味が特徴のアメリカンペールエール', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('フルーツビール', 1000, 15, '季節のフルーツを使用した華やかなフルーツビール', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('黒ビール', 1200, 15, '深いコクと香ばしいローストの風味が特徴のプレミアム黒ビール', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('IPA', 900, 15, 'ホップの苦味と香りが特徴のインディア・ペールエール', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO sales_weather (target_date, condition, created_at) VALUES 
    (CURRENT_DATE - INTERVAL '7 days', 1, CURRENT_TIMESTAMP),  
    (CURRENT_DATE - INTERVAL '6 days', 2, CURRENT_TIMESTAMP),  
    (CURRENT_DATE - INTERVAL '5 days', 1, CURRENT_TIMESTAMP),  
    (CURRENT_DATE - INTERVAL '4 days', 3, CURRENT_TIMESTAMP),  
    (CURRENT_DATE - INTERVAL '3 days', 2, CURRENT_TIMESTAMP),  
    (CURRENT_DATE - INTERVAL '2 days', 1, CURRENT_TIMESTAMP),  
    (CURRENT_DATE - INTERVAL '1 days', 1, CURRENT_TIMESTAMP);

-- 販売実績データ挿入（過去1週間分）
INSERT INTO sales (target_date, created_by, created_at, updated_at) VALUES 
    (CURRENT_DATE - INTERVAL '7 days', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CURRENT_DATE - INTERVAL '6 days', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CURRENT_DATE - INTERVAL '5 days', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CURRENT_DATE - INTERVAL '4 days', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CURRENT_DATE - INTERVAL '3 days', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CURRENT_DATE - INTERVAL '2 days', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (CURRENT_DATE - INTERVAL '1 days', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 7日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (1, 1, 25),  -- ホワイトビール
    (1, 2, 35),  -- ラガー
    (1, 3, 20),  -- ペールエール
    (1, 4, 15),  -- フルーツビール
    (1, 5, 10),  -- 黒ビール
    (1, 6, 18);  -- IPA

-- 6日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (2, 1, 20),
    (2, 2, 28),
    (2, 3, 16),
    (2, 4, 12),
    (2, 5, 8),
    (2, 6, 15);

-- 5日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (3, 1, 30),
    (3, 2, 40),
    (3, 3, 22),
    (3, 4, 18),
    (3, 5, 12),
    (3, 6, 20);

-- 4日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (4, 1, 15),
    (4, 2, 20),
    (4, 3, 12),
    (4, 4, 8),
    (4, 5, 6),
    (4, 6, 10);

-- 3日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (5, 1, 22),
    (5, 2, 32),
    (5, 3, 18),
    (5, 4, 14),
    (5, 5, 9),
    (5, 6, 16);

-- 2日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (6, 1, 28),
    (6, 2, 38),
    (6, 3, 24),
    (6, 4, 16),
    (6, 5, 11),
    (6, 6, 19);

-- 1日前の販売実績
INSERT INTO sales_items (sales_id, item_id, quantity) VALUES 
    (7, 1, 26),
    (7, 2, 36),
    (7, 3, 21),
    (7, 4, 17),
    (7, 5, 13),
    (7, 6, 21);