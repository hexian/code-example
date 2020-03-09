package com.example.genarator.config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author hexian
 * @description 
 */
public class CodeConfig { 
	
	/** 
	 * 代码生成的包的基本路径
	 */
	private String basePackage;
	
	/**
	 * 指定文件生成的基路径
	 */
	private String genPath;
	
	/**
	 * 作者(默认登录当前操作系统的用户名)
	 */
	private String author = System.getProperty("user.name");
	
	/**
	 * 逻辑删除列
	 */
	private String logicColumn = "";
	
	/**
	 * 表示被删除的值
	 */
	private String logicDeleteValue ="1";
	
	/**
	 * 表示没有被删除的值
	 */
	private String logicNotDeleteValue ="0";
	
	/**
	 * 生成 mapper.xml 的模板路径
	 */
	private String mapperTemplatePath = "";
	
	/**
	 * 最终生成的 xxxMapper.xml 的保存位置
	 */
	private String xmlPath = "";
	
	/**
	 * java代码的保存位置
	 */
	private String javaPath = "";
	
	/**
	 * 表的前缀
	 */
	private String[] tablePrefix;
	
	/**
	 * 列的前缀
	 */
	private String [] fieldPrefix;
	
	/**
	 * 是否大写命名: 全局大写命名 ORACLE 注意
	 */
	private boolean isCapitalMode = false;

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getGenPath() {
		return genPath;
	}

	public void setGenPath(String genPath) {
		this.genPath = genPath;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public String getMapperTemplatePath() {
		return mapperTemplatePath;
	}

	public void setMapperTemplatePath(String mapperTemplatePath) {
		this.mapperTemplatePath = mapperTemplatePath;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getJavaPath() {
		return javaPath;
	}

	public void setJavaPath(String javaPath) {
		this.javaPath = javaPath;
	}

	public String[] getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String... tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public String[] getFieldPrefix() {
		return fieldPrefix;
	}

	public void setFieldPrefix(String... fieldPrefix) {
		this.fieldPrefix = fieldPrefix;
	}

	public boolean isCapitalMode() {
		return isCapitalMode;
	}

	public void setCapitalMode(boolean isCapitalMode) {
		this.isCapitalMode = isCapitalMode;
	}

	/**
	 * 创建时间
	 * @return
	 */
	public String getCreateDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS E").format(new Date());
	}

	
	
}
