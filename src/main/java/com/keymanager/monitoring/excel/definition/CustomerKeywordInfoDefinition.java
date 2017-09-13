package com.keymanager.monitoring.excel.definition;

public enum CustomerKeywordInfoDefinition {
	Keyword(0),
	URL(1),
	Title(2),
	OriginalPosition(3),
	CurrentPosition(4),
	PlannedOptimizeCount(5),
	OptimizedCount(6),
	Price1(7),
	Price2(8),
	Price3(9),
	Price4(10),
	Price5(11),
	Price10(12),
	Index(13),
	Remarks(14);
	
	private int columnIndex;
	
	private CustomerKeywordInfoDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
