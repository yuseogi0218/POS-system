DROP TABLE IF EXISTS `settlement`;
DROP TABLE IF EXISTS `product_sale_statistic`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_detail`;
DROP TABLE IF EXISTS `order_table`;
DROP TABLE IF EXISTS `trade`;
DROP TABLE IF EXISTS `product_history`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;


CREATE TABLE `store` (
                         `id`            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `owner_user_id` BIGINT NOT NULL,
                         `name`          VARCHAR(255) NOT NULL,
                         `pos_grade`     VARCHAR(6) NOT NULL,`settlement_date` INT NOT NULL,
                         CHECK ( `pos_grade` IN ('BRONZE', 'SILVER', 'GOLD') ),
                         CHECK ( `settlement_date` IN (1, 5, 10, 15, 20, 25)),
                         UNIQUE (`owner_user_id`)
);

CREATE TABLE `trade_device` (
                                `id`       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `store_id` BIGINT NOT NULL,
                                FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);

CREATE TABLE `product` (
                           `id`         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `store_id`   BIGINT NOT NULL,
                           `name`       VARCHAR(255) NOT NULL,
                           `category`   VARCHAR(10) NOT NULL,
                           `price`      INT NOT NULL,
                           `stock`      INT NOT NULL,
                           `base_stock` INT NOT NULL,
                           `created_at` DATETIME(6) NOT NULL,
                           `updated_at` DATETIME(6) NOT NULL,
                           `is_deleted` VARCHAR(1) NOT NULL,
                           CHECK (`category` IN ('MAIN_MENU', 'SUB_MENU', 'DRINK')),
                           FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);

CREATE TABLE `product_history` (
                                `id`         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `product_id` BIGINT NOT NULL,
                                `price`      INT NOT NULL,
                                `base_stock` INT NOT NULL,
                                `created_at` DATETIME(6) NOT NULL,
                                FOREIGN KEY (`product_id`) REFERENCES product(`id`)
);

CREATE TABLE `trade` (
                         `id`              BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `store_id`        BIGINT NOT NULL,
                         `trade_device_id` BIGINT NOT NULL,
                         `trade_amount`    INT NOT NULL,
                         `created_at`      DATETIME(6) NOT NULL,
                         `is_completed`    VARCHAR(1) NOT NULL,
                         CHECK ( `is_completed` IN ('Y', 'N') )
);

CREATE TABLE `order_table` (
                               `id`           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               `trade_id`     BIGINT NOT NULL,
                               `order_amount` INT NOT NULL,
                               `created_at`   DATETIME(6) NOT NULL,
                               FOREIGN KEY (`trade_id`) REFERENCES trade(`id`)
);

CREATE TABLE `order_detail` (
                                `id`           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `order_id`     BIGINT NOT NULL,
                                `product_id`   BIGINT NOT NULL,
                                `count`        INT NOT NULL,
                                `total_amount` INT NOT NULL,
                                `created_at`   DATETIME(6) NOT NULL,
                                FOREIGN KEY (`order_id`) REFERENCES order_table(`id`)
);

CREATE TABLE `payment` (
                        `id`             BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `trade_id`       BIGINT NOT NULL,
                        `payment_method` VARCHAR(4) NOT NULL,
                        `card_company`   VARCHAR(1) NULL,
                        `card_fee`       INT NOT NULL,
                        `created_at`     DATETIME(6) NOT NULL,
                        CHECK ( `payment_method` IN ('CASH', 'CARD')),
                        CHECK ( `card_company` IN ('H', 'K', 'S')),
                        FOREIGN KEY (`trade_id`) REFERENCES trade(`id`)
);

CREATE TABLE `product_sale_statistic` (
                                       `id`          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       `product_id`  BIGINT NOT NULL,
                                       `date_term`   VARCHAR(5) NOT NULL,
                                       `start_date`  DATE,
                                       `sale_count`  INT NOT NULL,
                                       `sale_amount` INT NOT NULL,
                                       CHECK ( `date_term` IN ('DAY', 'WEEK', 'MONTH') ),
                                       FOREIGN KEY (`product_id`) REFERENCES product(`id`)
);

CREATE TABLE `settlement` (
                           `id`         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `store_id`   BIGINT NOT NULL,
                           `date_term`  VARCHAR(5) NOT NULL,
                           `start_date` DATE,
                           `revenue`    INT NOT NULL,
                           `fee`        INT NOT NULL,
                           `operating_profit` INT NOT NULL,
                           CHECK ( `date_term` IN ('DAY', 'WEEK', 'MONTH') ),
                           FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);