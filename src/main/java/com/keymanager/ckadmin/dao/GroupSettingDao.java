package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.GroupSetting;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("groupSettingDao2")
public interface GroupSettingDao extends BaseMapper<GroupSetting> {

    List<GroupSetting> searchGroupSettings (@Param("operationCombineUuid") long operationCombineUuid);

    GroupSetting getGroupSettingByUuid(@Param("uuid") Long uuid);

    List<GroupSetting> searchGroupSettingsSortingPercentage (@Param("operationCombineUuid") long operationCombineUuid);

    void saveGroupSetting (@Param("groupSetting") GroupSetting groupSetting);

    void updateGroupSetting (@Param("gs") GroupSetting gs, @Param("groupSetting") GroupSetting groupSetting);

    List<Long> getGroupSettingUuids (@Param("operationCombineUuid") long operationCombineUuid);

    void deleteGroupSettingByOperationCombineUuid (@Param("operationCombineUuid") long operationCombineUuid);


}
