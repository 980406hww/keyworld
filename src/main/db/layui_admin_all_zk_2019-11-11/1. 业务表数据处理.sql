
# 清空t_customer_business表的数据
DELETE FROM t_customer_business WHERE 1=1;
# 给还没有生成业务记录的客户生成对应数据
DELIMITER $$
DROP PROCEDURE IF EXISTS `pro_business_data_migration`;
CREATE PROCEDURE pro_business_data_migration ( IN resource_name VARCHAR ( 100 ), IN target_name VARCHAR ( 100 ) )
BEGIN
	DECLARE c_uuid BIGINT ( 19 ) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE business_data_migration_cursor CURSOR FOR (
		SELECT fUuid
		FROM t_customer c
		WHERE c.fEntryType = resource_name
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
				INSERT INTO t_customer_business SET fCustomerUuid = c_uuid, fType = target_name;
		END;
		SET done = 0;
	END LOOP out_loop;
	CLOSE business_data_migration_cursor;
	COMMIT;
END;

 CALL pro_business_data_migration("qz", "qzsetting");
 CALL pro_business_data_migration("pt", "keyword");
 CALL pro_business_data_migration("fm", "fm");
 CALL pro_business_data_migration("qt", "qt");

 DROP PROCEDURE pro_business_data_migration;