package com.keymanager.monitoring.criteria;


import com.keymanager.monitoring.entity.NegativeList;

import java.util.List;

public class KeywordNegativeCriteria extends BaseCriteria{

	private NegativeList negativeList;

	private List<NegativeList> negativeLists;

	private Boolean isNegative;

	public NegativeList getNegativeList() {
		return negativeList;
	}

	public void setNegativeList(NegativeList negativeList) {
		this.negativeList = negativeList;
	}

	public List<NegativeList> getNegativeLists() {
		return negativeLists;
	}

	public void setNegativeLists(List<NegativeList> negativeLists) {
		this.negativeLists = negativeLists;
	}

	public Boolean getNegative() {
		return isNegative;
	}

	public void setNegative(Boolean negative) {
		isNegative = negative;
	}
}
