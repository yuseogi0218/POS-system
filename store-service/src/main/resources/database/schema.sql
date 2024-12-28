DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;

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