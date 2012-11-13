/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `app_user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `email_id` varchar(45) NOT NULL,
  `mobile_no` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `pincode` varchar(15) DEFAULT NULL,
  `user_status` varchar(45) DEFAULT NULL,
  `isemailverified` varchar(1) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Time` time DEFAULT NULL,
  `sessionid` varchar(45) NOT NULL DEFAULT '',
  `isfbaccount` varchar(1) DEFAULT NULL,
  `fbid` varchar(45) DEFAULT NULL,
  `isprofile_created` varchar(6) DEFAULT 'false',
  `profile_id` varchar(45) DEFAULT NULL,
  `card_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`sessionid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `business_users` (
  `business_userid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `business_name` varchar(100) NOT NULL,
  `email_id` varchar(70) NOT NULL,
  `password` varchar(30) NOT NULL,
  `secretcode` varchar(20) DEFAULT NULL,
  `buss_desc` varchar(150) NOT NULL,
  `isemailverified` varchar(1) NOT NULL,
  `bussiness_logo` longblob NOT NULL,
  `role` varchar(10) NOT NULL,
  `contactno` varchar(20) NOT NULL,
  `contactname` varchar(30) NOT NULL,
  `logo_path` varchar(150) NOT NULL,
  `is_free_punch` varchar(10) NOT NULL,
  `modification_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `busi_enabled` varchar(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`business_userid`)
) ENGINE=InnoDB AUTO_INCREMENT=500 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `bussiness_address` (
  `address_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(10) unsigned NOT NULL,
  `address_line1` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `latitude` varchar(20) NOT NULL,
  `longitude` varchar(20) NOT NULL,
  PRIMARY KEY (`address_id`),
  KEY `FK_bussiness_address_1` (`business_id`),
  CONSTRAINT `FK_bussiness_address_1` FOREIGN KEY (`business_id`) REFERENCES `business_users` (`business_userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=500 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `city` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` char(35) NOT NULL DEFAULT '',
  `CountryCode` char(3) NOT NULL DEFAULT '',
  `District` char(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `country` (
  `country_id` int(11) NOT NULL AUTO_INCREMENT,
  `country_name` varchar(100) DEFAULT NULL,
  `country_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=500 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `feedback` (
  `feedback_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_user_id` int(10) unsigned NOT NULL,
  `feedback_data` varchar(500) NOT NULL,
  `feedback_date` date NOT NULL,
  `feedback_time` time NOT NULL,
  PRIMARY KEY (`feedback_id`),
  KEY `FK_feedback_1` (`app_user_id`),
  CONSTRAINT `FK_feedback_1` FOREIGN KEY (`app_user_id`) REFERENCES `app_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `marchant_code` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(10) unsigned NOT NULL,
  `code` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_marchant_code_1` (`business_id`),
  CONSTRAINT `FK_marchant_code_1` FOREIGN KEY (`business_id`) REFERENCES `business_users` (`business_userid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `payment_details` (
  `payment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `punchcard_id` int(10) unsigned NOT NULL,
  `appuser_id` int(10) unsigned NOT NULL,
  `token` varchar(45) NOT NULL,
  `response` varchar(45) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `punch_card` (
  `punch_card_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `punch_card_name` varchar(70) NOT NULL,
  `no_of_punches_per_card` int(10) unsigned NOT NULL,
  `value_of_each_punch` float(6,2) NOT NULL,
  `selling_price_of_punch_card` float(6,2) NOT NULL,
  `expiry_date` date NOT NULL,
  `effective_discount` float NOT NULL,
  `business_userid` int(10) unsigned NOT NULL,
  `qrcode` varchar(45) NOT NULL,
  `disc_value_of_each_punch` float(6,2) NOT NULL,
  `restriction_time` int(10) unsigned NOT NULL,
  `is_mystery_punch` varchar(10) NOT NULL,
  `punchcard_category` varchar(45) NOT NULL,
  `expirydays` int(10) unsigned NOT NULL,
  `minimumvalue` int(10) unsigned NOT NULL,
  PRIMARY KEY (`punch_card_id`) USING BTREE,
  KEY `business_userid` (`business_userid`),
  CONSTRAINT `FK_punch_card_1` FOREIGN KEY (`business_userid`) REFERENCES `business_users` (`business_userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=500 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `mystery_punch` (
  `mystery_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `punch_card_id` int(10) unsigned NOT NULL,
  `value_of_myst_punch` varchar(100) NOT NULL,
  `probability` int(10) unsigned NOT NULL,
  `paidpunchmystery` varchar(10) NOT NULL,
  `mysterypunchvalid` varchar(10) NOT NULL,
  PRIMARY KEY (`mystery_id`),
  KEY `FK_mystery_punch_1` (`punch_card_id`),
  CONSTRAINT `FK_mystery_punch_1` FOREIGN KEY (`punch_card_id`) REFERENCES `punch_card` (`punch_card_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=750 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `punch_card_tracker` (
  `punch_card_tracker_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_user_id` int(10) unsigned NOT NULL,
  `punch_card_id` int(10) unsigned NOT NULL,
  `tracker_date` date NOT NULL,
  `tracker_time` time NOT NULL,
  `punch_card_downloadid` int(10) unsigned NOT NULL,
  `is_mystery_punch` varchar(10) NOT NULL,
  PRIMARY KEY (`punch_card_tracker_id`) USING BTREE,
  KEY `FK_punch_card_tracker_1` (`app_user_id`),
  KEY `FK_punch_card_tracker_2` (`punch_card_id`),
  CONSTRAINT `FK_punch_card_tracker_1` FOREIGN KEY (`punch_card_id`) REFERENCES `punch_card` (`punch_card_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_punch_card_tracker_2` FOREIGN KEY (`app_user_id`) REFERENCES `app_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `punchcard_download` (
  `punch_card_downloadid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_user_id` int(10) unsigned NOT NULL,
  `punch_card_id` int(10) unsigned NOT NULL,
  `punch_used` int(10) unsigned NOT NULL,
  `download_date` date NOT NULL,
  `download_time` time NOT NULL,
  `payment_id` int(10) unsigned DEFAULT NULL,
  `isfreepunch` varchar(10) NOT NULL,
  `mystery_punchid` int(10) unsigned DEFAULT NULL,
  `exipre_date` varchar(45) NOT NULL,
  PRIMARY KEY (`punch_card_downloadid`),
  KEY `app_user_id` (`app_user_id`),
  KEY `punch_card_id` (`punch_card_id`) USING BTREE,
  KEY `FK_punchcard_download_3` (`payment_id`),
  KEY `FK_punchcard_download_4` (`mystery_punchid`),
  CONSTRAINT `FK_punchcard_download_1` FOREIGN KEY (`punch_card_id`) REFERENCES `punch_card` (`punch_card_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_punchcard_download_2` FOREIGN KEY (`app_user_id`) REFERENCES `app_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_punchcard_download_3` FOREIGN KEY (`payment_id`) REFERENCES `payment_details` (`payment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_punchcard_download_4` FOREIGN KEY (`mystery_punchid`) REFERENCES `mystery_punch` (`mystery_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `secretcode` (
  `code_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code_value` varchar(45) NOT NULL,
  `code_used` varchar(5) NOT NULL,
  PRIMARY KEY (`code_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS `user_feeds` (
  `feeds_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `punchcard_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `activity` varchar(45) NOT NULL,
  `ismysterypunch` varchar(1) NOT NULL,
  `mystery_id` int(10) unsigned DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`feeds_id`),
  KEY `FK_user_feeds_2` (`user_id`),
  KEY `FK_user_feeds_3` (`mystery_id`),
  KEY `FK_user_feeds_1` (`punchcard_id`) USING BTREE,
  CONSTRAINT `FK_user_feeds_1` FOREIGN KEY (`punchcard_id`) REFERENCES `punch_card` (`punch_card_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_feeds_2` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_feeds_3` FOREIGN KEY (`mystery_id`) REFERENCES `mystery_punch` (`mystery_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


