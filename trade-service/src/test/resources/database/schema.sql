DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_detail`;
DROP TABLE IF EXISTS `order_table`;
DROP TABLE IF EXISTS `trade`;
DROP TABLE IF EXISTS `product`;

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
                           CHECK (`category` IN ('MAIN_MENU', 'SUB_MENU', 'DRINK'))
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