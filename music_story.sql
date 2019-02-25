/*
SQLyog Ultimate
MySQL - 8.0.12 : Database - music_story
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`music_story` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

/*Table structure for table `carousel` */

DROP TABLE IF EXISTS `carousel`;

CREATE TABLE `carousel` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `forward_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_tip` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `category_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsqg2wqpwewps1n7fwst8deus` (`category_id`),
  CONSTRAINT `FKsqg2wqpwewps1n7fwst8deus` FOREIGN KEY (`category_id`) REFERENCES `carousel_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `carousel` */

insert  into `carousel`(`id`,`forward_url`,`image_tip`,`image_url`,`sort`,`category_id`) values
('1',NULL,'图1','http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg',1,'1');

/*Table structure for table `carousel_category` */

DROP TABLE IF EXISTS `carousel_category`;

CREATE TABLE `carousel_category` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `created_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `carousel_category` */

insert  into `carousel_category`(`id`,`created_time`,`updated_time`,`created_by`,`updated_by`,`remark`,`title`) values
('1','2019-01-19 11:50:40','2019-02-19 17:53:56',NULL,'admin','测试用的。','测试');

/*Table structure for table `classify` */

DROP TABLE IF EXISTS `classify`;

CREATE TABLE `classify` (
  `id` bigint(20) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `created_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `introduction` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `classify` */

insert  into `classify`(`id`,`created_time`,`updated_time`,`created_by`,`updated_by`,`introduction`,`name`) values
(29,'2018-12-29 14:52:53','2018-12-29 14:56:16',NULL,'admin','感情记录方面','情感实录'),
(30,'2018-12-29 15:04:52','2018-12-29 15:38:07',NULL,'admin','青春少女小心思','少女情感'),
(31,'2018-12-29 15:38:30','2018-12-29 15:38:30','admin','admin','年少轻狂','少年情怀'),
(32,'2018-12-29 15:39:06','2018-12-29 15:39:06','admin','admin','小时光','童年趣味'),
(33,'2018-12-29 15:39:51','2018-12-29 15:39:51','admin','admin','升职加薪','工作感想'),
(34,'2018-12-29 15:40:12','2018-12-29 15:40:12','admin','admin','平淡生活','生活琐碎'),
(35,'2018-12-29 15:40:50','2018-12-29 15:40:50','admin','admin','青春期','青春校园'),
(36,'2018-12-29 15:41:59','2018-12-29 15:41:59','admin','admin','深夜是孤独的影子','夜语日记'),
(37,'2018-12-29 15:44:08','2018-12-29 15:44:08','admin','admin','灵光一瞬','随感随悟'),
(38,'2018-12-29 15:46:30','2018-12-29 15:46:30','admin','admin','隔壁有光，红袖添香','诗词沉香'),
(39,'2018-12-29 15:47:54','2018-12-29 15:47:54','admin','admin','人在旅途，遍地芬芳','旅途之光');

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `essay_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reply_comment_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reply_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `comment` */

/*Table structure for table `daily` */

DROP TABLE IF EXISTS `daily`;

CREATE TABLE `daily` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `daily` */

/*Table structure for table `essay` */

DROP TABLE IF EXISTS `essay`;

CREATE TABLE `essay` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `read_num` int(11) DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `classify_id` bigint(20) NOT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK62o1l1mfi16f17ig1nsiog3hg` (`classify_id`),
  KEY `FK8ynbkpv25t16oqkiosgr8k60o` (`user_id`),
  CONSTRAINT `FK62o1l1mfi16f17ig1nsiog3hg` FOREIGN KEY (`classify_id`) REFERENCES `classify` (`id`),
  CONSTRAINT `FK8ynbkpv25t16oqkiosgr8k60o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `essay` */

insert  into `essay`(`id`,`created_time`,`updated_time`,`content`,`read_num`,`remark`,`state`,`title`,`classify_id`,`user_id`) values
('as','2019-02-22 17:47:04','2019-02-25 16:44:15','一片优秀的文章字数的长度是必须的',0,'文章内容涉及敏感词汇',2,'论优秀的文章',29,'40289f946508da04016508db5d1d0000');

/*Table structure for table `hibernate_sequence` */

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values
(1),
(1),
(1),
(1),
(52),
(52),
(52),
(52);

/*Table structure for table `login_log` */

DROP TABLE IF EXISTS `login_log`;

CREATE TABLE `login_log` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_ip` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_ip` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `login_log` */

insert  into `login_log`(`id`,`address_ip`,`login_ip`,`login_time`,`user_id`,`user_name`,`user_type`) values
('402880a167f810850167f811edc20000',NULL,'0:0:0:0:0:0:0:1','2018-12-29 11:46:24','1','admin',1),
('402880a167f896710167f8970b300000',NULL,'0:0:0:0:0:0:0:1','2018-12-29 14:11:48','1','admin',1),
('402880a167f896710167f8bb0ffa0001',NULL,'0:0:0:0:0:0:0:1','2018-12-29 14:51:08','1','admin',1),
('402880a167f896710167f8c3b6620002',NULL,'0:0:0:0:0:0:0:1','2018-12-29 15:00:35','1','admin',1),
('402880a167f896710167f8e5cc960003',NULL,'0:0:0:0:0:0:0:1','2018-12-29 15:37:49','1','admin',1),
('402880a167f896710167f9240d160004',NULL,'0:0:0:0:0:0:0:1','2018-12-29 16:45:49','1','admin',1),
('402880a1681258860168125d3c3a0000',NULL,'0:0:0:0:0:0:0:1','2019-01-03 14:18:47','1','admin',1),
('402880a16826048d0168260530fb0000',NULL,'0:0:0:0:0:0:0:1','2019-01-07 09:55:01','1','admin',1),
('402880a16826145801682614992a0000',NULL,'0:0:0:0:0:0:0:1','2019-01-07 10:11:51','1','admin',1),
('402880a168262044016826227cff0000',NULL,'0:0:0:0:0:0:0:1','2019-01-07 10:27:01','1','admin',1),
('402880a1682659600168265c11d20000',NULL,'0:0:0:0:0:0:0:1','2019-01-07 11:29:55','1','admin',1),
('402880a16826596001682672a6230001',NULL,'0:0:0:0:0:0:0:1','2019-01-07 11:54:34','1','admin',1),
('402880a1682cba4101682cbba7700000',NULL,'0:0:0:0:0:0:0:1','2019-01-08 17:12:02','1','admin',1),
('402880a1682ce23001682ce299650000',NULL,'0:0:0:0:0:0:0:1','2019-01-08 17:54:34','1','admin',1),
('402880a16830c157016830c2ec390000',NULL,'0:0:0:0:0:0:0:1','2019-01-09 11:58:27','1','admin',1),
('402880a16864313101686432dee30000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 11:41:22','1','admin',1),
('402880a1686434ed0168643661f70000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 11:45:12','1','admin',1),
('402880a168643797016864398c2d0000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 11:48:40','1','admin',1),
('402880a16864b962016864ba5b2f0000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 14:09:21','1','admin',1),
('402880a16864d181016864ef38e80000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 15:07:06','1','admin',1),
('402880a1686553360168655405760000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 16:57:12','1','admin',1),
('402880a1686576b201686578c7690000',NULL,'0:0:0:0:0:0:0:1','2019-01-19 17:37:21','1','admin',1),
('402880a1686f18c801686f1bdb760000',NULL,'0:0:0:0:0:0:0:1','2019-01-21 14:32:03','1','admin',1),
('402880a1687d9d6001687da297380000',NULL,'0:0:0:0:0:0:0:1','2019-01-24 10:13:54','1','admin',1),
('402880a168f553b90168f55436ee0000',NULL,'0:0:0:0:0:0:0:1','2019-02-16 16:02:44','1','admin',1),
('402880a1690504b4016905144fcd0000',NULL,'0:0:0:0:0:0:0:1','2019-02-19 17:26:51','1','admin',1),
('402880a1690534e80169053547aa0000',NULL,'0:0:0:0:0:0:0:1','2019-02-19 18:02:52','1','admin',1),
('402880a1690a120601690a1329dc0000',NULL,'0:0:0:0:0:0:0:1','2019-02-20 16:43:42','1','admin',1),
('402880a1690e08ea01690e0959ec0000',NULL,'0:0:0:0:0:0:0:1','2019-02-21 11:11:28','1','admin',1),
('402880a1690e131e01690e18d1f70000',NULL,'0:0:0:0:0:0:0:1','2019-02-21 11:28:21','1','admin',1),
('402880a1690e1da301690e1e0fa00000',NULL,'0:0:0:0:0:0:0:1','2019-02-21 11:34:05','1','admin',1),
('402880a1690eac6801690eb0fdbf0000',NULL,'0:0:0:0:0:0:0:1','2019-02-21 14:14:34','1','admin',1),
('402880a1690f787501690f7a39ae0000',NULL,'0:0:0:0:0:0:0:1','2019-02-21 17:54:22','1','admin',1),
('402880a16912e941016912ebf8310000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 09:57:28','1','admin',1),
('402880a1691306d701691307c4870000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 10:27:50','1','admin',1),
('402880a1691308b1016913095a040000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 10:29:34','1','admin',1),
('402880a1691321d2016913225d650000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 10:56:53','1','admin',1),
('402880a16913d037016913d099a20000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 14:07:12','1','admin',1),
('402880a16914646d01691466c9980000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 16:51:14','1','admin',1),
('402880a1691498d9016914999ada0000',NULL,'0:0:0:0:0:0:0:1','2019-02-22 17:46:45','1','admin',1),
('402880a169226bb80169226eb8bf0000',NULL,'0:0:0:0:0:0:0:1','2019-02-25 10:14:35','1','admin',1),
('402880a169234770016923484e6c0000',NULL,'0:0:0:0:0:0:0:1','2019-02-25 14:12:15','1','admin',1),
('402880a1692385c80169238ba74f0000',NULL,'0:0:0:0:0:0:0:1','2019-02-25 15:25:49','1','admin',1);

/*Table structure for table `manager` */

DROP TABLE IF EXISTS `manager`;

CREATE TABLE `manager` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `real_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o0yekye62maw0eia889dcxyk7` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `manager` */

insert  into `manager`(`id`,`created_time`,`updated_time`,`avatar`,`birthday`,`gender`,`password`,`real_name`,`state`,`username`) values
('1','2018-12-26 11:56:00','2018-12-26 11:56:05','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRi9ZPVVFjY-IIlKeZ5TufzsnIsoExzREVOfv2tmlg272lYLn49xg','1992-09-24 00:00:00',1,'$2a$04$a90t7tmpIjJl6Ic9PZTHgeJDqN4iRwk45s8AVmN10v9cZ3jYl0qk6','陈林',1,'admin'),
('2','2018-12-26 11:56:48','2019-01-10 10:59:43','http://p6wg9ob78.bkt.clouddn.com/Fstwz1bEPFOeVM8n0BfsLK2THome','2018-04-11 00:00:00',1,'$2a$04$9KSovkBDzr7HvBjH7Monm.nor.x/mjgo9c9/eG8IeZ2Uh8gMhykoC','aa',1,'vip'),
('2c9fa82862ae50a80162b2928821007e','2018-12-26 11:56:55','2018-12-26 11:57:07','http://p6wg9ob78.bkt.clouddn.com/FleBXBCLSIbyWiLlQtcgds-MNKT1','1999-04-07 00:00:00',2,'$2a$04$XivsC2zMsxylvLgtvl2ASuQI9MBrMZDol6JbPNN6V/lHlhg7MV/Z.','呵呵',1,'hzhl'),
('2c9fa82862be6d1b0162cca8b970014b','2018-12-26 11:57:02','2018-12-26 11:57:26','','2018-04-16 00:00:00',1,'$2a$04$xFEX8RjmGK8ga73sKFk8MO3lhHnP8MfOUtTgSse4ElZnHBei4WEFq','陆22',0,'mengmeng'),
('2c9fa82862cccae60162fd54a9b20726','2018-12-26 11:57:15','2018-12-26 11:57:41','111','2018-04-02 00:00:00',1,'$2a$04$eK80LZIQ.czTtefR6MbLpuCGnkLuUQSEa3GeZBr06e.L8m57zyD3q','2',0,'21'),
('2c9fa82862cccae60162ffc6ea6f0754','2018-12-26 11:57:19','2018-12-26 11:57:37','http://p6wg9ob78.bkt.clouddn.com/FhAfdvYslNifQASMXzW63_8oGUpK','1997-04-26 00:00:00',2,'$2a$04$5D.TXpZknslQjiDat.GDSO/mix1/cY9t8X9ZlmF30t.9KApPXZo1O','zsh',0,'zsh'),
('2c9fa82862cccae60162ffdc148e0762','2018-12-26 11:57:17','2018-12-26 11:57:45','','2018-04-26 00:00:00',1,'$2a$04$1QzOInDPpcfWkzxKzTXQTOhmlU29JNCGrU8sJh5A0ckypkQSLX5Oi','yf',2,'yfh'),
('2c9fa8286305138e0163053c895f0010','2018-12-26 11:57:24','2018-12-26 11:57:48','http://p6wg9ob78.bkt.clouddn.com/FslKv7LmMFejPDbkpdprMazGwtAc','2014-04-28 00:00:00',1,'$2a$04$emANT5ZaRLTvskaPyxfOEOmvT./g2l10dCKqXRkkpiNoiLP5ubMSu','gf11112231',2,'ddff');

/*Table structure for table `manager_role` */

DROP TABLE IF EXISTS `manager_role`;

CREATE TABLE `manager_role` (
  `role_id` bigint(20) NOT NULL,
  `manager_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`manager_id`,`role_id`),
  KEY `FKmhxeqq4liaxhs7sc6g7082hp4` (`role_id`),
  CONSTRAINT `FKi98goitstamcfe3vpdl5xux4` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`id`),
  CONSTRAINT `FKmhxeqq4liaxhs7sc6g7082hp4` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `manager_role` */

insert  into `manager_role`(`role_id`,`manager_id`) values
(1,'1'),
(4,'2'),
(4,'2c9fa82862ae50a80162b2928821007e'),
(4,'2c9fa82862be6d1b0162cca8b970014b'),
(4,'2c9fa8286305138e0163053c895f0010');

/*Table structure for table `music` */

DROP TABLE IF EXISTS `music`;

CREATE TABLE `music` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `album` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `artist` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cover` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `essay_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `link_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lrc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `music` */

/*Table structure for table `news` */

DROP TABLE IF EXISTS `news`;

CREATE TABLE `news` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  `sender_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sent` bit(1) DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `news` */

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `id` bigint(20) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `created_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `notice_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `notice` */

insert  into `notice`(`id`,`created_time`,`updated_time`,`created_by`,`updated_by`,`begin_time`,`content`,`end_time`,`notice_type`,`title`) values
(51,'2019-01-08 17:54:55','2019-02-25 15:03:49',NULL,'admin','2019-01-08 00:00:00','我是来测试的，哈哈哈，牛牛水水水水，常常的干啥子嫩   我是来凑字数的哈哈哈','2019-01-17 00:00:00',NULL,'测试一条');

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL,
  `icon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `parent_ids` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `purview` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `resource_type` enum('menu','button') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `permission` */

insert  into `permission`(`id`,`icon`,`name`,`parent_id`,`parent_ids`,`purview`,`resource_type`,`sort`,`url`) values
(1,'&#xe614;','系统设置',0,'0','sys:view','menu',0,''),
(2,'&#xe612;','管理员列表',1,'0/1','sys:admin','menu',1,'/sys/adminList'),
(3,'&#xe663;','菜单列表',1,'0/1','menu:view','menu',3,'/sys/menuList'),
(4,'&#xe617;','数据监控',1,'0/1','sys:data','menu',5,'/druid/index.html'),
(5,'&#xe629;','日志列表',1,'0/1','sys:log','menu',4,'/sys/loginLogs'),
(6,'&#xe613;','角色列表',1,'0/1','role:view','menu',2,'/sys/roleList'),
(7,'&#xe653;','音书管理',0,'0','mus:view','menu',1,''),
(8,'&#xe60e;','公告列表',7,'0/7','not:view','menu',3,'/ms/notice'),
(9,'&#xe66f;','用户列表',7,'0/7','user:view','menu',0,'/ms/user'),
(10,'','新增',2,'0/1/2','admin:add','button',1,''),
(11,'','删除',2,'0/1/2','admin:del','button',3,''),
(12,'','新增',3,'0/1/3','menu:add','button',1,''),
(13,'','删除',3,'0/1/3','menu:del','button',3,''),
(14,'','修改',6,'0/1/6','role:alt','button',2,''),
(15,'','删除',6,'0/1/6','role:del','button',3,''),
(16,'','重置密码',2,'0/1/2','admin:reset','button',4,''),
(17,'&#xe705;','文章列表',7,'0/7','story:view','menu',1,'/ms/essay'),
(18,'','修改',2,'0/1/2','admin:alt','button',2,''),
(19,'','授权',6,'0/1/6','role:grant','button',4,''),
(20,'','修改',3,'0/1/3','menu:alt','button',2,''),
(21,'','新增',6,'0/1/6','role:add','button',1,''),
(22,'&#xe634;','轮播图',7,'0/7','sel:view','menu',4,'/ms/carousel'),
(23,'&#xe60a;','分类列表',7,'0/7','clazz:view','menu',2,'/ms/classify'),
(24,'','删除',9,'0/7/9','user:del','button',1,''),
(25,'','修改',9,'0/7/9','user:alt','button',0,''),
(26,'','新增',23,'0/7/23','clazz:add','button',0,''),
(27,'','修改',23,'0/7/23','clazz:alt','button',1,''),
(28,'','删除',23,'0/7/23','clazz:del','button',2,''),
(44,'','新增',8,'0/7/8','not:add','button',0,''),
(45,'','修改',8,'0/7/8','not:alt','button',1,''),
(46,'','删除',8,'0/7/8','not:del','button',2,''),
(47,'','新增',22,'0/7/22','sel:add','button',0,''),
(48,'','修改',22,'0/7/22','sel:alt','button',1,''),
(49,'','删除',22,'0/7/22','sel:del','button',2,''),
(50,'','审核',17,'0/7/17','story:alt','button',0,'');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `available` bit(1) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKor7ok73vt9bsmxyc2t6s37qi5` (`role_code`,`role_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `role` */

insert  into `role`(`id`,`available`,`description`,`role_code`,`role_name`,`role_type`) values
(1,'','最牛逼的人,拥有毁天灭地的能力，一不小心就把自己玩死了','admin','超管',1),
(2,'\0','没多大权力的人','xg','小管',1),
(4,'','这个是你们VIP用户的角色，请勿动它','manger','管理员',1),
(6,'','前台管理员角色','vip','前台管理员',2);

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  KEY `FKf8yllw1ecvwqy3ehyxawqa1qp` (`permission_id`),
  KEY `FKa6jx8n8xkesmjmv6jqug6bg68` (`role_id`),
  CONSTRAINT `FKa6jx8n8xkesmjmv6jqug6bg68` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKf8yllw1ecvwqy3ehyxawqa1qp` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `role_permission` */

insert  into `role_permission`(`role_id`,`permission_id`) values
(1,7),
(1,23),
(1,28),
(1,26),
(1,27),
(1,8),
(1,44),
(1,46),
(1,45),
(1,9),
(1,24),
(1,25),
(1,22),
(1,48),
(1,47),
(1,49),
(1,17),
(1,50),
(1,1),
(1,2),
(1,16),
(1,10),
(1,18),
(1,11),
(1,3),
(1,13),
(1,12),
(1,20),
(1,5),
(1,4),
(1,6),
(1,14),
(1,21),
(1,15),
(1,19),
(4,7),
(4,23),
(4,26),
(4,27),
(4,8),
(4,44),
(4,45),
(4,9),
(4,22),
(4,48),
(4,47),
(4,17),
(4,1),
(4,2),
(4,10),
(4,18),
(4,3),
(4,12),
(4,20),
(4,5),
(4,4),
(4,6),
(4,14),
(4,21);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `person_desc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `real_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signature` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` enum('0','1','2') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `union_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKjyw4ddm9goh1x9bjqc9900vnj` (`union_id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user` */

insert  into `user`(`id`,`created_time`,`updated_time`,`address`,`avatar`,`balance`,`birthday`,`credit`,`email`,`gender`,`level`,`nick_name`,`password`,`person_desc`,`phone`,`real_name`,`signature`,`state`,`union_id`,`username`) values
('40289f946508da04016508db5d1d0000','2018-12-26 14:22:38','2018-12-28 17:33:06','湖北省武汉市','https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1114585158,1026985006&fm=27&gp=0.jpg',NULL,'2018-12-27 14:32:51',NULL,'53123775@qq.com',1,NULL,'风神','$2a$04$eK8ADtpXzGUtbHNzl5x.PebvQmhyCY7KLp29VSH5OVwhjZHvtjfRS','我从那里来，要到哪里去','1507886554','王八蛋的','无个性不签名','0',NULL,'hehe');

/*Table structure for table `user_faves` */

DROP TABLE IF EXISTS `user_faves`;

CREATE TABLE `user_faves` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `essay_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fave_type` tinyint(4) DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user_faves` */

/*Table structure for table `user_follow` */

DROP TABLE IF EXISTS `user_follow`;

CREATE TABLE `user_follow` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_time` datetime NOT NULL,
  `follow_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user_follow` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user_role` */

insert  into `user_role`(`user_id`,`role_id`) values
('40289f946508da04016508db5d1d0000',6);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
