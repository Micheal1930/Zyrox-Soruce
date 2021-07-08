CREATE TABLE IF NOT EXISTS `logs_auction_bids` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `item_owner` text NOT NULL,
  `item_name` text NOT NULL,
  `item_id` int(11) NOT NULL,
  `total_bids` int(11) NOT NULL,
  `auction_price` text NOT NULL,
  `time_remaining` text NOT NULL,
  `time_bidded` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;