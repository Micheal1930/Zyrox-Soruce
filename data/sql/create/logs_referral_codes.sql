CREATE TABLE IF NOT EXISTS `logs_referral_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `referral` text NOT NULL,
  `ip_address` text NOT NULL,
  `serial` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;