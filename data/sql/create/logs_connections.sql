CREATE TABLE IF NOT EXISTS `logs_connections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `action` text NOT NULL,
  `ip_address` text NOT NULL,
  `serial_address` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;