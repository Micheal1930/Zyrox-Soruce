CREATE TABLE IF NOT EXISTS `data_youtube_videos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_id` text NOT NULL,
  `uploader` text NOT NULL,
  `title` text NOT NULL,
  `description` text NOT NULL,
  `unix_time` bigint(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;