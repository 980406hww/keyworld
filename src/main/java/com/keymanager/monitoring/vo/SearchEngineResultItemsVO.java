package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.criteria.BaseCriteria;

import java.util.List;

public class SearchEngineResultItemsVO extends BaseCriteria {

    private List<SearchEngineResultItemVO> searchEngineResultItemVOs;

    public List<SearchEngineResultItemVO> getSearchEngineResultItemVOs() {
        return searchEngineResultItemVOs;
    }

    public void setSearchEngineResultItemVOs(List<SearchEngineResultItemVO> searchEngineResultItemVOs) {
        this.searchEngineResultItemVOs = searchEngineResultItemVOs;
    }

}
