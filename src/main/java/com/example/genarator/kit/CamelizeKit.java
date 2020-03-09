package com.example.genarator.kit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author hexian
 * 下划线与驼峰命名互转
 */
public class CamelizeKit {
	
	/**
	 * 下划线转驼峰
	 * @param underLineExpr 下划线表达式
	 * @return 驼峰表达式
	 */
	public static String underLine2Camelize(String underLineExpr) {
		Pattern linePattern = Pattern.compile("_(\\w)");
		underLineExpr = underLineExpr.toLowerCase();
		Matcher matcher = linePattern.matcher(underLineExpr);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()) {
			String upperCase = matcher.group(1).toUpperCase();
			matcher.appendReplacement(buffer, upperCase);
		}
		matcher.appendTail(buffer);
		String camelizeExpr = buffer.toString();
		
		return camelizeExpr;
	}
	
	/**
	 * 驼峰转下划线
	 * @param camelizeExpr  驼峰表达式
	 * @return 下划线表达式
	 */
	public static String camelize2UnderLine(String camelizeExpr) {
		Pattern humpPattern = Pattern.compile("[A-Z]");
		Matcher matcher = humpPattern.matcher(camelizeExpr);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()) {
			String lowerCase = matcher.group(0).toLowerCase();
			matcher.appendReplacement(buffer, "_" + lowerCase);
		}
		matcher.appendTail(buffer);
		String underLineExpr = buffer.toString();
		
		return underLineExpr;
	}

	public static void main(String[] args) {
		String exp = "selected_option";
		
		// 转驼峰
		String camelizeExpr = underLine2Camelize(exp);
		System.out.println(camelizeExpr);
		
		// 转下划线
		String underLineExpr = camelize2UnderLine(camelizeExpr);
		System.out.println(underLineExpr);
	}
	
}