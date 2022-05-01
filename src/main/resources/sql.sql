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



DROP TABLE IF EXISTS `t_goods`;
create table if not exists `t_goods`(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `goods_name` VARCHAR(64) NOT NULL COMMENT '商品名称',
    `goods_title` VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
    `goods_img` VARCHAR(128) DEFAULT NULL COMMENT '商品图像URL',
    `goods_detail` LONGTEXT COMMENT '商品详情',
    `goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品价格',
    `goods_stock` INT(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
    PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `t_secdkill_goods`;
create table if not exists `t_secdkill_goods`(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀商品ID',
    `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品名称',
    `secdkill_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '秒杀价格',
    `stock_count` INT(11) DEFAULT NULL COMMENT '库存数量',
    `start_date` DATETIME DEFAULT NULL COMMENT '秒杀开始时间',
    `end_date` DATETIME DEFAULT NULL COMMENT '秒杀结束时间',
    PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `t_order`;
create table if not exists `t_order`(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
    `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品ID',
    `delivery_addr_id` BIGINT(20) DEFAULT NULL COMMENT '收货地址ID',
    `goods_name` VARCHAR(64) DEFAULT NULL COMMENT '冗余的商品名称，方便查询',
    `goods_count` INT(11) DEFAULT '0' COMMENT '下单的商品数量',
    `goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品价格',
    `order_channel` TINYINT(4) DEFAULT '0' COMMENT '1 pc;2 Android; 3 IOS',
    `statue` TINYINT(4) DEFAULT '0' COMMENT '订单状态,0 新建未支付; 1 已支付; 2 已发货;3 已收货; 4 已退款; 5 已完成',
    `create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
    `pay_date` datetime DEFAULT NULL COMMENT '订单支付时间',
    PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `t_secdkill_order`;
create table if not exists `t_secdkill_order`(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀订单ID',
    `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单ID',
    `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品ID',
    PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;