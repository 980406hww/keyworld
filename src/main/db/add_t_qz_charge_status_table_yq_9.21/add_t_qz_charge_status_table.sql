create table t_qz_charge_status
(
	fUuid int(11) auto_increment comment '主键ID',
	fQZSettingUuid int(11) not null comment '全站对应表',
	fChargeStatus int(2) not null comment '收费状态 1：续费中 0：暂停续费 2：首次收费 3：下架 4：其他',
	fChargeMoney decimal default 0 null comment '收费金额',
	fCustomerSatisfaction int(2) default 5 null comment '客户满意度 5：特别满意 4：满意 3：中等合适 2：不满意 1：特别不满意',
	fChargeStatusMsg text null comment '状态备注',
	fCreateTime timestamp default current_timestamp null comment '创建时间',
	fLoginName varchar(64) null comment '创建人',
	constraint t_qz_charge_status_pk
		primary key (fUuid)
)
comment '收费状态表';

alter table t_qz_setting
	add fChargeStatusUuid int null comment '最新一次收费状态Uuid';