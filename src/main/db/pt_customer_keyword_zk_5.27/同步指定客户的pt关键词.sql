
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
			
			INSERT INTO cms_keyword(KEYWORD,URL,TITLE,SEARCH_ENGINE,TERMINAL_TYPE,BEAR_PAW_NUM,PRICE_PER_DAY,CURRENT_POSITION,STATUS,COMP_STATUS, CITY,COMPANY_CODE,CAPTURE_POSITION_CITY,CAPTURE_POSITION_TIME,CAPTURE_STATUS,CREATE_TIME)
			SELECT
				fKeyword, fUrl, fTitle, fSearchEngine, fTerminalType, fBearPawNumber, IF(fPositionFirstPageFee > '',fPositionFirstPageFee, 0.00), fCurrentPosition, fStatus, IF(fCurrentPosition > 0 and fCurrentPosition <= 10, 1, 0), 
				fCity, customerName, fCapturePositionCity, fCapturePositionQueryTime, fCaptureStatus, fCreateTime
			FROM t_customer_keyword
			WHERE fUuid = keyword_uuid;
			
			INSERT INTO cms_keyword_position_history(KEYWORD_ID, SYSTEM_POSITION, SEARCH_ENGINE,TERMINAL_TYPE,TODAY_FEE,RECORD_DATE)
			SELECT
					fUuid, fCurrentPosition, fSearchEngine, fTerminalType,
					IF(fCurrentPosition > 0 and fCurrentPosition <= 10 and fPositionFirstPageFee > '', fPositionFirstPageFee, 0.00),
					CURRENT_DATE()
			FROM t_customer_keyword
			WHERE fUuid = keyword_uuid;
			
			# 下架指定客户的关键词 (注意！！！！)
			# UPDATE t_customer_keyword SET fStatus = 3 WHERE fUuid = keyword_uuid;
			
			# 真删  (注意！！！！)
			# DELETE FROM t_customer_keyword WHERE fUuid = keyword_uuid;
			# DELETE FROM t_ck_position_summary WHERE fCustomerKeywordUuid = keyword_uuid;
			
			END;
			SET done = 0;
			END LOOP out_loop;
		CLOSE pt_keyword_cursor;
	COMMIT;
END;

#liebiaobaidu,liebiaosougou,liebiao360
CALL pro_pt_keyword("liebiaobaidu");
CALL pro_pt_keyword("liebiaosougou");
CALL pro_pt_keyword("liebiao360");

DROP PROCEDURE `pro_pt_keyword`;


