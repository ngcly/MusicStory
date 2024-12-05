insert  into `carousel_category`(`id`,`created_at`,`updated_at`,`created_by`,`updated_by`,`remark`,`title`) values
(1,'2019-01-19 11:50:40','2019-02-19 17:53:56','admin','admin','首页展示用','首页轮播图');

insert  into `carousel`(`id`,`created_at`,`updated_at`,`created_by`,`updated_by`,`forward_url`,`image_tip`,`image_url`,`sort`,`category_id`) values
(1,'2017-08-24 10:22:28','2021-08-24 10:22:36','admin','admin','www.baidu.com','第一张','https://oss.ngcly.cn/img/1551088500425.jpg?Expires=1866448493&OSSAccessKeyId=LTAI6bpHNc1Fjfb7&Signature=TPlBdlhpDX7nSVTVJnPE2JN2hPg%3D',1,1),
(2,'2021-08-24 10:53:07','2021-08-24 10:53:09','admin','admin','https://ngcly.cn','第二张','https://oss.ngcly.cn/img/1551088526951.jpg?Expires=1866448525&OSSAccessKeyId=LTAI6bpHNc1Fjfb7&Signature=fhiONy7Tmk9GwkHNDNzh%2B28uDLw%3D',2,1),
(3,'2021-08-24 10:53:43','2021-08-24 10:53:41','admin','admin','xxx','第三张','https://oss.ngcly.cn/img/1551890475084.jpg?Expires=1867250475&OSSAccessKeyId=LTAI6bpHNc1Fjfb7&Signature=xnYdW%2BKMUh0fI89jOq%2Fjs%2FIlcbY%3D',3,1);

insert  into `classify`(`id`,`created_at`,`updated_at`,`created_by`,`updated_by`,`introduction`,`name`) values
(1,'2018-12-29 14:52:53','2018-12-29 14:56:16','admin','admin','感情记录方面','情感实录'),
(2,'2018-12-29 15:04:52','2018-12-29 15:38:07','admin','admin','青春少女小心思','少女情感'),
(3,'2018-12-29 15:38:30','2018-12-29 15:38:30','admin','admin','年少轻狂','少年情怀'),
(4,'2018-12-29 15:39:06','2018-12-29 15:39:06','admin','admin','小时光','童年趣味'),
(5,'2018-12-29 15:39:51','2018-12-29 15:39:51','admin','admin','升职加薪','工作感想'),
(6,'2018-12-29 15:40:12','2018-12-29 15:40:12','admin','admin','平淡生活','生活琐碎'),
(7,'2018-12-29 15:40:50','2018-12-29 15:40:50','admin','admin','青春期','青春校园'),
(8,'2018-12-29 15:41:59','2018-12-29 15:41:59','admin','admin','深夜是孤独的影子','夜语日记'),
(9,'2018-12-29 15:44:08','2018-12-29 15:44:08','admin','admin','灵光一瞬','随感随悟'),
(10,'2018-12-29 15:46:30','2018-12-29 15:46:30','admin','admin','隔壁有光，红袖添香','诗词沉香'),
(11,'2018-12-29 15:47:54','2018-12-29 15:47:54','admin','admin','人在旅途，遍地芬芳','旅途之光');

insert  into `manager`(`id`,`created_at`,`updated_at`,`avatar`,`birthday`,`gender`,`password`,`real_name`,`state`,`username`) values
(1,'2018-12-26 11:56:00','2019-07-10 01:25:07','https://music-story.oss-cn-hongkong.aliyuncs.com/uPic/beautify.png','1992-09-21','MAN','$2a$04$a90t7tmpIjJl6Ic9PZTHgeJDqN4iRwk45s8AVmN10v9cZ3jYl0qk6','陈林','NORMAL','admin'),
(2,'2018-12-26 11:56:48','2020-05-30 14:11:33','https://music-story.oss-cn-hongkong.aliyuncs.com/uPic/cute.png','2018-03-18','MAN','$2a$04$6y1tJwU0WgqL6bnXC7Q2t.gfwhqI1lrSOBy9dLuBhrnNnXFFV88LC','睿智','NORMAL','vip');

insert  into `role`(`id`,`created_at`,`updated_at`,`created_by`,`updated_by`,`available`,`description`,`role_code`,`role_name`,`role_type`) values
(1,'2021-08-24 12:43:52','2022-07-22 14:17:04','system','admin',1,'最牛逼的人,拥有毁天灭地的能力，一不小心就把自己玩死了','admin','超管','ADMIN'),
(2,'2021-08-24 14:38:18','2021-08-24 14:38:21','admin','admin',1,'这个是你们VIP用户的角色，请勿动它','manger','管理员','ADMIN'),
(3,'2021-08-24 14:39:39','2022-07-22 14:31:57','system','admin',1,'前台管理员角色','vip','前台管理员','USER');

insert  into `permission`(`id`,`icon`,`name`,`parent_id`,`parent_ids`,`http_method`,`resource_type`,`sort`,`url`) values
(1,'&#xe614;','系统设置',0,'0','GET','MENU',0,'/'),
(2,'&#xe612;','管理员列表',1,'0/1','GET','MENU',1,'/sys/manager.html'),
(3,'&#xe663;','菜单列表',1,'0/1','GET','MENU',3,'/sys/menu.html'),
(4,'&#xe617;','数据监控',1,'0/1','GET','MENU',5,'/druid/index.html'),
(5,'&#xe629;','日志列表',1,'0/1','GET','MENU',4,'/sys/logs.html'),
(6,'&#xe613;','角色列表',1,'0/1','GET','MENU',2,'/sys/role.html'),
(7,'&#xe653;','音书管理',0,'0','GET','MENU',1,''),
(8,'&#xe60e;','公告列表',7,'0/7','GET','MENU',3,'/ms/notice.html'),
(9,'&#xe66f;','用户列表',7,'0/7','GET','MENU',0,'/ms/user.html'),
(10,'&#xe705;','文章列表',7,'0/7','GET','MENU',1,'/ms/essay.html'),
(11,'&#xe60a;','分类列表',7,'0/7','GET','MENU',2,'/ms/classify.html'),
(12,'&#xe634;','轮播图',7,'0/7','GET','MENU',4,'/ms/carousel.html'),
(13,'','新增',2,'0/1/2','POST','BUTTON',1,'/sys/manager'),
(14,'','删除',2,'0/1/2','DELETE','BUTTON',3,'/sys/manager'),
(15,'','新增',3,'0/1/3','POST','BUTTON',1,'/sys/menu'),
(16,'','删除',3,'0/1/3','DELETE','BUTTON',3,'/sys/menu'),
(17,'','修改',6,'0/1/6','PUT','BUTTON',2,'/sys/role'),
(18,'','删除',6,'0/1/6','DELETE','BUTTON',3,'/sys/role'),
(19,'','重置密码',2,'0/1/2','POST','BUTTON',4,'/sys/manager/pwd'),
(20,'','修改',2,'0/1/2','PUT','BUTTON',2,'/sys/manager'),
(21,'','授权',6,'0/1/6','POST','BUTTON',4,'/sys/role/grant'),
(22,'','修改',3,'0/1/3','PUT','BUTTON',2,'/sys/menu'),
(23,'','新增',6,'0/1/6','POST','BUTTON',1,'/sys/role'),
(24,'','删除',9,'0/7/9','DELETE','BUTTON',1,'/ms/user'),
(25,'','修改',9,'0/7/9','PUT','BUTTON',0,'/ms/user'),
(26,'','新增',23,'0/7/23','POST','BUTTON',0,'/ms/classify'),
(27,'','修改',23,'0/7/23','PUT','BUTTON',1,'/ms/classify'),
(28,'','删除',23,'0/7/23','DELETE','BUTTON',2,'/ms/classify'),
(29,'','新增',8,'0/7/8','POST','BUTTON',0,'/ms/notice'),
(30,'','修改',8,'0/7/8','PUT','BUTTON',1,'/ms/notice'),
(31,'','删除',8,'0/7/8','DELETE','BUTTON',2,'/ms/notice'),
(32,'','新增',12,'0/7/12','POST','BUTTON',0,'/ms/carousel'),
(33,'','修改',12,'0/7/12','PUT','BUTTON',1,'/ms/carousel'),
(34,'','删除',12,'0/7/12','DELETE','BUTTON',2,'/ms/carousel'),
(35,'','审核',10,'0/7/10','POST','BUTTON',0,'/ms/essay');

insert  into `role_permission`(`role_id`,`permission_id`) values
(1,7),
(1,23),
(1,28),
(1,26),
(1,27),
(1,8),
(1,33),
(1,34),
(1,35),
(1,9),
(1,24),
(1,25),
(1,22),
(1,29),
(1,30),
(1,31),
(1,17),
(1,32),
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
(2,7),
(2,23),
(2,26),
(2,27),
(2,8),
(2,28),
(2,29),
(2,9),
(2,25),
(2,22),
(2,30),
(2,31),
(2,17),
(2,32),
(2,1),
(2,2),
(2,10),
(2,18),
(2,3),
(2,12),
(2,20),
(2,5),
(2,4),
(2,6),
(2,21);

insert  into `manager_role`(`role_id`,`manager_id`) values
(1,1),
(2,2);

insert  into `user_info`(`id`,`created_at`,`updated_at`,`address`,`avatar`,`balance`,`birthday`,`credit`,`email`,`gender`,`level`,`nick_name`,`password`,`person_desc`,`phone`,`pwd_alt`,`real_name`,`signature`,`state`,`username`) values
(1,'2019-11-23 09:45:40','2019-11-27 18:05:58',NULL,'https://music-story.oss-cn-hongkong.aliyuncs.com/uPic/cute.png',NULL,NULL,NULL,'965315004@qq.com','MAN',NULL,'cly','{bcrypt}$2a$10$CXnnVeyyZGCjnFw9F1dR9eC8cnKBlDqsly1d2XEWxMyIaaSk5ny0W',NULL,NULL,NULL,NULL,NULL,'NORMAL','ngcly'),
(2,'2019-11-26 04:34:06','2019-11-26 04:45:52',NULL,NULL,NULL,NULL,NULL,'cly002@icloud.com','WOMAN',NULL,'ningxiner','{bcrypt}$2a$10$QVSpv0ayuEXqPPUt.Go2IOi1pQPa36/0ihjfXcjOD7jtaADFUDj1O',NULL,NULL,NULL,NULL,NULL,'NORMAL','ningxiner'),
(3,'2020-03-26 09:12:33','2020-03-26 04:13:18',NULL,'https://music-story.oss-cn-hongkong.aliyuncs.com/uPic/small.jpg',NULL,NULL,NULL,'188@169.com','MAN',NULL,'noname','{bcrypt}$2a$10$LughQMwQUzt0AKE9r99B0.W8xDQnDxGJy3Jrfick7N/OnSMJsD52q',NULL,NULL,NULL,NULL,NULL,'NORMAL','1111'),
(4,'2018-12-26 14:22:38','2018-12-28 17:33:06','湖北省武汉市','https://music-story.oss-cn-hongkong.aliyuncs.com/uPic/beautify.png',NULL,'2018-12-27',NULL,'53123775@qq.com','MAN',NULL,'风神','{bcrypt}$2a$10$zt3xDTDmnFFZdzaTZSPUhu.ZhvQYijtGpj4y5BrkBn/6lKi/SQQZ2','我从那里来，要到哪里去','1507886554',NULL,'王八蛋的','无个性不签名','NORMAL','hehe');

insert  into `notice`(`id`,`created_at`,`updated_at`,`created_by`,`updated_by`,`begin_time`,`content`,`end_time`,`notice_type`,`title`) values
(1,'2019-01-08 17:54:55','2020-06-07 21:19:26','ngcly','ngcly','2020-05-18 01:00:00','连雨不知春去，一晴方觉夏深。韶华易逝，流年易失。(⊙o⊙)…','2021-10-01 23:19:12','message','测试一条');
