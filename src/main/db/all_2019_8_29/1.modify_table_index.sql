
CREATE INDEX searchEngine_index ON `db_keyword`.`t_qz_setting`(`fSearchEngine`);

ALTER TABLE `db_keyword`.`t_qz_keyword_rank_info` DROP INDEX terminalType_increase_index;

CREATE INDEX fQZSettingUuid ON `db_keyword`.`t_qz_keyword_rank_info`(`fQZSettingUuid`, `fTerminalType`, `fIncrease`, `fOneWeekDifference`);