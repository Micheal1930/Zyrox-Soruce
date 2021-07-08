CREATE TABLE IF NOT EXISTS `logs_global_events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event` text NOT NULL,
  `description` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;