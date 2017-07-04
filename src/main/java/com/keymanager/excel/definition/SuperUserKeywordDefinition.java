package com.keymanager.excel.definition;

public enum SuperUserKeywordDefinition {
	Keyword(0),
	URL(1),
	SearchEngine(2),
	ServiceProvider(3),
	StartOptimization(4),
	CollectMethod(5),
	FirstFee(6),
	SecondFee(7),
	ThirdFee(8),
	FirstCost(9),
	SecondCost(10),
	ThirdCost(11),
	ForthFee(12),
	FifthFee(13),
	ForthCost(14),
	FifthCost(15),
	Remarks(16)
	;
	private int columnIndex;
	
	private SuperUserKeywordDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
