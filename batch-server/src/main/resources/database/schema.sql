DROP TABLE IF EXISTS `fee_adjustment_item`;
DROP TABLE IF EXISTS `fee_adjustment`;
DROP TABLE IF EXISTS `revenue_adjustment_item`;
DROP TABLE IF EXISTS `revenue_adjustment`;
DROP TABLE IF EXISTS `adjustment`;
DROP TABLE IF EXISTS `product_sale_statistic_item`;
DROP TABLE IF EXISTS `product_sale_statistic`;

CREATE TABLE `product_sale_statistic` (
                                       `id`               BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       `store_id`         BIGINT(20) NOT NULL,
                                       `date_term`        VARCHAR(5) NOT NULL,
                                       `start_date`       DATE,
                                       `product_category` VARCHAR(10) NOT NULL,
                                       CHECK ( `date_term` IN ('DAY', 'WEEK', 'MONTH') ),
                                       CHECK ( `product_category` IN ('MAIN_MENU', 'SUB_MENU', 'DRINK') ),
                                       FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);

CREATE TABLE `product_sale_statistic_item` (
                                            `id`             BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                            `statistic_id`   BIGINT(20) NOT NULL,
                                            `count_ranking`  INT NOT NULL,
                                            `amount_ranking` INT NOT NULL,
                                            `product_name`   VARCHAR(255) NOT NULL,
                                            `product_price`  INT NOT NULL,
                                            `sale_count`     INT NOT NULL,
                                            `sale_amount`    INT NOT NULL,
                                            FOREIGN KEY (`statistic_id`) REFERENCES product_sale_statistic(`id`)
);

CREATE TABLE `adjustment` (
                           `id`               BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `store_id`         BIGINT(20) NOT NULL,
                           `date_term`        VARCHAR(5) NOT NULL,
                           `start_date`       DATE,
                           `total_revenue`    INT NOT NULL,
                           `total_fee`        INT NOT NULL,
                           `operating_profit` INT NOT NULL,
                           CHECK ( `date_term` IN ('DAY', 'WEEK', 'MONTH') ),
                           FOREIGN KEY (`store_id`) REFERENCES store(`id`)

);

CREATE TABLE `revenue_adjustment` (
                                   `id`                      BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   `adjustment_id`           BIGINT(20) NOT NULL,
                                   `product_category` VARCHAR(10) NOT NULL,
                                   `category_revenue_amount` INT NOT NULL,
                                   CHECK ( `product_category` IN ('MAIN_MENU', 'SUB_MENU', 'DRINK') ),
                                   FOREIGN KEY (`adjustment_id`) REFERENCES adjustment(`id`)
);

CREATE TABLE `revenue_adjustment_item` (
                                        `id`                    BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        `revenue_adjustment_id` BIGINT(20) NOT NULL,
                                        `product_name`          VARCHAR(255) NOT NULL,
                                        `product_price`         INT NOT NULL,
                                        `sale_count`            INT NOT NULL,
                                        `sale_amount`           INT NOT NULL,
                                        FOREIGN KEY (`revenue_adjustment_id`) REFERENCES revenue_adjustment(`id`)
);

CREATE TABLE `fee_adjustment` (
                               `id`                  BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               `adjustment_id`       BIGINT(20) NOT NULL,
                               `fee_category`        VARCHAR(10) NOT NULL,
                               `category_fee_amount` INT NOT NULL,
                               CHECK ( `fee_category` IN ('CARD', 'POS_USAGE') ),
                               FOREIGN KEY (`adjustment_id`) REFERENCES adjustment(`id`)
);

CREATE TABLE `fee_adjustment_item` (
                                    `id`                    BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    `fee_adjustment_id`     BIGINT(20) NOT NULL,
                                    `service_provider_name` VARCHAR(255) NOT NULL,
                                    `fee_amount`            INT NOT NULL,
                                    FOREIGN KEY (`fee_adjustment_id`) REFERENCES fee_adjustment(`id`)
);