
# 更新指定客户
update t_config
set fValue = 'liebiaobaidu,liebiaosougou,liebiao360,guorenbaidu'
where fConfigType = 'SyncCustomerPtKeyword'
  and fKey = 'SyncCustomerName';

# 配置最近同步时间
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`)
VALUES ('SyncPtKeywordTime', 'guorenbaidu', '2020-06-29 18:40');

# 同步指定客户的pt关键词
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_pt_keyword` $$
CREATE PROCEDURE pro_pt_keyword(customerName VARCHAR(255))
BEGIN
    DECLARE keyword_uuid INT DEFAULT 0;
    DECLARE done INT DEFAULT 0;
    DECLARE pt_keyword_cursor CURSOR FOR (
        SELECT
            ck.fUuid AS 'keywordUuid'
        FROM
            t_customer_keyword ck, t_customer c
        WHERE c.fUuid = ck.fCustomerUuid
          AND c.fContactPerson = customerName
          AND ck.fType = 'pt'
    );
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    START TRANSACTION;
    OPEN pt_keyword_cursor;
    out_loop:
    LOOP
        FETCH pt_keyword_cursor INTO keyword_uuid;
        IF done = 1 THEN
            LEAVE out_loop;
        END IF;

        BEGIN

            REPLACE INTO cms_keyword(CUSTOMER_KEYWORD_ID, KEYWORD, URL, TITLE, SEARCH_ENGINE, TERMINAL_TYPE, BEAR_PAW_NUM, PRICE_PER_DAY, CURRENT_POSITION, STATUS, CITY, COMPANY_CODE,CAPTURE_POSITION_CITY, CAPTURE_POSITION_TIME, CAPTURE_STATUS, CREATE_TIME)
            SELECT
                keyword_uuid, fKeyword, fUrl, fTitle, fSearchEngine, fTerminalType, fBearPawNumber, IF(fPositionFirstPageFee > '',fPositionFirstPageFee, 0.00),
                fCurrentPosition, fStatus, fCity, customerName, fCapturePositionCity, fCapturePositionQueryTime, fCaptureStatus, fCreateTime
            FROM t_customer_keyword
            WHERE fUuid = keyword_uuid;

        END;
        SET done = 0;
    END LOOP out_loop;
    CLOSE pt_keyword_cursor;
    COMMIT;
END;

# guorenbaidu
CALL pro_pt_keyword('guorenbaidu');

DROP PROCEDURE `pro_pt_keyword`;

# 配置客户新增词的默认优化组
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`)
VALUES ('DefaultOptimizeGroup', 'DefaultOptimizeGroupName', 'Default');

# 配置客户新增词的默认机器分组
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`)
VALUES ('DefaultMachineGroup', 'DefaultMachineGroupName', 'super');

# 配置同步更新排名sql的行数
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`)
VALUES ('SyncKeywordRowNumber', 'DefaultRowNumber', 15000);

# 配置新增词一次操作的大小
INSERT INTO `db_keyword`.`t_config` (`fConfigType`, `fKey`, `fValue`)
VALUES ('KeywordSubListSize', 'DefaultSize', 3000);

