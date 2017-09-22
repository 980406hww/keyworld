package com.keymanager.monitoring.excel.definition;

public enum CustomerKeywordInfoDefinition {
	Keyword(0),
	URL(1),
	OriginalUrl(2),
	Title(3),
	OriginalPosition(4),
	CurrentPosition(5),
	PlannedOptimizeCount(6),
	OptimizedCount(7),
	Price1(8),
	Price2(9),
	Price3(10),
	Price4(11),
	Price5(12),
	Price10(13),
	Index(14),
	Remarks(15);
	
	private int columnIndex;
	
	private CustomerKeywordInfoDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
