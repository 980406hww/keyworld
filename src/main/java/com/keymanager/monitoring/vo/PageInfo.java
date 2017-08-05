package com.keymanager.monitoring.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj01 on 2017/8/4.
 */
public class PageInfo<E> {
  private int currentpage;//当前页
  private int displaysRecords;//每页显示记录数
  private int totalSize;
  private List<E>  content;
  private Map<String,Object> searchCondition;//搜索条件

  public int getTotalPage(){//获取中页数
    return (totalSize-1)/displaysRecords+1;
  }

  public int getCurrentpage() {
    return currentpage;
  }

  public void setCurrentpage(int currentpage) {
    this.currentpage = currentpage;
  }

  public int getDisplaysRecords() {
    return displaysRecords;
  }

  public void setDisplaysRecords(int displaysRecords) {
    this.displaysRecords = displaysRecords;
  }

  public int getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(int totalSize) {
    this.totalSize = totalSize;
  }

  public List<E> getContent() {
    return content;
  }

  public void setContent(List<E> content) {
    this.content = content;
  }

  public Map<String, Object> getSearchCondition() {
    return searchCondition;
  }

  public void setSearchCondition(Map<String, Object> searchCondition) {
    this.searchCondition = searchCondition;
  }

  public PageInfo(int currentpage, int displaysRecords, int totalSize, List<E> content,
      Map<String, Object> searchCondition) {
    this.currentpage = currentpage;
    this.displaysRecords = displaysRecords;
    this.totalSize = totalSize;
    this.content = content;
    this.searchCondition = searchCondition;
  }

  public PageInfo() {
  }

  @Override
  public String toString() {
    return "PageInfo{" +
        "currentpage=" + currentpage +
        ", displaysRecords=" + displaysRecords +
        ", totalSize=" + totalSize +
        '}';
  }
}
