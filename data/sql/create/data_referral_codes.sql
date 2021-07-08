CREATE TABLE IF NOT EXISTS `data_referral_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `referral_code` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;