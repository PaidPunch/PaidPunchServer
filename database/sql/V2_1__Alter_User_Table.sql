ALTER TABLE `app_user` ADD `user_code` varchar(5) DEFAULT NULL;
ALTER TABLE `app_user` ADD `refer_code` varchar(5) DEFAULT NULL;
ALTER TABLE `app_user` ADD `credit` decimal(10,2) NOT NULL DEFAULT '0.00'; 

