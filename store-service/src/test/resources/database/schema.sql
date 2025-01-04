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
