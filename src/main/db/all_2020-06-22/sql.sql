# 处理机器分组统计缺失terminalType,type 的问题
update t_machine_group_work_info mi,
    (
        SELECT ck.fMachineGroup,ck.fType,ck.fTerminalType
        FROM t_customer_keyword ck, t_group g
        WHERE ck.fOptimizeGroupName = g.fGroupName
          AND ck.fStatus = 1
        GROUP BY ck.fMachineGroup, ck.fType, ck.fTerminalType
        ORDER BY ck.fMachineGroup
    ) a
set mi.fType = a.fType, mi.fTerminalType = a.fTerminalType
WHERE mi.fMachineGroup = a.fMachineGroup;