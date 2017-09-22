package com.keymanager.monitoring.excel.definition;

public enum SuperUserSimpleKeywordDefinition {
	Keyword(0),
	Sequnce(1),
	IndexCount(2),
	OptimizePlanCount(3),
	OriginalURL(4),
	CollectMethod(5),
	Fee(6),
	URL(7),
	OptimizeGroupName(8),
	Title(9),
	OrderNumber(10),
	Remarks(11);
	private int columnIndex;
	
	private SuperUserSimpleKeywordDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
