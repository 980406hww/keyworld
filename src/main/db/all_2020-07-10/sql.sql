alter table t_pt_customer_keyword
    add fKeyword varchar(200) null comment '关键词' after fUuid;

alter table t_pt_customer_keyword
    add fUrl varchar(255) null comment '链接' after fKeyword;

alter table t_qz_customer_keyword
    add fKeyword varchar(200) null comment '关键词' after fUuid;

alter table t_qz_customer_keyword
    add fUrl varchar(255) null comment '链接' after fKeyword;