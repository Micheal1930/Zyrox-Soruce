CREATE TABLE IF NOT EXISTS `logs_suspicious_logins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `current_ip` text NOT NULL,
  `last_ip` text NOT NULL,
  `current_serial` text NOT NULL,
  `last_serial` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;