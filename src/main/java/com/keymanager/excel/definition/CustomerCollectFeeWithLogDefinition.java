package com.keymanager.excel.definition;

public enum CustomerCollectFeeWithLogDefinition {
	Uuid(0),
	ContactPerson(1),
	Remarks(2),
	Date(3),
	Keyword(4),
	OriginalURL(5),
	OriginalPhoneURL(6),
	CollectMethod(7),
	PcPosition(8),
	PcCurrentPosition(9),
	PcPositionLog(10),
	ChupingPosition(11),
	ChupingCurrentPosition(12),
	ChupingPositionLog(13),
	JisuPosition(14),
	JisuCurrentPosition(15),
	JisuPositionLog(16),
	Fee(17),
	ChupingFee(18),
	JisuFee(19),
	SubTotal(20);
	private int columnIndex;
	
	private CustomerCollectFeeWithLogDefinition(int columnIndex){
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
