# 全站新增字段fSearchEngine
ALTER TABLE `t_qz_setting` ADD COLUMN `fSearchEngine` VARCHAR(50) DEFAULT  NULL COMMENT '搜索引擎' AFTER `fDomain`;

# 改规范命名的站点的搜索引擎
# 改百度
UPDATE t_qz_setting SET fSearchEngine = '百度' WHERE fPcGroup LIKE 'pc_%' OR fPhoneGroup LIKE 'm_%';
# 改搜狗
UPDATE t_qz_setting SET fSearchEngine = '搜狗' WHERE fPcGroup LIKE 'sg_%' OR fPhoneGroup LIKE 'msg_%';
# 改360
UPDATE t_qz_setting SET fSearchEngine = '360' WHERE fPcGroup LIKE '360_%' OR fPhoneGroup LIKE 'm360_%';
# 改uc(神马)
UPDATE t_qz_setting SET fSearchEngine = '神马' WHERE fPhoneGroup LIKE 'uc_%';
# 改必应中国
UPDATE t_qz_setting SET fSearchEngine = '必应中国' WHERE fPcGroup LIKE 'by_cn%';
# 改必应日本
UPDATE t_qz_setting SET fSearchEngine = '必应日本' WHERE fPcGroup LIKE 'by_jp%';
# 改谷歌
UPDATE t_qz_setting SET fSearchEngine = '谷歌' WHERE fPcGroup LIKE 'google_%';

# 改不规范命名的站点的搜索引擎 如qzw_xxx_xxx
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_qs_search_engine_data_patch`;
CREATE PROCEDURE pro_qs_search_engine_data_patch()
BEGIN
DECLARE qs_uuid INT DEFAULT 0;
DECLARE ck_searchEngine VARCHAR(50) DEFAULT NULL;
DECLARE done INT DEFAULT 0;
DECLARE qs_search_engine_data_patch_cursor CURSOR FOR (
		SELECT
		qs.fUuid         AS 'qsUuid',
		ck.fSearchEngine AS 'searchEngine'
		FROM t_customer_keyword ck
		LEFT JOIN t_qz_setting qs
		ON qs.fCustomerUuid = ck.fCustomerUuid
		WHERE ck.fQZSettingUuid IS NULL
		AND qs.fUuid > ''
		AND qs.fSearchEngine IS NULL
		GROUP BY qs.fUuid, ck.fSearchEngine
) ;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
		OPEN qs_search_engine_data_patch_cursor;
			out_loop:
			LOOP
			FETCH qs_search_engine_data_patch_cursor INTO qs_uuid, ck_searchEngine;
			IF done = 1 THEN
				LEAVE out_loop;
			END IF;
			BEGIN
			UPDATE t_qz_setting SET fSearchEngine = ck_searchEngine WHERE fUuid = qs_uuid;
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE qs_search_engine_data_patch_cursor;
	COMMIT;
END;

CALL pro_qs_search_engine_data_patch();

DROP PROCEDURE `pro_qs_search_engine_data_patch`;

# 不规范命名的站点下无关键词，无法弄清是什么搜索引擎，查出来，让他们自己去确定
SELECT
ui.fUserName     AS  '用户名',
c.fContactPerson AS  '客户',
qs.fDomain       AS  '客户站点'
FROM t_qz_setting qs
JOIN t_customer c
ON c.fUuid = qs.fCustomerUuid
LEFT JOIN t_userinfo ui
ON c.fUserID = ui.fLoginName
WHERE qs.fSearchEngine IS NULL;