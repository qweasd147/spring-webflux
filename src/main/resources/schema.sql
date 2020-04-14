create table IF NOT EXISTS `article` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) NOT NULL,
  `contents` LONGVARCHAR NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`idx`)
);