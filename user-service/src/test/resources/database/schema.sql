DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
                           `id`    BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `email` VARCHAR(255) NOT NULL,
                           `name`  VARCHAR(10) NOT NULL,
                           `phone` VARCHAR(11) NOT NULL
);