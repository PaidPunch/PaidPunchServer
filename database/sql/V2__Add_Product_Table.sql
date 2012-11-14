CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `credits` decimal(10,2) NOT NULL,
  `cost` decimal(10,2) NOT NULL,
  `disabled` bool NOT NULL DEFAULT 0,
  `date_modified` DATETIME DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
