CREATE TABLE IF NOT EXISTS `pptemplates` (
  `pptemplate_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `group_id` int(10) unsigned,
  `group_name` varchar(45) NOT NULL,
  `template` varchar(512) NOT NULL,
  `uses_file` bool NOT NULL DEFAULT 0,
  `disabled` bool NOT NULL DEFAULT 0,
  `date_modified` DATETIME DEFAULT NULL,
  PRIMARY KEY (`pptemplate_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
