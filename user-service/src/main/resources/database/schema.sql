DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_detail`;
DROP TABLE IF EXISTS `order_table`;
DROP TABLE IF EXISTS `trade`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `trade_device`;
DROP TABLE IF EXISTS `store`;
DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
                           `id`    BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `email` VARCHAR(255) NOT NULL,
                           `name`  VARCHAR(10) NOT NULL,
                           `phone` VARCHAR(11) NOT NULL
);