package com.keymanager.monitoring.excel.definition;

public enum SuperUserSimpleKeywordDefinition {
	Keyword(0),
	SearchEngine(1),
	Sequnce(2),
	IndexCount(3),
	OptimizePlanCount(4),
	OriginalURL(5),
	CollectMethod(6),
	Fee(7),
	URL(8),
	OptimizeGroupName(9),
	BearPawNumber(10),
	Title(11),
	OrderNumber(12),
	Remarks(13);
	private int columnIndex;
	
	private SuperUserSimpleKeywordDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
