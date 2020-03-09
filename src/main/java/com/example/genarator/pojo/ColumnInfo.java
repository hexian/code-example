package com.example.genarator.pojo;

/**
 * 
 * @author hexian
 * 记录数据库字段信息
 */
public class ColumnInfo {
	
	/**
	 * 表名称
	 */
	private String tbName;

	/**
	 * 列名称
	 */
	private String columnName;
	
	/**
	 * 列描述
	 */
	private String columnRemarks;
	
	/**
	 * jdbc 类型名称
	 */
	private String jdbcTypeName;
	
	/**
	 * java类型短名称
	 */
	private String javaTypeShortName;
	
	/**
	 * java类型全名称
	 */
	private String javaTypeFullName;
	
	/**
	 * 数据库长度
	 */
	private int length;
	
	/**
	 * 小数位数
	 */
	private int scale;
	
	/**
	 * 是否允许为空(true 表示允许为空, flase表示不允许)
	 */
	private boolean nullable = true;
	
	/**
	 * 表示当前字段是否是主键字段
	 */
	private boolean isPrimaryKey = false;

	public ColumnInfo(String tbName, String columnName) {
		this.tbName = tbName;
		this.columnName = columnName;
	}
	
	public ColumnInfo() {
		
	}
	
	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnRemarks() {
		return columnRemarks;
	}

	public void setColumnRemarks(String columnRemarks) {
		this.columnRemarks = columnRemarks;
	}

	public String getJdbcTypeName() {
		return jdbcTypeName;
	}

	public void setJdbcTypeName(String jdbcTypeName) {
		this.jdbcTypeName = jdbcTypeName;
	}

	public String getJavaTypeShortName() {
		return javaTypeShortName;
	}

	public void setJavaTypeShortName(String javaTypeShortName) {
		this.javaTypeShortName = javaTypeShortName;
	}

	public String getJavaTypeFullName() {
		return javaTypeFullName;
	}

	public void setJavaTypeFullName(String javaTypeFullName) {
		this.javaTypeFullName = javaTypeFullName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
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
		ColumnInfo other = (ColumnInfo) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (tbName == null) {
			if (other.tbName != null)
				return false;
		} else if (!tbName.equals(other.tbName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ColumnInfo [tbName=" + tbName + ", columnName=" + columnName + ", columnRemarks=" + columnRemarks
				+ ", jdbcTypeName=" + jdbcTypeName + ", javaTypeShortName=" + javaTypeShortName + ", javaTypeFullName="
				+ javaTypeFullName + ", length=" + length + ", scale=" + scale + ", nullable=" + nullable
				+ ", isPrimaryKey=" + isPrimaryKey + "]";
	}

	
	
	
}
