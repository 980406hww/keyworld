
# 下面的处理并未完全处理好全站关键词关联站点id，前台功能辅助下可实现完全关联
# 给全站未下架电脑端关键字关联对应站点uuid
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_ck_qz_pc_migration`;
CREATE PROCEDURE pro_ck_qz_pc_migration ()
BEGIN
	DECLARE ck_customeruuid INT ( 11 ) DEFAULT NULL;
	DECLARE ck_url VARCHAR ( 128 ) DEFAULT NULL;
	DECLARE ck_searchengine VARCHAR ( 100 ) DEFAULT NULL;
	DECLARE ck_groupname VARCHAR ( 20 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE ck_qz_pc_migration_cursor CURSOR FOR (
			SELECT ck.fCustomerUuid, ck.fUrl, ck.fSearchEngine, ck.fOptimizeGroupName
			FROM t_customer_keyword ck
			LEFT JOIN t_qz_setting qs
			ON qs.fCustomerUuid = ck.fCustomerUuid
			WHERE ck.fTerminalType = 'PC'
			AND ck.fType = 'qz'
			AND ck.fQZSettingUuid IS NULL
			AND qs.fSearchEngine = ck.fSearchEngine
			AND qs.fPcGroup > ''
			AND qs.fPcGroup = ck.fOptimizeGroupName
			GROUP BY ck.fCustomerUuid, ck.fUrl, ck.fSearchEngine, ck.fOptimizeGroupName
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN ck_qz_pc_migration_cursor;
	out_loop:
	LOOP
		FETCH ck_qz_pc_migration_cursor INTO ck_customeruuid, ck_url, ck_searchengine, ck_groupname;
		IF done = 1 THEN
		LEAVE out_loop;
		END IF;
		BEGIN
			SET @qzSettingUuid = (
					SELECT fUuid FROM t_qz_setting
					WHERE fCustomerUuid = ck_customeruuid AND fDomain = ck_url AND fSearchEngine = ck_searchengine AND fPcGroup = ck_groupname
			);
			IF @qzSettingUuid > '' THEN
					UPDATE t_customer_keyword SET fQZSettingUuid = @qzSettingUuid
					WHERE fCustomerUuid = ck_customeruuid AND fUrl = ck_url AND fTerminalType = 'PC' AND fSearchEngine = ck_searchengine
					AND fOptimizeGroupName = ck_groupname AND fQZSettingUuid IS NULL;
			END IF;
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE ck_qz_pc_migration_cursor;
	COMMIT;
END;

CALL pro_ck_qz_pc_migration();

DROP PROCEDURE pro_ck_qz_pc_migration;

# 给全站未下架移动端关键字关联对应站点uuid
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_ck_qz_phone_migration`;
CREATE PROCEDURE pro_ck_qz_phone_migration ()
BEGIN
	DECLARE ck_customeruuid INT ( 11 ) DEFAULT NULL;
	DECLARE ck_url VARCHAR ( 128 ) DEFAULT NULL;
	DECLARE ck_searchengine VARCHAR ( 100 ) DEFAULT NULL;
	DECLARE ck_groupname VARCHAR ( 20 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE ck_qz_phone_migration_cursor CURSOR FOR (
			SELECT ck.fCustomerUuid, ck.fUrl, ck.fSearchEngine, ck.fOptimizeGroupName
			FROM t_customer_keyword ck
			LEFT JOIN t_qz_setting qs
			ON qs.fCustomerUuid = ck.fCustomerUuid
			WHERE ck.fTerminalType = 'Phone'
			AND ck.fType = 'qz'
			AND ck.fQZSettingUuid IS NULL
			AND qs.fSearchEngine = ck.fSearchEngine
			AND qs.fPhoneGroup > ''
			AND qs.fPhoneGroup = ck.fOptimizeGroupName
			GROUP BY ck.fCustomerUuid, ck.fUrl, ck.fSearchEngine, ck.fOptimizeGroupName
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN ck_qz_phone_migration_cursor;
	out_loop:
	LOOP
		FETCH ck_qz_phone_migration_cursor INTO ck_customeruuid, ck_url, ck_searchengine, ck_groupname;
		IF done = 1 THEN
		LEAVE out_loop;
		END IF;
		BEGIN
			SET @qzSettingUuid = (
					SELECT fUuid FROM t_qz_setting
					WHERE fCustomerUuid = ck_customeruuid AND fDomain = ck_url AND fSearchEngine = ck_searchengine AND fPhoneGroup = ck_groupname
			);
			IF @qzSettingUuid > '' THEN
					UPDATE t_customer_keyword SET fQZSettingUuid = @qzSettingUuid
					WHERE fCustomerUuid = ck_customeruuid AND fUrl = ck_url AND fTerminalType = 'Phone' AND fSearchEngine = ck_searchengine
					AND fOptimizeGroupName = ck_groupname AND fQZSettingUuid IS NULL;
			END IF;
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE ck_qz_phone_migration_cursor;
	COMMIT;
END;
CALL pro_ck_qz_phone_migration();

DROP PROCEDURE pro_ck_qz_phone_migration;