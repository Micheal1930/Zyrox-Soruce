CREATE TABLE IF NOT EXISTS `saves_wallet_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `wallet_code` varchar(30) NOT NULL,
  `amount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `wallet_code` (`wallet_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;