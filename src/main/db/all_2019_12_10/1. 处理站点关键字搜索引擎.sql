
# 服务端端口修改时，新增一部分站点并加了词，从而出现一些关键词没有搜索引擎
UPDATE t_customer_keyword ck, t_qz_setting qs
SET ck.fSearchEngine = qs.fSearchEngine
WHERE ck.fQZSettingUuid = qs.fUuid
AND ck.fCustomerUuid = qs.fCustomerUuid
AND ck.fSearchngine = '';
