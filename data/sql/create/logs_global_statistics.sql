CREATE TABLE IF NOT EXISTS `logs_global_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `players_online` text NOT NULL,
  `staff_online` text NOT NULL,
  `wilderness_count` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;