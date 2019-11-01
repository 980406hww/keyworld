
UPDATE t_customer_keyword SET fCaptureIndexQueryTime = DATE_SUB(NOW(), INTERVAL 1 DAY) WHERE fCurrentIndexCount = -1 AND fType = 'qz' AND fCaptureIndexQueryTime IS NULL;