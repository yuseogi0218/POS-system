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
                             `product_id`       BIGINT(20) NOT NULL,
                             `count`            INT NOT NULL,
                             `total_amount`     INT NOT NULL,
                             `created_at`   DATETIME(6) NOT NULL,
                             FOREIGN KEY (`order_id`) REFERENCES order_table(`id`),
                             FOREIGN KEY (`product_id`) REFERENCES product(`id`)
);
-- Index 추가
CREATE INDEX idx_order_detail_created_at ON order_detail (created_at);

CREATE TABLE `payment` (
                        `id`             BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `trade_id`       BIGINT(20) NOT NULL,
                        `payment_method` VARCHAR(4) NOT NULL,
                        `card_company`   VARCHAR(1) NULL,
                        `card_fee`       INT NOT NULL,
                        `created_at`     DATETIME(6) NOT NULL,
                        CHECK ( `payment_method` IN ('CASH', 'CARD') ),
                        CHECK ( `card_company` IN ('H', 'K', 'S') ),
                        FOREIGN KEY (`trade_id`) REFERENCES trade(`id`)
);
-- Index 추가
CREATE INDEX idx_payment_created_at ON payment (created_at);