CREATE TABLE IF NOT EXISTS `logs_punishments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `punisher` text NOT NULL,
  `target` text NOT NULL,
  `type` text NOT NULL,
  `duration` text NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;