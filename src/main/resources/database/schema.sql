DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;
DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
                           `id`    BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `email` VARCHAR(255) NOT NULL,
                           `name`  VARCHAR(10) NOT NULL,
                           `phone` VARCHAR(11) NOT NULL
);

CREATE TABLE `store` (
                      `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      `owner_user_id` BIGINT(20) NOT NULL,
                      `name` VARCHAR(255) NOT NULL,
                      `pos_grade` ENUM ('BRONZE', 'SILVER', 'GOLD') NOT NULL,
                      FOREIGN KEY (`owner_user_id`) REFERENCES user_table(`id`),
                      UNIQUE (`owner_user_id`)
);

CREATE TABLE `trade_device` (
                             `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `store_id` BIGINT(20) NOT NULL,
                             FOREIGN KEY (`store_id`) REFERENCES store(`id`)
);