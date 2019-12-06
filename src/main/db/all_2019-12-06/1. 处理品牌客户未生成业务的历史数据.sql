
# 处理品牌客户未生成业务的历史数据
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_business_data_migration`;
CREATE PROCEDURE pro_business_data_migration ()
BEGIN
	DECLARE c_uuid BIGINT ( 19 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE business_data_migration_cursor CURSOR FOR (
		SELECT fUuid, fEntryType 
		FROM t_customer 
		WHERE fEntryType = 'fm' 
		AND fUuid NOT IN (
			SELECT fCustomerUuid FROM t_customer_business WHERE fType = 'fm'
		)
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	START TRANSACTION;
	OPEN business_data_migration_cursor;
	out_loop:
	LOOP
		FETCH business_data_migration_cursor INTO c_uuid;
		IF done = 1 THEN
		LEAVE out_loop;	
		END IF;
		BEGIN
				INSERT INTO t_customer_business SET fCustomerUuid = c_uuid, fType = 'fm';
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE business_data_migration_cursor;
	COMMIT;
END;

CALL pro_business_data_migration();
DROP PROCEDURE pro_business_data_migration;
