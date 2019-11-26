
# 设置全站关键词fqzsettinguuid为null
UPDATE t_customer_keyword SET fQZSettingUuid = NULL;

# 给全站pc端关键字关联对应站点uuid
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_ck_qz_pc_migration`;
CREATE PROCEDURE pro_ck_qz_pc_migration ( )
BEGIN
	DECLARE qz_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE c_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE qz_search_engine VARCHAR ( 20 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE ck_qz_pc_cursor CURSOR FOR (
		SELECT
			qs.fUuid,
			qs.fCustomerUuid,
			qs.fSearchEngine
		FROM t_qz_setting qs
		WHERE qs.fPcGroup > ''
			AND EXISTS (
				SELECT 1
				FROM t_customer_keyword ck
				LEFT JOIN t_customer c ON ck.fCustomerUuid = c.fUuid
				LEFT JOIN t_userinfo ui ON ui.fLoginName = c.fUserID
				WHERE qs.fCustomerUuid = ck.fCustomerUuid
				AND ck.fType = 'qz'
				AND ck.fTerminalType = 'PC'
				AND ui.fStatus = 0
				LIMIT 1
			)
		GROUP BY qs.fCustomerUuid, qs.fSearchEngine
		HAVING COUNT(qs.fCustomerUuid) = 1
		ORDER BY qs.fCustomerUuid ASC
		);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN ck_qz_pc_cursor;
	out_loop:
	LOOP
		FETCH ck_qz_pc_cursor INTO qz_uuid, c_uuid, qz_search_engine;
		IF done = 1 THEN
			LEAVE out_loop;
		END IF;
		BEGIN
				UPDATE t_customer_keyword SET fQZSettingUuid = qz_uuid WHERE fCustomerUuid = c_uuid AND fSearchEngine = qz_search_engine
				AND fTerminalType = 'PC' AND fType = 'qz';
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE ck_qz_pc_cursor;
	COMMIT;
END;

CALL pro_ck_qz_pc_migration();
DROP PROCEDURE pro_ck_qz_pc_migration;

# 给全站phone端关键字关联对应站点uuid
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_ck_qz_phone_migration`;
CREATE PROCEDURE pro_ck_qz_phone_migration ( )
BEGIN
	DECLARE qz_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE c_uuid INT ( 11 ) DEFAULT NULL;
	DECLARE qz_search_engine VARCHAR ( 20 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE ck_qz_phone_cursor CURSOR FOR (
		SELECT
			qs.fUuid,
			qs.fCustomerUuid,
			qs.fSearchEngine
		FROM t_qz_setting qs
		WHERE qs.fPhoneGroup > ''
			AND EXISTS (
				SELECT 1
				FROM t_customer_keyword ck
				LEFT JOIN t_customer c ON ck.fCustomerUuid = c.fUuid
				LEFT JOIN t_userinfo ui ON ui.fLoginName = c.fUserID
				WHERE qs.fCustomerUuid = ck.fCustomerUuid
				AND ck.fType = 'qz'
				AND ck.fTerminalType = 'Phone'
				AND ui.fStatus = 0
				LIMIT 1
			)
		GROUP BY qs.fCustomerUuid, qs.fSearchEngine
		HAVING COUNT(qs.fCustomerUuid) = 1
		ORDER BY qs.fCustomerUuid ASC
		);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN ck_qz_phone_cursor;
	out_loop:
	LOOP
		FETCH ck_qz_phone_cursor INTO qz_uuid, c_uuid, qz_search_engine;
		IF done = 1 THEN
			LEAVE out_loop;
		END IF;
		BEGIN
				UPDATE t_customer_keyword SET fQZSettingUuid = qz_uuid WHERE fCustomerUuid = c_uuid AND fSearchEngine = qz_search_engine
				AND fTerminalType = 'Phone' AND fType = 'qz';
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE ck_qz_phone_cursor;
	COMMIT;
END;

CALL pro_ck_qz_phone_migration();
DROP PROCEDURE pro_ck_qz_phone_migration;

# 搜索客户-站点 一对多的数据 导出excel给到运营处理
SELECT c.fContactPerson, t1.fDomain, t1.fSearchEngine, t1.fPcGroup, t1.fPhoneGroup
FROM t_qz_setting t1
LEFT JOIN t_customer c
ON t1.fCustomerUuid = c.fUuid
WHERE t1.fCustomerUuid IN (
	SELECT qs.fCustomerUuid
	FROM t_qz_setting qs
	WHERE qs.fPcGroup > ''
	AND EXISTS (
			SELECT 1
			FROM t_customer_keyword ck
			LEFT JOIN t_customer c
			ON ck.fCustomerUuid = c.fUuid
			LEFT JOIN t_userinfo ui
			ON ui.fLoginName = c.fUserID
			WHERE qs.fCustomerUuid = ck.fCustomerUuid
			AND ck.fType = 'qz'
			AND ck.fTerminalType = 'PC'
			AND ui.fStatus = 0
			LIMIT 1
	)
	GROUP BY qs.fCustomerUuid, qs.fSearchEngine
	HAVING COUNT(qs.fCustomerUuid) > 1
	ORDER BY qs.fCustomerUuid ASC
)
ORDER BY c.fContactPerson;

