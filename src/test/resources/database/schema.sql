DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;
DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
                              `id`    BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              `email` VARCHAR(255) NOT NULL,
                              `name`  VARCHAR(10) NOT NULL,
                              `phone` VARCHAR(11) NOT NULL
);

CREATE TABLE `store` (
                         `id`            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `owner_user_id` BIGINT NOT NULL,
                         `name`          VARCHAR(255) NOT NULL,
                         `pos_grade` VARCHAR(6) NOT NULL,
                         CHECK (`pos_grade` IN ('BRONZE', 'SILVER', 'GOLD')),
                         FOREIGN KEY (`owner_user_id`) REFERENCES user_table(`id`),
                         UNIQUE (`owner_user_id`)
);

CREATE TABLE `trade_device` (
                                `id`       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `store_id` BIGINT NOT NULL,
                                FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);