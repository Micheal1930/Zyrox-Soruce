CREATE TABLE IF NOT EXISTS `logs_auction_purchases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `item_owner` text NOT NULL,
  `item_name` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `amount_purchased` text NOT NULL,
  `amount_left` text NOT NULL,
  `purchase_price` text NOT NULL,
  `time_remaining` text NOT NULL,
  `time_purchased` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;