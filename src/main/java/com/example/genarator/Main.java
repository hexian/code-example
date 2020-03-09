package com.example.genarator;

import com.example.genarator.config.CodeConfig;
import com.example.genarator.config.DBConfig;
import com.example.genarator.support.CodeGenServer;
import com.example.genarator.support.jpa.JpaGenServer;
import com.example.genarator.support.mybaitsplus.MyBatisPlusGenServer;
import com.example.genarator.support.mybatis.MyBatisGenServer;

/**
 * 
 * @author hexian
 */
public class Main {

	public static void main(String[] args) {
		// 数据库配置
		DBConfig dbConfig = new DBConfig();
		// MySQL5
		//dbConfig.setDirverClass("com.mysql.jdbc.Driver");
		// MySQL8
		dbConfig.setDirverClass("com.mysql.cj.jdbc.Driver");
		dbConfig.setUrl("jdbc:mysql://127.0.0.1:3306/aaaaaa");
		dbConfig.setUsername("xxxxxx");
		//dbConfig.setSchema("xxxxxx");
		dbConfig.setPassword("xxxxxx");
		
		// 代码配置
		CodeConfig codeConfig = new CodeConfig();
		// 设置代码作者 
		codeConfig.setAuthor("Adminisrator");
		// 设置逻辑删除列
		codeConfig.setLogicColumn("is_removed");
		// 逻辑删除列值为1表示已被删除(不需要考虑是 VARCHAR 还是 INTEGER 类型, 自动判断)
		codeConfig.setLogicDeleteValue("1");
		// 逻辑删除列值为0表示未被删除(不需要考虑是 VARCHAR 还是 INTEGER 类型, 自动判断)
		codeConfig.setLogicNotDeleteValue("0");
		// 设置基础包名称(Controller、Service、Repository、Entity 名称生成策略)
		codeConfig.setBasePackage("com.exmaple");
		codeConfig.setTablePrefix("t_", "ca_", "sfs");
		codeConfig.setFieldPrefix("qq_");
		
		// 目前受限jdbc驱动实现, 在mysql中无法提取到数据表的注释信息(参考:  com\example\genarator\metadata\mysql.sql)
		CodeGenServer server = null;
		//server = new MyBatisGenServer(dbConfig, codeConfig);
		server = new MyBatisPlusGenServer(dbConfig, codeConfig);
		//server = new JpaGenServer(dbConfig, codeConfig);
		
		if(server!=null) {
			// 所有表生成代码
			//server.generatorCode();
			// 只生成那些表(多个表使用英文逗号分隔)
			server.generatorCodeIncludeTable("t_aaa_ttttt");// 
			// 排除那些表(多个表使用英文逗号分隔)
			//server.generatorCodeIncludeTable("t_aaa_ttttt");
		}
	}

}
