/*
SQLyog Ultimate v12.09 (64 bit)
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

USE `music_story`;

/*Table structure for table `carousel` */

DROP TABLE IF EXISTS `carousel`;

CREATE TABLE `carousel` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `image_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_tip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `manager_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `carousel` */

/*Table structure for table `classify` */

DROP TABLE IF EXISTS `classify`;

CREATE TABLE `classify` (
  `id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `classify` */

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `essay_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reply_comment_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reply_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `comment` */

/*Table structure for table `essay` */

DROP TABLE IF EXISTS `essay`;

CREATE TABLE `essay` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `classify_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `read_num` int(11) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `essay` */

/*Table structure for table `hibernate_sequence` */

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values (1),(1),(1),(1);

/*Table structure for table `login_log` */

DROP TABLE IF EXISTS `login_log`;

CREATE TABLE `login_log` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `login_log` */

insert  into `login_log`(`id`,`address_ip`,`login_ip`,`login_time`,`user_id`,`user_name`,`user_type`) values ('402880b86339a709016339a7b2120000','','0:0:0:0:0:0:0:1','2018-05-07 16:11:21','1','admin',1),('402880d26420faf3016420fb901e0000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 14:15:15','1','admin',1),('402880d26420faf3016420fd3ac60001',NULL,'0:0:0:0:0:0:0:1','2018-06-21 14:17:04','2','vip',1),('402880d2642134f70164213557f50000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 15:18:21','2','vip',1),('402880d2642137c7016421382dc60000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 15:21:27','2','vip',1),('402880d264213adb0164213dace80000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 15:27:27','2','vip',1),('402880d264214626016421467b020000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 15:37:05','2','vip',1),('402880d264214626016421472a740001',NULL,'0:0:0:0:0:0:0:1','2018-06-21 15:37:49','2','vip',1),('402880d264214ce90164214d3e390000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 15:44:28','2','vip',1),('402880d26421aefe016421af67d50000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 17:31:41','2','vip',1),('402880d26421b984016421b9f5f30000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 17:43:13','2','vip',1),('402880d26421c431016421ca9da50000',NULL,'0:0:0:0:0:0:0:1','2018-06-21 18:01:24','2','vip',1),('402880d26421c431016421cb19e10001','XXXX内网IP','0:0:0:0:0:0:0:1','2018-06-21 18:01:56','1','admin',1),('402880d26421c431016421cb7afc0002',NULL,'0:0:0:0:0:0:0:1','2018-06-21 18:02:21','2','vip',1),('40289f9464e10f690164e11014bb0000',NULL,'0:0:0:0:0:0:0:1','2018-07-28 21:24:45','1','admin',1);

/*Table structure for table `manager` */

DROP TABLE IF EXISTS `manager`;

CREATE TABLE `manager` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `birthday` date DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKo0yekye62maw0eia889dcxyk7` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `manager` */

insert  into `manager`(`id`,`birthday`,`gender`,`password`,`real_name`,`state`,`username`,`avatar`,`create_time`,`update_time`) values ('1','1992-09-24',1,'$2a$04$a90t7tmpIjJl6Ic9PZTHgeJDqN4iRwk45s8AVmN10v9cZ3jYl0qk6','陈林',1,'admin','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRi9ZPVVFjY-IIlKeZ5TufzsnIsoExzREVOfv2tmlg272lYLn49xg','2018-04-25 02:34:31','2018-04-25 02:34:46'),('2','2018-04-11',1,'$2a$04$9KSovkBDzr7HvBjH7Monm.nor.x/mjgo9c9/eG8IeZ2Uh8gMhykoC','aa',1,'vip','http://p6wg9ob78.bkt.clouddn.com/Fstwz1bEPFOeVM8n0BfsLK2THome','2018-04-26 02:00:33','2018-05-07 01:53:20'),('2c9fa82862ae50a80162b2928821007e','1999-04-07',2,'$2a$04$XivsC2zMsxylvLgtvl2ASuQI9MBrMZDol6JbPNN6V/lHlhg7MV/Z.','呵呵',1,'hzhl','http://p6wg9ob78.bkt.clouddn.com/FleBXBCLSIbyWiLlQtcgds-MNKT1','2018-04-19 09:48:28','2018-04-24 02:34:11'),('2c9fa82862be6d1b0162bf5525e40013','2018-04-13',1,'$2a$04$s8Brjn7R8YtulSpfvBg1bOhGKQPHMEV4ZZ6bJ6StARk7f.bvLgbku','zzg',1,'zzg','','2018-04-14 07:41:01','2018-04-18 16:48:33'),('2c9fa82862be6d1b0162cca8b970014b','2018-04-16',1,'$2a$04$xFEX8RjmGK8ga73sKFk8MO3lhHnP8MfOUtTgSse4ElZnHBei4WEFq','陆22',0,'mengmeng','','2018-04-16 04:16:23','2018-05-02 03:24:17'),('2c9fa82862cccae60162d18e8afd00d4',NULL,2,'$2a$04$Tyx.vvPoaJHRNlGNVhDaY.6jvezVgS7HfMfydvmFAbXXKtid3coG.','1',1,'1','http://p6wg9ob78.bkt.clouddn.com/FkJsPJL_Xu3ZUOyQQ2Ri33YBWoy-','2018-04-24 06:16:58','2018-04-24 06:17:08'),('2c9fa82862cccae60162e1657b0a0424','2018-04-16',1,'$2a$04$b7glehublDszZorJgRDJ9ez/WqPF/ct2lOumza3xT1cVHCgO6nVJy','xu',0,'xu','http://p6wg9ob78.bkt.clouddn.com/FheFhqb-HWa0QZ1lAA0pnAboRH9v','2018-04-20 04:52:27','2018-04-20 04:52:27'),('2c9fa82862cccae60162e227a96d0474',NULL,2,'$2a$04$cNfSwCbljxXAS5NMgBaxFeWaFwEEfbAusucPpVogeOfvv3U6f2ZHq','',0,'张三丰','','2018-04-20 08:24:33','2018-05-02 04:15:50'),('2c9fa82862cccae60162fd5475740725','2018-04-02',1,'$2a$04$RsuCwyQENypU.djxYl76debZbbz1q82c8znEvhXnpXgKJZjaSHUbi','1',0,'111','1','2018-04-25 15:03:14','2018-04-25 15:03:14'),('2c9fa82862cccae60162fd54a9b20726','2018-04-02',1,'$2a$04$eK80LZIQ.czTtefR6MbLpuCGnkLuUQSEa3GeZBr06e.L8m57zyD3q','2',0,'21','111','2018-04-25 15:03:27','2018-04-25 15:03:27'),('2c9fa82862cccae60162ffc6ea6f0754','1997-04-26',2,'$2a$04$5D.TXpZknslQjiDat.GDSO/mix1/cY9t8X9ZlmF30t.9KApPXZo1O','zsh',0,'zsh','http://p6wg9ob78.bkt.clouddn.com/FhAfdvYslNifQASMXzW63_8oGUpK','2018-04-26 02:27:29','2018-04-26 02:27:29'),('2c9fa82862cccae60162ffdc148e0762','2018-04-26',1,'$2a$04$1QzOInDPpcfWkzxKzTXQTOhmlU29JNCGrU8sJh5A0ckypkQSLX5Oi','yf',2,'yfh','','2018-04-26 02:50:36','2018-05-02 07:03:29'),('2c9fa8286305138e0163053c895f0010','2014-04-28',1,'$2a$04$emANT5ZaRLTvskaPyxfOEOmvT./g2l10dCKqXRkkpiNoiLP5ubMSu','gf11112231',2,'ddff','http://p6wg9ob78.bkt.clouddn.com/FslKv7LmMFejPDbkpdprMazGwtAc','2018-04-27 03:54:03','2018-05-07 01:39:26'),('7',NULL,1,'$2a$04$CtsUyu2hPj1LxhoPiLw14ejs7mvqT.T6I322V5f1mJFOS5oCNIiNK','',1,'liu','','2018-04-16 03:29:04','2018-05-02 04:16:12');

/*Table structure for table `manager_role` */

DROP TABLE IF EXISTS `manager_role`;

CREATE TABLE `manager_role` (
  `role_id` bigint(20) NOT NULL,
  `manager_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`manager_id`,`role_id`),
  KEY `FKmhxeqq4liaxhs7sc6g7082hp4` (`role_id`),
  CONSTRAINT `FKi98goitstamcfe3vpdl5xux4` FOREIGN KEY (`manager_id`) REFERENCES `manager` (`id`),
  CONSTRAINT `FKmhxeqq4liaxhs7sc6g7082hp4` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `manager_role` */

insert  into `manager_role`(`role_id`,`manager_id`) values (1,'1'),(4,'2'),(4,'2c9fa82862ae50a80162b2928821007e'),(4,'2c9fa82862be6d1b0162cca8b970014b'),(4,'2c9fa82862cccae60162e227a96d0474'),(4,'2c9fa8286305138e0163053c895f0010');

/*Table structure for table `music` */

DROP TABLE IF EXISTS `music`;

CREATE TABLE `music` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `album` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `essay_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `singer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `artist` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lrc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `music` */

/*Table structure for table `news` */

DROP TABLE IF EXISTS `news`;

CREATE TABLE `news` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  `sended` bit(1) DEFAULT NULL,
  `sender_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `news` */

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `manager_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `notice_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `notice` */

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `parent_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `resource_type` enum('menu','button') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `purview` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `permission` */

insert  into `permission`(`id`,`name`,`parent_id`,`parent_ids`,`resource_type`,`sort`,`url`,`icon`,`purview`) values (1,'系统设置',0,'0','menu',0,'','&#xe614;','sys:view'),(2,'管理员列表',1,'0/1','menu',1,'/sys/adminList','&#xe612;','sys:admin'),(3,'菜单列表',1,'0/1','menu',3,'/sys/menuList','&#xe65f;','menu:view'),(4,'数据监控',1,'0/1','menu',5,'/druid/index.html','&#xe617;','sys:data'),(5,'日志列表',1,'0/1','menu',4,'/sys/loginLogs','&#xe629;','sys:log'),(6,'角色列表',1,'0/1','menu',2,'/sys/roleList','&#xe613;','role:view'),(11,'新增',2,'0/1/2','button',1,'','','admin:add'),(12,'删除',2,'0/1/2','button',3,'','','admin:del'),(13,'新增',3,'0/1/3','button',1,'','','menu:add'),(14,'删除',3,'0/1/3','button',3,'','','menu:del'),(15,'修改',6,'0/1/6','button',2,'','','role:alt'),(16,'删除',6,'0/1/6','button',3,'','','role:del'),(17,'重置密码',2,'0/1/2','button',4,'','','admin:reset'),(19,'修改',2,'0/1/2','button',2,'','','admin:alt'),(20,'授权',6,'0/1/6','button',4,'','','role:grant'),(21,'修改',3,'0/1/3','button',2,'','','menu:alt'),(22,'新增',6,'0/1/6','button',1,'','','role:add');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `available` tinyint(1) DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_type` tinyint(4) DEFAULT NULL,
  `role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKor7ok73vt9bsmxyc2t6s37qi5` (`role_code`,`role_type`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `role` */

insert  into `role`(`id`,`role_name`,`available`,`description`,`role_type`,`role_code`) values (1,'超管',1,'最牛逼的人,拥有毁天灭地的能力，一不小心就把自己玩死了',1,'admin'),(3,'小管',1,'没多大权力的人',1,'xg'),(4,'管理员',1,'这个是你们VIP用户的角色，请勿动它',1,'manger'),(6,'测试',1,'测试',2,'ceshi'),(48,'1',1,'1',2,'1'),(51,'菜111111111',0,'菜比',1,'2'),(53,'不死阿龙',1,'超级无敌VIP',1,'vip001'),(54,'12312332',1,'',1,'123123'),(55,'winson',1,'',1,'winson'),(56,'aaaaa',1,'',1,'aaa'),(57,'pengyin',1,'专注测试',1,'pengyin'),(58,'34',1,'34',1,'34'),(67,'JBJ',1,'sdfsdf',2,'sdf'),(68,'test',1,'test',1,'test'),(69,'admin11',1,'我是大哥',2,'admin11'),(70,'dddd',1,'dddd',1,'ddddddd'),(71,'tttt',1,'tret',1,'erter'),(73,'ertwr',1,'sdfg',1,'wrt'),(74,'aaaaaa',1,'aaaaa',1,'aaaaaa'),(75,'x',1,'x',1,'x'),(76,'hellojim',1,'hellojim',1,'hellojim'),(77,'123',1,'',1,'344'),(80,'1',1,'1',1,'1'),(81,'12',1,'12',1,'12'),(82,'1234567',1,'gdfsd ',1,'12345'),(85,'test111',1,'test111',1,'test111'),(92,'afas',1,'qrqwrqr',1,'weqrweqrw'),(98,'12222',1,'122',1,'1222'),(100,'9',1,'1',1,'9'),(105,'121',1,'12',1,'212'),(106,'发送到发',1,' 阿萨德',1,'萨达'),(108,'123456',1,'',1,'123'),(111,'aaa',1,'',1,'aaa123');

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

insert  into `role_permission`(`role_id`,`permission_id`) values (1,1),(1,6),(1,16),(1,15),(1,22),(1,20),(1,3),(1,13),(1,21),(1,14),(1,4),(1,5),(1,2),(1,19),(1,11),(1,12),(1,17),(4,1),(4,4),(4,2),(4,19),(4,17),(4,3),(4,13),(4,21),(4,6),(4,22);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `person_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user` */

insert  into `user`(`id`,`address`,`avatar`,`balance`,`birthday`,`create_time`,`credit`,`email`,`gender`,`last_login`,`level`,`nick_name`,`password`,`person_desc`,`phone`,`real_name`,`signature`,`state`,`update_time`,`username`) values ('40289f946508da04016508db5d1d0000',NULL,NULL,NULL,NULL,NULL,NULL,'53123775@qq.com',NULL,NULL,NULL,NULL,'$2a$04$eK8ADtpXzGUtbHNzl5x.PebvQmhyCY7KLp29VSH5OVwhjZHvtjfRS',NULL,NULL,'王八蛋的',NULL,NULL,NULL,'hehe');

/*Table structure for table `user_faves` */

DROP TABLE IF EXISTS `user_faves`;

CREATE TABLE `user_faves` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `essay_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fave_type` tinyint(4) DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user_faves` */

/*Table structure for table `user_follow` */

DROP TABLE IF EXISTS `user_follow`;

CREATE TABLE `user_follow` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `follow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user_follow` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `user_role` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
