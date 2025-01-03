DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_detail`;
DROP TABLE IF EXISTS `order_table`;
DROP TABLE IF EXISTS `trade`;

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
                        `card_fee`       INT NOT NULL,
                        `payment_at`     DATETIME(6) NOT NULL,
                        FOREIGN KEY (`trade_id`) REFERENCES trade(`id`)
);