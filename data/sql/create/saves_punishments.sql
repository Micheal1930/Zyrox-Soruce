CREATE TABLE IF NOT EXISTS `saves_punishments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `punishment_type` text NOT NULL,
  `punishment_data` varchar(512) NOT NULL,
  `reason` varchar(512) NOT NULL DEFAULT '',
  `timer` text NOT NULL,
  `duration` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `punishment_data` (`punishment_data`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;