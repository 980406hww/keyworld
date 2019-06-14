package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.FriendlyLinkCriteria;
import com.keymanager.monitoring.entity.FriendlyLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FriendlyLinkDao extends BaseMapper<FriendlyLink> {

    List<FriendlyLink> searchFriendlyLinkListsPage(Page<FriendlyLink> page, @Param("friendlyLinkCriteria") FriendlyLinkCriteria friendlyLinkCriteria);

    FriendlyLink getFriendlyLink(@Param("uuid") Long uuid);

    int selectMaxSortRank(@Param("websiteUuid") int websiteUuid);

    void retreatSortRank(@Param("websiteUuid") int websiteUuid, @Param("friendlyLinkSortRank") int friendlyLinkSortRank);

    void updateCentreSortRank(@Param("beginSortRank") int beginSortRank, @Param("endSortRank") int endSortRank, @Param("websiteUuid") int websiteUuid);

    List<String> searchFriendlyLinkids(@Param("websiteUuid") Long websiteUuid, @Param("uuids") List<String> uuids);

    List<Map> searchOriginalSortRank(@Param("websiteUuid") int websiteUuid, @Param("originalFriendlyLinkUrl") String originalFriendlyLinkUrl);

    List<String> searchFriendlyLinkidsByUrl(@Param("websiteUuid") Long websiteUuid, @Param("originalFriendlyLinkUrl") String originalFriendlyLinkUrl);

    void batchDeleteFriendlyLinkByUrl(@Param("friendlyLinkUrl") String friendlyLinkUrl,  @Param("websiteUuids") List<String> websiteUuids);

    FriendlyLink getFriendlyLinkByUrl(@Param("websiteUuid") int websiteUuid, @Param("friendlyLinkUrl") String friendlyLinkUrl);

    List<Integer> selectByWebsiteId(@Param("websiteUuid") Long websiteUuid);

    Long selectIdByFriendlyLinkId(@Param("websiteUuid") Long websiteUuid, @Param("friendlyLinkId") int friendlyLinkId);
}
