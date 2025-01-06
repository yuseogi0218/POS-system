DROP TABLE IF EXISTS `settlement`;
DROP TABLE IF EXISTS `product_sale_statistic`;

CREATE TABLE `product_sale_statistic` (
                                       `id`          BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       `product_id`  BIGINT(20) NOT NULL,
                                       `date_term`   VARCHAR(5) NOT NULL,
                                       `start_date`  DATE,
                                       `sale_count`  INT NOT NULL,
                                       `sale_amount` INT NOT NULL,
                                       CHECK ( `date_term` IN ('DAY', 'WEEK', 'MONTH') ),
                                       FOREIGN KEY (`product_id`) REFERENCES product(`id`)
);

CREATE TABLE `settlement` (
                           `id`         BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `store_id`   BIGINT(20) NOT NULL,
                           `date_term`  VARCHAR(5) NOT NULL,
                           `start_date` DATE,
                           `revenue`    INT NOT NULL,
                           `fee`        INT NOT NULL,
                           `operating_profit` INT NOT NULL,
                           CHECK ( `date_term` IN ('DAY', 'WEEK', 'MONTH') ),
                           FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);
