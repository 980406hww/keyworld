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
	MachineGroupName(9),
	OptimizeGroupName(10),
	BearPawNumber(11),
	Title(12),
	OrderNumber(13),
	Remarks(14),
	KeywordEffect(15);
	private int columnIndex;
	
	private SuperUserSimpleKeywordDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
