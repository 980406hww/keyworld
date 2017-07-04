package com.keymanager.excel.definition;

public enum SuperUserFullKeywordDefinition {
	Keyword(0),
	Sequnce(1),
	IndexCount(2),
	OptimizePlanCount(3),
	CollectMethod(4),
	OriginalURL(5),
	URL(6),
	PositionFirstFee(7),
	PositionSecondFee(8),
	PositionThirdFee(9),
	PositionForthFee(10),
	PositionFifthFee(11),
	PositionFirstPageFee(12),
	OptimizeGroupName(13),
	Title(14),
	Remarks(15);
	private int columnIndex;
	
	private SuperUserFullKeywordDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
