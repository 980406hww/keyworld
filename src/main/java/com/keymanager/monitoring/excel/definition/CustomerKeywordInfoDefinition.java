package com.keymanager.monitoring.excel.definition;

public enum CustomerKeywordInfoDefinition {
	Keyword(0),
	URL(1),
	OriginalUrl(2),
	Title(3),
	BearPawNumber(4),
	OriginalPosition(5),
	CurrentPosition(6),
	PlannedOptimizeCount(7),
	OptimizedCount(8),
	Price1(9),
	Price2(10),
	Price3(11),
	Price4(12),
	Price5(13),
	Price10(14),
	Index(15),
	Remarks(16);
	
	private int columnIndex;
	
	private CustomerKeywordInfoDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
