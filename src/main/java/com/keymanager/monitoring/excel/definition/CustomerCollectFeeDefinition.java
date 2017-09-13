package com.keymanager.monitoring.excel.definition;

public enum CustomerCollectFeeDefinition {
	Uuid(0),
	ContactPerson(1),
	Remarks(2),
	Date(3),
	Keyword(4),
	OriginalURL(5),
	OriginalPhoneURL(6),
	CollectMethod(7),
	PcPosition(8),
	ChupingPosition(9),
	JisuPosition(10),
	Fee(11),
	ChupingFee(12),
	JisuFee(13),
	SubTotal(14);
	private int columnIndex;
	
	private CustomerCollectFeeDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
