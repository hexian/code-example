package com.example.genarator.support;

import java.util.Random;

import com.example.genarator.kit.OtherKit;

public interface CodeGenServer {

	/**
	 * 生成代码-所有表
	 */
	public void generatorCode();
	
	/**
	 * 生成代码-只包含指定表,多个表之间用逗号分隔
	 */
	public void generatorCodeIncludeTable(String tables);
	
	/**
	 * 生成代码-排除指定表,多个表之间用逗号分隔
	 */
	public void generatorCodeExcludeTable(String tables);
	
	/**
	 * 去掉列的前缀
	 */
	public default String trimFieldPrefix(String columnName, String[] trimColumnPrefix) {
		return OtherKit.trimFieldPrefix(columnName, trimColumnPrefix);
	}
	
	public default String getSerialVersionUID() {
		String serialVersionUID = (new Random().nextInt() %2 == 0 ? "" : "-") + (new Random().nextInt(6)+1) + (new Random().nextInt(8)+1) + (2468267600614678L + new Random().nextInt()) + "L";
		
		return serialVersionUID;
	}
	
}
