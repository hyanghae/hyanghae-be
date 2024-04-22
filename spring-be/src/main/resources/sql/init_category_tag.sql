ALTER TABLE tag MODIFY tag_name TEXT;

INSERT INTO category (category_name) VALUES ('체험');
INSERT INTO category (category_name) VALUES ('휴식');
INSERT INTO category (category_name) VALUES ('전시');
INSERT INTO category (category_name) VALUES ('역사');
INSERT INTO category (category_name) VALUES ('음식');
INSERT INTO category (category_name) VALUES ('경치');

-- 카테고리 1
INSERT INTO tag (tag_name, category_id) VALUES ('액티비티', 1);
INSERT INTO tag (tag_name, category_id) VALUES ('체험시설', 1);
INSERT INTO tag (tag_name, category_id) VALUES ('산책', 1);
INSERT INTO tag (tag_name, category_id) VALUES ('등산', 1);

-- 카테고리 2
INSERT INTO tag (tag_name, category_id) VALUES ('숙박시설', 2);
INSERT INTO tag (tag_name, category_id) VALUES ('해수욕장', 2);
INSERT INTO tag (tag_name, category_id) VALUES ('계곡', 2);
INSERT INTO tag (tag_name, category_id) VALUES ('공원', 2);

-- 카테고리 3
INSERT INTO tag (tag_name, category_id) VALUES ('공연', 3);
INSERT INTO tag (tag_name, category_id) VALUES ('거리', 3);
INSERT INTO tag (tag_name, category_id) VALUES ('마을', 3);
INSERT INTO tag (tag_name, category_id) VALUES ('예술', 3);

-- 카테고리 4
INSERT INTO tag (tag_name, category_id) VALUES ('문화유적', 4);
INSERT INTO tag (tag_name, category_id) VALUES ('박물관', 4);
INSERT INTO tag (tag_name, category_id) VALUES ('자연유산', 4);
INSERT INTO tag (tag_name, category_id) VALUES ('종교유적', 4);

-- 카테고리 5
INSERT INTO tag (tag_name, category_id) VALUES ('맛집', 5);
INSERT INTO tag (tag_name, category_id) VALUES ('카페', 5);
INSERT INTO tag (tag_name, category_id) VALUES ('특산물', 5);
INSERT INTO tag (tag_name, category_id) VALUES ('야시장', 5);

-- 카테고리 6
INSERT INTO tag (tag_name, category_id) VALUES ('오션뷰', 6);
INSERT INTO tag (tag_name, category_id) VALUES ('도시뷰', 6);
INSERT INTO tag (tag_name, category_id) VALUES ('숲뷰', 6);
INSERT INTO tag (tag_name, category_id) VALUES ('전망대', 6);

