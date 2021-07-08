CREATE TABLE IF NOT EXISTS `logs_social_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `code` text NOT NULL,
  `ip_address` text NOT NULL,
  `serial` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;