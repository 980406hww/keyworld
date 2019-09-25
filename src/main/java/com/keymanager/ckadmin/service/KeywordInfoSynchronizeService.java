package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.vo.KeywordInfoVO;
import java.util.Map;

public interface KeywordInfoSynchronizeService {
    KeywordInfoVO getKeywordList(String webPath, Map map) throws Exception;
    Boolean deleteKeywordList(String webPath, Map map) throws Exception;
    String getUserReportInfo(String webPath, Map map) throws Exception;
}
