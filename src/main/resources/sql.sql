DROP TABLE IF EXISTS `t_user`;
create table IF NOT EXISTS `t_user`(
    `id` BIGINT(20) NOT NULL COMMENT '用手机号码当成用户ID',
    `nickname` varchar(255) NOT NULL COMMENT '用户昵称',
    `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass+salt)+salt)',
    `salt` varchar(10) DEFAULT NULL,
    `head` varchar(128) DEFAULT NULL COMMENT '头像url',
    `register_date` datetime DEFAULT NULL COMMENT '注册时间',
    `last_login_date` datetime DEFAULT NULL COMMENT '最后一次登录时间',
    `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
    PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;