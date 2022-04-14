项目名称：图书管理系统 
项目框架：spring boot + mybatis + redis + mysql 
环境：JDK1.8 
数据库：mysql 5.7.33 
项目简介：实现了用户管理、图书管理、登录认证、统一异常和redis缓存；使用了SpringBoot + Jwt实现后台接口权限认证；
数据库环境配置：

#用户表
DROP TABLE IF EXISTS `ums_user`;
create table `ums_user` (
	`id` bigint (15),
	`username` varchar (192),
	`password` varchar (192),
	`icon` varchar (1500),
	`email` varchar (300),
	`nick_name` varchar (600),
	`note` varchar (1500),
	`create_time` datetime ,
	`login_time` datetime ,
	`status` int (1),
	`is_delete` int (1)
)ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='用户表';
insert into `ums_user` (`id`, `username`, `password`, `icon`, `email`, `nick_name`, `note`, `create_time`, `login_time`, `status`, `is_delete`) values('1493837577194500097','admin','admin',NULL,NULL,NULL,NULL,'2022-02-16 14:40:07',NULL,'1','1');

#图书表
DROP TABLE IF EXISTS `lib_book`;
create table `lib_book` (
	`id` bigint (20),
	`name` varchar (90),
	`author` varchar (90),
	`publisher` varchar (90),
	`publish_date` datetime ,
	`description` varchar (1500),
	`create_time` datetime ,
	`update_time` datetime 
)ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='图书表'; 
insert into `lib_book` (`id`, `name`, `author`, `publisher`, `publish_date`, `description`, `create_time`, `update_time`) values('10','射雕英雄传','金庸','广州出版社','1957-02-16 17:17:37','射雕英雄传','2022-02-17 14:53:15',NULL);
insert into `lib_book` (`id`, `name`, `author`, `publisher`, `publish_date`, `description`, `create_time`, `update_time`) values('11','连城诀','金庸','广州出版社','1963-02-17 16:26:27','连城诀','2022-02-17 15:49:29',NULL);
