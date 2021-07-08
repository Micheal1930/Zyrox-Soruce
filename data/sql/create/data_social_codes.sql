CREATE TABLE IF NOT EXISTS `data_social_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `social_code` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `uses` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;