alter table t_customer drop column fActiveHour;

alter table t_customer drop column fInActiveHour;

alter table t_customer drop column fType;

alter table t_customer modify fStatus tinyint not null;

alter table t_customer drop column fSearchEngine;

drop index NewIndex5 on t_customer;

alter table t_customer drop column fEntryType;