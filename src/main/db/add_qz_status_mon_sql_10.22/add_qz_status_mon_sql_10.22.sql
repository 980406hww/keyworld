create table t_qz_charge_mon
(
    fUuid int auto_increment,
    fOperationDate TIMESTAMP default current_timestamp not null comment '操作时间',
    fOperationType int(2) not null comment '操作类型：2首次收费,0暂停,1续费,3下架',
    fOperationObj varchar(100) not null comment '操作对象',
    fOperationAmount varchar(100) null comment '操作金额',
    fSearchEngine varchar(150) not null comment '全站对应搜索引擎',
    constraint t_qz_charge_mon_pk
        primary key (fUuid)
)
comment '收费状态记录表';