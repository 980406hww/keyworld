package com.keymanager.monitoring.excel.definition;

public enum SuperUserSimpleKeywordDefinition {
	Keyword(0),
	SearchEngine(1),
	KeywordEffect(2),
	Sequnce(3),
	IndexCount(4),
	OptimizePlanCount(5),
	OriginalURL(6),
	CollectMethod(7),
	Fee(8),
	URL(9),
	OptimizeGroupName(10),
	BearPawNumber(11),
	Title(12),
	OrderNumber(13),
	Remarks(14);
	private int columnIndex;
	
	private SuperUserSimpleKeywordDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
