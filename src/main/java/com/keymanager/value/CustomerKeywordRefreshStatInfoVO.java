package com.keymanager.value;


public class CustomerKeywordRefreshStatInfoVO {	
	private String group;
	private int totalKeywordCount;
	private int needOptimizeKeywordCount;
	private int invalidKeywordCount;
	private int totalOptimizeCount;
	private int totalOptimizedCount;
	private int needOptimizeCount;
	private int queryCount;
	private int totalMachineCount;
	private int unworkMachineCount;
	private int maxInvalidCount;
	
	public double getInvalidKeywordPercentage(){
		return (this.getTotalKeywordCount() > 0) ? ((this.getInvalidKeywordCount() * 1.0) / this.getTotalKeywordCount()) * 100 : 0;
	}

	public double getInvalidOptimizePercentage(){
		return (this.getQueryCount() > 0) ? (((this.getQueryCount() - this.getTotalOptimizedCount()) * 1.0) / this.getQueryCount()) * 100 : 0;
	}
	
	public String getInvalidKeywordCountStr(){
		if(this.getInvalidKeywordCount() > 0){
			String url = "/customerkeyword/keywordfinder.jsp?invalidRefreshCount=" + this.getMaxInvalidCount() + ("总计".equals(getGroup()) ?  "" : "&optimizeGroupName=" + this.getGroup());
			return String.format("<a href='%s' target='_blank'>%d</a>", url, getInvalidKeywordCount());
		}else{
			return "";
		}
	}
	
	public String getResetInvalidRefreshCountStr(String groupName, String customerName){
		if(getInvalidKeywordCount() > 0){
			return String.format("<a href=\"javascript:resetInvaidRefreshCount('%s', '%s', this)\">重置</a>", 
					"总计".equals(getGroup()) ?  (groupName == null ? "" : groupName) : getGroup(), customerName == null ? "" : customerName);
		}else{
			return "";
		}
	}
	
	public String getUnworkMachineCountStr(){
		if(this.getUnworkMachineCount() > 0){
			String url = "/client/clientlist.jsp?prefix=1&hasProblem=hasProblem" + ("总计".equals(getGroup()) ?  "" : "&groupName=" + this.getGroup());
			return String.format("<a href='%s' target='_blank'>%d</a>", url, getUnworkMachineCount());
		}else{
			return "";
		}
	}
	
	public int getTotalKeywordCount() {
		return totalKeywordCount;
	}

	public void setTotalKeywordCount(int totalKeywordCount) {
		this.totalKeywordCount = totalKeywordCount;
	}

	public int getNeedOptimizeKeywordCount() {
		return needOptimizeKeywordCount;
	}

	public void setNeedOptimizeKeywordCount(int needOptimizeKeywordCount) {
		this.needOptimizeKeywordCount = needOptimizeKeywordCount;
	}

	public int getInvalidKeywordCount() {
		return invalidKeywordCount;
	}

	public void setInvalidKeywordCount(int invalidKeywordCount) {
		this.invalidKeywordCount = invalidKeywordCount;
	}

	public int getTotalOptimizeCount() {
		return totalOptimizeCount;
	}

	public void setTotalOptimizeCount(int totalOptimizeCount) {
		this.totalOptimizeCount = totalOptimizeCount;
	}

	public int getTotalOptimizedCount() {
		return totalOptimizedCount;
	}

	public void setTotalOptimizedCount(int totalOptimizedCount) {
		this.totalOptimizedCount = totalOptimizedCount;
	}

	public int getNeedOptimizeCount() {
		return needOptimizeCount;
	}

	public void setNeedOptimizeCount(int needOptimizeCount) {
		this.needOptimizeCount = needOptimizeCount;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public int getTotalMachineCount() {
		return totalMachineCount;
	}

	public void setTotalMachineCount(int totalMachineCount) {
		this.totalMachineCount = totalMachineCount;
	}

	public int getUnworkMachineCount() {
		return unworkMachineCount;
	}

	public void setUnworkMachineCount(int unworkMachineCount) {
		this.unworkMachineCount = unworkMachineCount;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getMaxInvalidCount() {
		return maxInvalidCount;
	}

	public void setMaxInvalidCount(int maxInvalidCount) {
		this.maxInvalidCount = maxInvalidCount;
	}
}