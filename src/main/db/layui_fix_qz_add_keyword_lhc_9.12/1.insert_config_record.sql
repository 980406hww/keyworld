
#往config表插入数据
# 所有搜索引擎
INSERT INTO `db_keyword`.`t_config`(`fConfigType`, `fKey`, `fValue`) VALUES ('SearchEngine', 'All', '谷歌,百度,搜狗,必应中国,百度下拉,360,必应日本');

# 重点词的预设刷量
INSERT INTO `db_keyword`.`t_config`(`fConfigType`, `fKey`, `fValue`) VALUES ('KeywordEffectOptimizePlanCount', 'ImportantKeyword', '100');