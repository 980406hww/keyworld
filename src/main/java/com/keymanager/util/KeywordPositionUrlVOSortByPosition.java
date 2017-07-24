package com.keymanager.util;

import java.util.Comparator;

import com.keymanager.value.KeywordPositionUrlVO;

public class KeywordPositionUrlVOSortByPosition implements Comparator<KeywordPositionUrlVO> {
	public int compare(KeywordPositionUrlVO o1, KeywordPositionUrlVO o2) {
		if (o1.getPosition() > o2.getPosition())
			return 1;
		return 0;
	}
}
