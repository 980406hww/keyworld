drop index customerUuid_terminalType_index on t_user_notebook;

create index index_one
	on t_user_notebook (fQzUuid, fTerminalType);