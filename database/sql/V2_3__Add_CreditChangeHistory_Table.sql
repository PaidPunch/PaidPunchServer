CREATE TABLE IF NOT EXISTS `creditchangehistory` (
  `creditchangehistory_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `credit_change` decimal(10,2) NOT NULL,
  `reason` int(3) unsigned NOT NULL,
  `source_id` int(10) unsigned NOT NULL,
  `date_modified` DATETIME DEFAULT NULL,

  PRIMARY KEY (`creditchangehistory_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
