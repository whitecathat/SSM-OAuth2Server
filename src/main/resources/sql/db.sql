use demo;

create database center default character set utf8 collate utf8_general_ci;

use center;

CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

select * from user;

use center;
drop table user;

CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` bigint(2) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

drop table localUser;
CREATE TABLE `localUser` (
  `uid` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`),
  foreign key(uid)references user(uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `thirdUser` (
  `uid` bigint(20) NOT NULL,
  `thirdUniqueId` varchar(255) NOT NULL,
  `typeName` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`),
  foreign key(uid)references user(uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clientId` varchar(255) NOT NULL,
  `clientSecret` varchar(255) NOT NULL,
  `directUri` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;