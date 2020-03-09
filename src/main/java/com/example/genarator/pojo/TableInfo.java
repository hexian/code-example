package com.example.genarator.pojo;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author hexian
 * 表信息
 */
public class TableInfo {  

	/**
	 * 表名称
	 */
	private String tbName;
	
	/**
	 * 是否是逻辑删除表
	 */
	private boolean isLogicDelete = false;
	
	/**
	 * 逻辑删除列
	 */
	private String logicColumn = "";
	
	/**
	 * 表示被删除的值
	 */
	private String logicDeleteValue ="0";
	
	/**
	 * 表示没有被删除的值
	 */
	private String logicNotDeleteValue ="1";
	
	/**
	 * 表描述
	 */
	private String tbRemarks;
	
	/**
	 * 表主键
	 */
	private List<ColumnInfo> primaryKeys = new ArrayList<ColumnInfo>();
	
	/**
	 * 保存表所有列
	 */
	private List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();

	public TableInfo() {
	}
	
	public TableInfo(String tbName) {
		this.tbName = tbName;
	}

	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public String getTbRemarks() {
		return tbRemarks;
	}

	public void setTbRemarks(String tbRemarks) {
		this.tbRemarks = tbRemarks;
	}

	public boolean isLogicDelete() {
		return isLogicDelete;
	}

	public void setLogicDelete(boolean isLogicDelete) {
		this.isLogicDelete = isLogicDelete;
	}

	public String getLogicColumn() {
		return logicColumn;
	}

	public void setLogicColumn(String logicColumn) {
		this.logicColumn = logicColumn;
	}

	public String getLogicDeleteValue() {
		return logicDeleteValue;
	}

	public void setLogicDeleteValue(String logicDeleteValue) {
		this.logicDeleteValue = logicDeleteValue;
	}

	public String getLogicNotDeleteValue() {
		return logicNotDeleteValue;
	}

	public void setLogicNotDeleteValue(String logicNotDeleteValue) {
		this.logicNotDeleteValue = logicNotDeleteValue;
	}

	public List<ColumnInfo> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<ColumnInfo> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public List<ColumnInfo> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnInfo> columnList) {
		this.columnList = columnList;
	}

	public ColumnInfo findLogicColumnInfo() {
		if(isLogicDelete) {
			ColumnInfo columnInfo = new ColumnInfo(this.tbName, this.logicColumn);
			int pos = columnList.indexOf(columnInfo);
			if(-1 != pos) {
				ColumnInfo oldColInfo = columnList.get(pos);
				return oldColInfo;
			}
		}
		
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tbName == null) ? 0 : tbName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableInfo other = (TableInfo) obj;
		if (tbName == null) {
			if (other.tbName != null)
				return false;
		} else if (!tbName.equals(other.tbName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TableInfo [tbName=" + tbName + ", isLogicDelete=" + isLogicDelete + ", logicColumn=" + logicColumn
				+ ", logicDeleteValue=" + logicDeleteValue + ", logicNotDeleteValue=" + logicNotDeleteValue
				+ ", tbRemarks=" + tbRemarks + ", primaryKeys=" + primaryKeys + ", columnList=" + columnList + "]";
	}

	
	
	
	
}
