ALTER TABLE `db_keyword`.`t_website`ADD COLUMN `fBackgroundLoginStatus` TINYINT NULL COMMENT '网站后台登录状态 0：正常、1：url链接出错、2：用户名密码错误' after `fSynchronousAdvertisingSign` ;

ALTER TABLE `db_keyword`.`t_website`ADD COLUMN `fSftpStatus` TINYINT NULL COMMENT 'sftp服务状态 0：正常、1：失败' after `fBackgroundLoginStatus` ;

ALTER TABLE `db_keyword`.`t_website`ADD COLUMN `fIndexFileStatus` TINYINT NULL COMMENT 'index文件大小 0：正常、1：异常' after `fSftpStatus` ;

ALTER TABLE `db_keyword`.`t_website`ADD COLUMN `fDatabaseStatus` VARCHAR(100) NULL COMMENT '数据库状态 0：正常、其他：数据库密码' after `fIndexFileStatus` ;



#增加更新网站的开关 0：关闭 1：打开
insert into `db_keyword`.`t_config` VALUE("WebsiteCheckSign","WebsiteCheck","1");