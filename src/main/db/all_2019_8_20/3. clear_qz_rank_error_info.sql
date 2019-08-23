
# 清除非百度的系统排名曲线
DELETE FROM t_qz_keyword_rank_info 
WHERE fWebsiteType = 'xt' 
AND fQZSettingUuid IN(
	SELECT qs.fUuid
	FROM t_qz_setting qs
	WHERE qs.fSearchEngine != '百度'
);

# 清除百度移动的系统排名曲线
DELETE FROM t_qz_keyword_rank_info 
WHERE fWebsiteType = 'xt' 
AND fTerminalType = 'Phone'
AND fQZSettingUuid IN(
	SELECT qs.fUuid
	FROM t_qz_setting qs
	WHERE qs.fSearchEngine = '百度'
);

				
