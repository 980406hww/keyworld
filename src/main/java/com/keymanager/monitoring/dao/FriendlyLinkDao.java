package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.FriendlyLinkCriteria;
import com.keymanager.monitoring.entity.FriendlyLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendlyLinkDao extends BaseMapper<FriendlyLink> {

    List<FriendlyLink> searchFriendlyLinkListsPage(Page<FriendlyLink> page, @Param("friendlyLinkCriteria") FriendlyLinkCriteria friendlyLinkCriteria);

    FriendlyLink getFriendlyLink(@Param("uuid") Long uuid);

    void insertFriendlyLink(@Param("friendlyLink") FriendlyLink friendlyLink);

    void retreatSortRank(@Param("websiteUuid") int websiteUuid, @Param("friendlyLinkSortRank") int friendlyLinkSortRank);

    void updateCentreSortRank(@Param("beginSortRank") int beginSortRank, @Param("endSortRank") int endSortRank, @Param("websiteUuid") int websiteUuid);

    List<String> searchFriendlyLinkids(@Param("websiteUuid") Long websiteUuid, @Param("uuids") List<String> uuids);
}
