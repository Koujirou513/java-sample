-- Phase1: ビール商品のテーブル作成と初期データ

-- テーブルが存在しない場合のみ作成
CREATE TABLE IF NOT EXISTS beers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL
);
