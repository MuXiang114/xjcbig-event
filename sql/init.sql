-- ============================================================
-- 大事件 数据库初始化脚本
-- 用法：mysql -uroot -p < sql/init.sql
-- 注意：会先删除并重建 user / category / article 三张表，已有数据会被清空
-- ============================================================

CREATE DATABASE IF NOT EXISTS `big_event` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `big_event`;

DROP TABLE IF EXISTS `article`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username`    varchar(20)  NOT NULL COMMENT '用户名',
  `password`    varchar(32)  DEFAULT NULL COMMENT '密码（MD5）',
  `nickname`    varchar(10)  DEFAULT '' COMMENT '昵称',
  `email`       varchar(128) DEFAULT '' COMMENT '邮箱',
  `user_pic`    varchar(128) DEFAULT '' COMMENT '头像',
  `create_time` datetime     NOT NULL COMMENT '创建时间',
  `update_time` datetime     NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `category` (
  `id`            int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `category_name` varchar(32)  NOT NULL COMMENT '分类名称',
  `category_alias` varchar(32) NOT NULL COMMENT '分类别名',
  `create_user`   int unsigned NOT NULL COMMENT '创建人ID',
  `create_time`   datetime     NOT NULL COMMENT '创建时间',
  `update_time`   datetime     NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `fk_category_user` (`create_user`),
  CONSTRAINT `fk_category_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

CREATE TABLE `article` (
  `id`          int unsigned  NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title`       varchar(30)   NOT NULL COMMENT '文章标题',
  `content`     varchar(10000) NOT NULL COMMENT '文章内容',
  `cover_img`   varchar(128)  NOT NULL COMMENT '文章封面',
  `state`       varchar(3)    DEFAULT '草稿' COMMENT '文章状态：只能是[已发布]或[草稿]',
  `category_id` int unsigned  DEFAULT NULL COMMENT '文章分类ID',
  `create_user` int unsigned  NOT NULL COMMENT '创建人ID',
  `create_time` datetime      NOT NULL COMMENT '创建时间',
  `update_time` datetime      NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `fk_article_category` (`category_id`),
  KEY `fk_article_user` (`create_user`),
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_article_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- ------------------------------------------------------------
-- 演示账号：demo123 / 123456
-- 密码为 MD5("123456") = e10adc3949ba59abbe56e057f20f883e
-- ------------------------------------------------------------
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `user_pic`, `create_time`, `update_time`)
VALUES ('demo123', 'e10adc3949ba59abbe56e057f20f883e', '演示账号', 'demo@example.com', '', NOW(), NOW());

SET @uid = (SELECT `id` FROM `user` WHERE `username` = 'demo123');

INSERT INTO `category` (`category_name`, `category_alias`, `create_user`, `create_time`, `update_time`) VALUES
('美食', 'food', @uid, NOW(), NOW()),
('娱乐', 'ent',  @uid, NOW(), NOW()),
('军事', 'mil',  @uid, NOW(), NOW());

INSERT INTO `article` (`title`, `content`, `cover_img`, `state`, `category_id`, `create_user`, `create_time`, `update_time`)
VALUES ('示例文章：陕西旅游攻略',
        '<p>兵马俑、华清池、法门寺、华山，爱去哪去哪。</p>',
        'https://big-event-gongcheng1.oss-cn-beijing.aliyuncs.com/3a657aa5-2bca-46b9-b093-c0d0c8d58d62.png',
        '已发布',
        (SELECT `id` FROM `category` WHERE `category_name` = '美食' AND `create_user` = @uid),
        @uid, NOW(), NOW());
