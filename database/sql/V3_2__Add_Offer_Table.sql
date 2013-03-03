CREATE TABLE IF NOT EXISTS `offers` (
  `offer_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(10) NOT NULL,
  `discount` decimal(10,2) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `offertype` int(3) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `condition1` varchar(150) DEFAULT NULL,
  `condition2` varchar(150) DEFAULT NULL,
  `condition3` varchar(150) DEFAULT NULL,
  `disabled` bool NOT NULL DEFAULT 0,
  `expiry_date` DATETIME DEFAULT NULL,
  `date_modified` DATETIME DEFAULT NULL,
  PRIMARY KEY (`offer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
