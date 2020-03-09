package com.example.genarator.kit;

public class OtherKit {
	
	private OtherKit() {
		
	}

	public static String trimFieldPrefix(String columnName, String[] trimColumnPrefix) {
		String trimPrefixTableName = columnName;
		if(trimColumnPrefix!=null && trimColumnPrefix.length>0) {
			String upperCaseColumnName = columnName.toUpperCase();
			for(String prefix : trimColumnPrefix) {
				prefix = prefix.toUpperCase();
				if(upperCaseColumnName.startsWith(prefix)) {
				   upperCaseColumnName = upperCaseColumnName.substring(prefix.length());
				}
			}
			
			// 如果是以 _ 开头
			if(upperCaseColumnName.startsWith("_")) {
				upperCaseColumnName = upperCaseColumnName.substring(1);
			}
			// 如果是以 _ 结尾开头
			if(upperCaseColumnName.endsWith("_")) {
				upperCaseColumnName = upperCaseColumnName.substring(0, upperCaseColumnName.length()-1);
			}
			
			trimPrefixTableName = upperCaseColumnName;
		} 
		
		return trimPrefixTableName;
	}
	
}
