CREATE TABLE IF NOT EXISTS `logs_introduction_claims` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `ip_address` text NOT NULL,
  `serial_address` text NOT NULL,
  `topic_id` int(11) NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;