CREATE DATABASE IF NOT EXISTS maxx_toy_shop
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE maxx_toy_shop;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categories_name (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image VARCHAR(500),
    category_id BIGINT,
    tag VARCHAR(100),
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_products_category (category_id),
    CONSTRAINT fk_products_category
      FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS customer_orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    subtotal DECIMAL(10,2) NOT NULL,
    delivery_charge DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(100),
    shipping_address VARCHAR(500) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(20),
    phone VARCHAR(50),
    customer_email VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_orders_user (user_id),
    CONSTRAINT fk_orders_user
      FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_order_items_order (order_id),
    KEY idx_order_items_product (product_id),
    CONSTRAINT fk_order_items_order
      FOREIGN KEY (order_id) REFERENCES customer_orders(id)
      ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product
      FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB;

-- Initial categories
INSERT IGNORE INTO categories (name) VALUES
  ('Educational'),
  ('Remote Control'),
  ('Dolls'),
  ('Action Figures'),
  ('Soft Toys'),
  ('Board Games'),
  ('Outdoor Toys'),
  ('Pretend Play'),
  ('Electronic Toys');

-- Initial products
INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'LEGO Set STAR WARS', 'Build and play with a STAR WARS LEGO set.',
       799.00, 50, 'assets/LEGO STAR WARS.jpg', id, 'Popular', NOW()
FROM categories WHERE name='Educational'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='LEGO Set STAR WARS');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'HotWheels', 'Exciting remote control HotWheels toy.',
       299.00, 50, 'assets/Hotwheels.jpg', id, 'New', NOW()
FROM categories WHERE name='Remote Control'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='HotWheels');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'Barbie', 'Classic Barbie doll for creative play.',
       599.00, 50, 'assets/Barbie.jpg', id, NULL, NOW()
FROM categories WHERE name='Dolls'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='Barbie');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'Batman', 'Batman action figure for superhero adventures.',
       999.00, 50, 'assets/Batman.jpg', id, 'Hot', NOW()
FROM categories WHERE name='Action Figures'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='Batman');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'RC Helicopter', 'Remote control helicopter for indoor and outdoor fun.',
       1199.00, 50, 'assets/Rc helicopter.jpg', id, NULL, NOW()
FROM categories WHERE name='Educational'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='RC Helicopter');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'Teddy Bear', 'Soft and cuddly teddy bear.',
       399.00, 50, 'assets/Teddybear.jpg', id, NULL, NOW()
FROM categories WHERE name='Soft Toys'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='Teddy Bear');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'MONOPOLY', 'Classic family board game.',
       699.00, 50, 'assets/Monopoly.avif', id, NULL, NOW()
FROM categories WHERE name='Board Games'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='MONOPOLY');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'Cricket Bat', 'Fun cricket bat for outdoor play.',
       1099.00, 50, 'assets/Cricket bat.jpg', id, NULL, NOW()
FROM categories WHERE name='Outdoor Toys'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='Cricket Bat');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'Mini Kitchen Set', 'Pretend-play mini kitchen set.',
       849.00, 50, 'assets/mini kitchen set.jpg', id, NULL, NOW()
FROM categories WHERE name='Pretend Play'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='Mini Kitchen Set');

INSERT INTO products
  (name, description, price, stock, image, category_id, tag, created_at)
SELECT 'Robot Buddy', 'Interactive electronic toy robot.',
       1899.00, 50, 'assets/robot buddy.jpg', id, 'Featured', NOW()
FROM categories WHERE name='Electronic Toys'
AND NOT EXISTS (SELECT 1 FROM products WHERE name='Robot Buddy');
