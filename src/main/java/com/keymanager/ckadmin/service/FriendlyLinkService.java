package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.FriendlyLinkCriteria;
import com.keymanager.ckadmin.entity.FriendlyLink;
import com.keymanager.ckadmin.vo.FriendlyLinkVO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface FriendlyLinkService extends IService<FriendlyLink> {

    ModelAndView constructSearchFriendlyLinkListsModelAndView(int currentPageNumber, int pageSize, FriendlyLinkCriteria friendlyLinkCriteria);

    Page<FriendlyLink> searchFriendlyLinkList(Page<FriendlyLink> page, FriendlyLinkCriteria friendlyLinkCriteria);

    void saveFriendlyLink(MultipartFile file, FriendlyLink friendlyLink);

    FriendlyLink getFriendlyLink(Long uuid);

    void delFriendlyLinks(Map map);

    void delFriendlyLink(Long uuid);

    void updateFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, int originalSortRank);

    void saveOrUpdateConnectionCMS(FriendlyLink friendlyLink, MultipartFile file, String type);

    void deleteConnectionCMS(Long websiteUuid, String[] uuids);

    List<FriendlyLinkVO> selectConnectionCMS(Long websiteUuid);

    String connectionCMS(Map map, String type, String backendDomain);

    FriendlyLink initFriendlyLink(HttpServletRequest request) throws Exception;

    List<Map> searchFriendlyLinkTypeList(Long websiteUuid);

    void insertFriendlyLink(FriendlyLink friendlyLink);

    void retreatSortRank(int websiteUuid, int friendlyLinkSortRank);

    void updateFriendlyLinkById(FriendlyLink friendlyLink);

    void updateCentreSortRank(int beginSortRank, int endSortRank, int websiteUuid);

    List<Map> searchOriginalSortRank(int websiteUuid, String originalFriendlyLinkUrl);

    List<String> searchFriendlyLinkidsByUrl(Long websiteUuid, String originalFriendlyLinkUrl);

    void batchDeleteFriendlyLinkByUrl(String friendlyLinkUrl, List<String> websiteUuids);

    FriendlyLink getFriendlyLinkByUrl(int websiteUuid, String friendlyLinkUrl);

    List<Integer> selectByWebsiteId(Long websiteUuid);

    void initSynchronousFriendlyLink(FriendlyLink friendlyLink, FriendlyLinkVO friendlyLinkVO);

    Long selectIdByFriendlyLinkId(Long websiteUuid, int friendlyLinkId);

    int searchFriendlyLinkCount(Long websiteUuid);

    void pushFriendlyLink(Map map);

    int selectMaxSortRank(int websiteUuid);
}
