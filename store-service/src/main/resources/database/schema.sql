DROP TABLE IF EXISTS `product_history`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;

CREATE TABLE `store` (
                      `id`            BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      `owner_user_id` BIGINT(20) NOT NULL,
                      `name`          VARCHAR(255) NOT NULL,
                      `pos_grade`     VARCHAR(6) NOT NULL,
                      `settlement_date` INT NOT NULL,
                      CHECK ( `pos_grade` IN ('BRONZE', 'SILVER', 'GOLD') ),
                      CHECK ( `settlement_date` IN (1, 5, 10, 15, 20, 25)),
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
                        `category`   VARCHAR(10) NOT NULL,
                        `price`      INT NOT NULL,
                        `stock`      INT NOT NULL,
                        `base_stock` INT NOT NULL,
                        `created_at` DATETIME(6) NOT NULL,
                        `updated_at` DATETIME(6) NOT NULL,
                        `is_deleted` VARCHAR(1) NOT NULL,
                        CHECK ( `category` IN ('MAIN_MENU', 'SUB_MENU', 'DRINK') ),
                        CHECK ( `is_deleted` IN ('Y', 'N') ),
                        FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);

CREATE TABLE `product_history` (
                                `id`         BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `product_id` BIGINT(20) NOT NULL,
                                `price`      INT NOT NULL,
                                `base_stock` INT NOT NULL,
                                `created_at` DATETIME(6) NOT NULL,
                                FOREIGN KEY (`product_id`) REFERENCES product(`id`)
);