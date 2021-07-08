CREATE TABLE IF NOT EXISTS `statistics_online` (
  `id` int(11) NOT NULL DEFAULT '1',
  `online` int(11) NOT NULL,
  `wilderness` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;