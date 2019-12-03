
# 删除索引
ALTER TABLE t_negative_list DROP INDEX NewIndex1;
ALTER TABLE t_negative_list DROP INDEX NewIndex2;
ALTER TABLE t_negative_list DROP INDEX NewIndex3;
# 建立索引
CREATE INDEX NewIndex1 ON `db_keyword`.`t_negative_list`(fKeyword(20), fTitle(50), fUrl(40));
CREATE INDEX NewIndex1 ON `db_keyword`.`t_positive_list`(fKeyword(20), fTitle(50));
CREATE INDEX NewIndex1 ON `db_keyword`.`t_warn_list`(fKeyword(20), fTitle(50));
CREATE INDEX NewIndex1 ON `db_keyword`.`t_positive_list_update_info`(fPid);