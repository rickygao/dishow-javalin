DROP DATABASE IF EXISTS dishow;
CREATE DATABASE IF NOT EXISTS dishow;
USE dishow;

CREATE TABLE IF NOT EXISTS universities (
  id INT PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  location VARCHAR(64) NULL,
  longitude DECIMAL(10, 7) NULL,
  latitude DECIMAL(10, 7) NULL
);
INSERT INTO universities(id, name, location, longitude, latitude)
VALUES(1, '天津大学北洋园校区', '天津市津南区海河教育园区天津大学北洋园校区', 117.3147580, 38.9982858);
INSERT INTO universities(id, name, location, longitude, latitude)
VALUES(2, '天津大学卫津路校区', '天津市南开区学府街道天津大学卫津路校区', 117.1703830, 39.1101440);

CREATE TABLE IF NOT EXISTS canteens (
  id INT PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  location VARCHAR(64) NULL,
  longitude DECIMAL(10, 7) NULL,
  latitude DECIMAL(10, 7) NULL,
  uid INT NOT NULL,
  FOREIGN KEY (uid) REFERENCES universities(id) ON DELETE RESTRICT
);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(101, '梅园餐厅（学一食堂）', NULL, 117.315904, 39.001469, 1);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(102, '兰园餐厅（学二食堂）', NULL, 117.317684, 38.998513, 1);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(103, '棠园餐厅（学三食堂）', NULL, 117.312020, 38.998459, 1);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(104, '竹园餐厅（学四食堂）', NULL, 117.315815, 38.993515, 1);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(105, '桃园餐厅（学五食堂）', NULL, 117.312564, 38.995922, 1);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(106, '留园餐厅（留学生食堂）', NULL, 117.315794, 39.002539, 1);
INSERT INTO canteens(id, name, location, longitude, latitude, uid)
VALUES(107, '菊园餐厅（清真食堂）', NULL, 117.316131, 39.002081, 1);

CREATE TABLE IF NOT EXISTS catalogs (
  id INT PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  location VARCHAR(16) NULL,
  cid INT NOT NULL,
  FOREIGN KEY (cid) REFERENCES canteens(id) ON DELETE RESTRICT
);
LOAD DATA LOCAL INFILE 'catalogs.csv' INTO TABLE catalogs
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\';
SHOW WARNINGS;

CREATE TABLE IF NOT EXISTS dishes (
  id INT PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  price DECIMAL(6, 2) NULL,
  cid INT NOT NULL,
  FOREIGN KEY (cid) REFERENCES catalogs(id) ON DELETE RESTRICT
);
LOAD DATA LOCAL INFILE 'dishes.csv' INTO TABLE dishes
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\';
SHOW WARNINGS;

CREATE TABLE IF NOT EXISTS catalog_comments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  star INT NOT NULL,
  detail VARCHAR(255) NULL,
  cid INT NOT NULL,
  FOREIGN KEY (cid) REFERENCES catalogs(id) ON DELETE RESTRICT
);
