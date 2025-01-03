DROP TABLE IF EXISTS `product_sale_statistic_item`;
DROP TABLE IF EXISTS `product_sale_statistic`;

CREATE TABLE `product_sale_statistic` (
                         `id`               BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `store_id`         BIGINT(20) NOT NULL,
                         `date_term`        ENUM ('DAY', 'WEEK', 'MONTH') NOT NULL,
                         `start_date`       DATE,
                         `product_category` ENUM ('MAIN_MENU', 'SUB_MENU', 'DRINK') NOT NULL,
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