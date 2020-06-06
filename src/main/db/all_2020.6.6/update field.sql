# 给cms_keyword表建立索引
CREATE INDEX COMPANY_CODE ON `db_keyword`.`cms_keyword`(`COMPANY_CODE`);
CREATE INDEX SEARCH_ENGINE ON `db_keyword`.`cms_keyword`(`SEARCH_ENGINE`, `TERMINAL_TYPE`, `KEYWORD_GROUP`, `COMPANY_CODE`);

# 修改字段类型或长度 
alter table cms_keyword_position_history change CUSTOMER_POSITION CUSTOMER_POSITION varchar(20) DEFAULT NULL COMMENT '客户提供的排名';
alter table cms_keyword_position_history change SEARCH_ENGINE SEARCH_ENGINE varchar(20) NOT NULL COMMENT '搜索引擎';
alter table cms_keyword_position_history change TERMINAL_TYPE TERMINAL_TYPE varchar(10) NOT NULL COMMENT '终端';
alter table cms_keyword_position_history change TODAY_FEE TODAY_FEE double(10,2) NOT NULL COMMENT '今日收费';

