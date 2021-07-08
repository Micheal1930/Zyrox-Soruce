CREATE TABLE IF NOT EXISTS `logs_staked_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `holder` text NOT NULL,
  `receiver` text NOT NULL,
  `item_name` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `time` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;