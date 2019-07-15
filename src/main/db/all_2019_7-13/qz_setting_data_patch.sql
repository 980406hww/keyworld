
# fDataProcessingStatus字段用于判断是否计算达标
# 把aiZhan类的 全部设置为不计算 0， 后面用存储过程把不是附带爬取爱站的站点在设置回来
UPDATE t_qz_keyword_rank_info SET fDataProcessingStatus = 0 WHERE fWebsiteType = 'aiZhan';
# 吧除了aiZhan之外的，全部设置为计算达标 1
UPDATE t_qz_keyword_rank_info SET fDataProcessingStatus = 1 WHERE (fWebsiteType = 'designationWord' OR fWebsiteType = '5118') AND fDataProcessingStatus = 0;

# aiZhan类 非指定词附带添加的信息
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_qk_data_flag_update_1`;
CREATE PROCEDURE pro_qk_data_flag_update_1()
BEGIN
DECLARE qk_uuid INT DEFAULT 0;
DECLARE done INT DEFAULT 0;
DECLARE qk_data_flag_update_cursor CURSOR FOR (
		SELECT fUuid
		FROM t_qz_keyword_rank_info qk 
		GROUP BY qk.fQZSettingUuid, qk.fTerminalType
		HAVING COUNT(qk.fQZSettingUuid) = 1
) ;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
		OPEN qk_data_flag_update_cursor;
			out_loop:
			LOOP
			FETCH qk_data_flag_update_cursor INTO qk_uuid;
			IF done = 1 THEN
				LEAVE out_loop;
			END IF;
			BEGIN
			UPDATE t_qz_keyword_rank_info SET fDataProcessingStatus = 1 WHERE fUuid = qk_uuid;
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE qk_data_flag_update_cursor;
	COMMIT;
END;

CALL pro_qk_data_flag_update_1();

DROP PROCEDURE `pro_qk_data_flag_update_1`;

# 给几个字段设置有作用的默认值
ALTER TABLE t_qz_keyword_rank_info ALTER COLUMN fAchieveLevel SET DEFAULT 0;
ALTER TABLE t_qz_keyword_rank_info ALTER COLUMN fDifferenceValue SET DEFAULT 1;
ALTER TABLE t_qz_keyword_rank_info ALTER COLUMN fTodayDifference SET DEFAULT 0;

# 对这些字段进行null值的修改
UPDATE t_qz_keyword_rank_info SET fAchieveLevel = 0 WHERE fAchieveLevel IS NULL;
UPDATE t_qz_keyword_rank_info SET fDifferenceValue = 1 WHERE fDifferenceValue IS NULL AND fDataProcessingStatus = 0;
UPDATE t_qz_keyword_rank_info SET fTodayDifference = 0 WHERE fTodayDifference IS NULL;

# 代码上线后，再跑下面的语句(指定词的爬取会修改fDataProcessingStatus标识)
UPDATE t_qz_keyword_rank_info SET fDataProcessingStatus = 1 WHERE fWebsiteType = 'designationWord' AND fDataProcessingStatus = 0;
