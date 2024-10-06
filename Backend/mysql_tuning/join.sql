-- 기존 테이블 삭제 (만약 존재한다면)
DROP TABLE IF EXISTS template_corner;
DROP TABLE IF EXISTS shop_template;
DROP TABLE IF EXISTS template;
DROP TABLE IF EXISTS corner;
DROP TABLE IF EXISTS shop;
DROP TABLE IF EXISTS CATEGORY;

-- 테이블 생성
CREATE TABLE shop (
                      shop_no INT AUTO_INCREMENT PRIMARY KEY,
                      shop_name VARCHAR(255) NOT NULL
);

CREATE TABLE corner (
                        corner_no INT AUTO_INCREMENT PRIMARY KEY,
                        corner_name VARCHAR(255) NOT NULL,
                        corner_id VARCHAR(50) NOT NULL
);

CREATE TABLE template (
                          template_no INT AUTO_INCREMENT PRIMARY KEY,
                          template_name VARCHAR(255) NOT NULL,
                          TEMPLATE_ID VARCHAR(50) NOT NULL
);

CREATE TABLE shop_template (
                               shop_template_no INT AUTO_INCREMENT PRIMARY KEY,
                               shop_shop_no INT,
                               shop_template_name VARCHAR(255) NOT NULL,
                               template_template_no INT,
                               FOREIGN KEY (shop_shop_no) REFERENCES shop(shop_no),
                               FOREIGN KEY (template_template_no) REFERENCES template(template_no)
);

CREATE TABLE template_corner (
                                 template_corner_no INT AUTO_INCREMENT PRIMARY KEY,
                                 corner_corner_no INT,
                                 template_template_no INT,
                                 template_corner_name VARCHAR(255) NOT NULL,
                                 FOREIGN KEY (corner_corner_no) REFERENCES corner(corner_no),
                                 FOREIGN KEY (template_template_no) REFERENCES template(template_no)
);


CREATE TABLE CATEGORY (
                          CATEGORY_NO INT PRIMARY KEY,
                          CATEGORY_NAME VARCHAR(255) NOT NULL
);


-- 데이터 삽입
INSERT INTO shop (shop_name, shop_no) VALUES ('매장1', 1);

INSERT INTO corner (corner_name, corner_no, corner_id)
VALUES ('코너1', 1, 'CORNER_1'),
       ('코너2', 2, 'CORNER_2');

INSERT INTO template (template_name, template_no, TEMPLATE_ID)
VALUES ('템플릿1', 1, 'TEMPLATE_1');

INSERT INTO shop_template (shop_shop_no, shop_template_name, template_template_no, shop_template_no)
VALUES (1, '매장1 - 템플릿1', 1, 1);

INSERT INTO template_corner (corner_corner_no, template_template_no, template_corner_name, template_corner_no)
VALUES (1, 1, '템플릿1 - 코너1', 1);

INSERT INTO template (template_name, template_no, TEMPLATE_ID)
VALUES ('템플릿2', 2, 'TEMPLATE_2');

INSERT INTO shop_template (shop_shop_no, shop_template_name, template_template_no, shop_template_no)
VALUES (1, '매장1 - 템플릿2', 2, 2);

INSERT INTO template_corner (corner_corner_no, template_template_no, template_corner_name, template_corner_no)
VALUES (1, 2, '템플릿2 - 코너1', 2),
       (2, 2, '템플릿2 - 코너2', 3);

INSERT INTO CATEGORY (CATEGORY_NO, CATEGORY_NAME) VALUES
                                                      (1, '가전'),
                                                      (2, '의류'),
                                                      (3, '식품');
