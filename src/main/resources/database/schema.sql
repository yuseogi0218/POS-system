DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_detail`;
DROP TABLE IF EXISTS `order_table`;
DROP TABLE IF EXISTS `trade`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;
DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
                           `id`    BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `email` VARCHAR(255) NOT NULL,
                           `name`  VARCHAR(10) NOT NULL,
                           `phone` VARCHAR(11) NOT NULL
);

CREATE TABLE `store` (
                      `id`            BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      `owner_user_id` BIGINT(20) NOT NULL,
                      `name`          VARCHAR(255) NOT NULL,
                      `pos_grade`     ENUM ('BRONZE', 'SILVER', 'GOLD') NOT NULL,
                      FOREIGN KEY (`owner_user_id`) REFERENCES user_table(`id`),
                      UNIQUE (`owner_user_id`)
);

CREATE TABLE `trade_device` (
                             `id`       BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `store_id` BIGINT(20) NOT NULL,
                             FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);

CREATE TABLE `product` (
                        `id`         BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `store_id`   BIGINT(20) NOT NULL,
                        `name`       VARCHAR(255) NOT NULL,
                        `category`   ENUM ('MAIN_MENU', 'SUB_MENU', 'DRINK') NOT NULL,
                        `price`      INT NOT NULL,
                        `stock`      INT NOT NULL,
                        `base_stock` INT NOT NULL,
                        `is_deleted` VARCHAR(1) NOT NULL,
                        CHECK ( `is_deleted` IN ('Y', 'N') ),
                        FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);

CREATE TABLE `trade` (
                      `id`              BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      `store_id`        BIGINT(20) NOT NULL,
                      `trade_device_id` BIGINT(20) NOT NULL,
                      `trade_amount`    INT NOT NULL,
                      `created_at`      DATETIME(6) NOT NULL,
                      `is_completed`    VARCHAR(1) NOT NULL,
                      CHECK ( `is_completed` IN ('Y', 'N') ),
                      FOREIGN KEY (`store_id`) REFERENCES store(`id`),
                      FOREIGN KEY (`trade_device_id`) REFERENCES trade_device(`id`)
);

CREATE TABLE `order_table` (
                            `id`           BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `trade_id`     BIGINT(20) NOT NULL,
                            `order_amount` INT NOT NULL,
                            `created_at`   DATETIME(6) NOT NULL,
                            FOREIGN KEY (`trade_id`) REFERENCES trade(`id`)
);

CREATE TABLE `order_detail` (
                             `id`               BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `order_id`         BIGINT(20) NOT NULL,
                             `product_name`     VARCHAR(255) NOT NULL,
                             `product_category` ENUM ('MAIN_MENU', 'SUB_MENU', 'DRINK') NOT NULL,
                             `product_price`    INT NOT NULL,
                             `count`            INT NOT NULL,
                             `total_amount`     INT NOT NULL,
                             FOREIGN KEY (`order_id`) REFERENCES order_table(`id`)
);

CREATE TABLE `payment` (
                        `id`             BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `trade_id`       BIGINT(20) NOT NULL,
                        `payment_method` ENUM ('CASH', 'CARD') NOT NULL,
                        `card_company`   ENUM ('H', 'K', 'S') NULL,
                        `payment_at`     DATETIME(6) NOT NULL,
                        FOREIGN KEY (`trade_id`) REFERENCES trade(`id`)
);