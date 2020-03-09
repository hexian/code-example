package com.example.genarator.support.mybaitsplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.genarator.config.CodeConfig;
import com.example.genarator.config.DBConfig;
import com.example.genarator.kit.CamelizeKit;
import com.example.genarator.kit.OtherKit;
import com.example.genarator.metadata.CodeMetaData;
import com.example.genarator.metadata.factory.CodeMetaDataFactory;
import com.example.genarator.metadata.factory.impl.MyBatisPlusCodeMetaDataFactoryImpl;
import com.example.genarator.pojo.ColumnInfo;
import com.example.genarator.pojo.FileInfo;
import com.example.genarator.pojo.TableInfo;
import com.example.genarator.support.CodeGenServer;

/**
 * <pre>
 * @author hexian
 * @description MyBatisPlus 代码生成器(自定义)
 * </pre>
 */ 
public class MyBatisPlusGenServer implements CodeGenServer {

	private DBConfig dbConfig;
	
	private CodeConfig codeConfig;
	
	// 定义每行显示多少个数据库列
	private static final int LINE_SIZE = 5;
	
	private String separator = System.lineSeparator();
	
	public MyBatisPlusGenServer(DBConfig dbConfig, CodeConfig codeConfig) {
		this.dbConfig = dbConfig;
		this.codeConfig = codeConfig;
	}
	
	/**
	 * 生成代码-所有表
	 */
	@Override
	public void generatorCode(){ 
		Connection con = getConnection();
		List<TableInfo> tbInfoList = getAllTableInfo(con);
		table2code(tbInfoList);
	}
	
	/**
	 * 生成代码-排除指定表,多个表之间用逗号分隔
	 */
	@Override
	public void generatorCodeExcludeTable(String tables){ 
		Connection con = getConnection();
		List<TableInfo> tbInfoList = getExcludeTableInfo(con, tables);
		table2code(tbInfoList);
	}
	
	/**
	 * 生成代码-只包含指定表,多个表之间用逗号分隔
	 */
	@Override
	public void generatorCodeIncludeTable(String tables) {
		Connection con = getConnection();
		List<TableInfo> tbInfoList = getIncludeTableInfo(con, tables);
		table2code(tbInfoList);
	}
	
	/**
	 * 从 List<TableInfo> 转换成代码
	 */
	private void table2code(List<TableInfo> tbInfoList) {
		if(tbInfoList!=null && !tbInfoList.isEmpty()) {
			CodeMetaDataFactory facorty = MyBatisPlusCodeMetaDataFactoryImpl.getInstance(codeConfig);
			String basePackage = codeConfig.getBasePackage();
			for(TableInfo tbInfo : tbInfoList) {
				String tbName = tbInfo.getTbName();
				CodeMetaData codeMetaData = facorty.genCodeMetaData(tbName, basePackage);
				if(codeMetaData != null) {
					// 生成实体 
					FileInfo entityFile = generatorEntity(tbInfo, codeMetaData);
					// 生成 mapper 的 java 类
					FileInfo mapperJavaFile = generatorMapperJava(tbInfo, codeMetaData);
					// 生成 mapper 的 xml 文件
					FileInfo mapperXmlFile = generatorMapperXML(tbInfo, codeMetaData);
					// 生成 service 接口的 java 文件
					FileInfo serviceJavaFile = generatorService(tbInfo, codeMetaData);
					// 生成 service 接口实现类的 java 文件
					FileInfo serviceJavaImplFile = generatorServiceImpl(tbInfo, codeMetaData);
					// 生成 controller 的 java 类
					FileInfo controllerJavaFile = generatorController(tbInfo, codeMetaData);
					
					writer(entityFile);
					writer(mapperJavaFile);
					writer(mapperXmlFile);
					writer(serviceJavaFile);
					writer(serviceJavaImplFile);
					writer(controllerJavaFile);
				}
			}
		}
	}
	
	/**
	 * 写入文件
	 */
	private void writer(FileInfo fileInfo) {
		// 不是空文件
		if(fileInfo!=null && !fileInfo.isEmptyFile()){
			// 文件内容
			String content = fileInfo.getContent();
			// 包名称
			String packageName = fileInfo.getPackageName();
			// 文件类型 : java、xml
			String fileType = fileInfo.getFileType();
			// 文件名称
			String fileName = fileInfo.getFileName();
			// 表名称
			String tbName = fileInfo.getTbName();
			// 是否是 mapper 的 xml 文件
			boolean isMapperFile = "xml".equalsIgnoreCase(fileType);
			
			// 文件基路径
			String genPath = codeConfig.getGenPath();
			if(isEmpty(genPath)) {
			   genPath = System.getProperty("user.dir");
			}
			
			String directory = "";
			if(isMapperFile) {
				String[] tbNames = tbName.split("_");
				// 获取公司名称
				String company = tbNames[0].toLowerCase();
				// 获取模块名称
				String model = tbNames[1].toLowerCase();
				directory = genPath + File.separator+"mapper" + File.separator + company + File.separator + model;
			} else {
				directory = genPath + File.separator+"code"+ File.separator +packageName.replace(".", File.separator);;
			}
			File dir = new File(directory);
			if(!dir.exists()) {
			   dir.mkdirs();
			}
			// 最终的文件名
			fileName = directory + File.separator + fileName + "." + fileType;
			// 删除文件
			File file = new File(fileName);
			if(file.exists()) {
			   file.delete();
			}
			
			try(BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName), true), "UTF-8"))){
				bw.write(content);
				bw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			/**
			BufferedWriter bw = null;
			try {
				// 删除文件
				File file = new File(fileName);
				if(file.exists()) {
				   file.delete();
				}
				// 覆盖内容
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName), true), "UTF-8"));
				bw.write(content);
				bw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(bw!=null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			*/
		}
	}
	
	
	/**
	 * 生成实体 
	 */
	private FileInfo generatorEntity(TableInfo tbInfo, CodeMetaData codeMetaData) {
		FileInfo fileInfo = new FileInfo();
		StringBuilder content = new StringBuilder();
		
		String tbName = tbInfo.getTbName();
		String className = codeMetaData.getEntityName();
		
		// 设置 package
		content.append("package " + codeMetaData.getEntityFullPackage() + ";" + separator + separator);
		String packageName = codeMetaData.getEntityFullPackage()+"";
		
		fileInfo.setPackageName(packageName);
		fileInfo.setFileName(className);
		fileInfo.setFileType("java");
		fileInfo.setTbName(tbName);
		
		content.append("import java.io.Serializable;" + separator);
		content.append("import com.baomidou.mybatisplus.annotation.*;" + separator);
		if (!tbInfo.getColumnList().isEmpty()) {
			// 当前表是否支持逻辑删除
			boolean logicDelete = tbInfo.isLogicDelete();
			String logicColumn = tbInfo.getLogicColumn();
			
			// 保存 import
			StringBuilder importSb = new StringBuilder();
			// 保存字段
			StringBuilder fleldSb = new StringBuilder();
			// 保存 getter、setter方法
			StringBuilder getAndSetMethodSb = new StringBuilder();
			// 空构造方法
			StringBuilder noArgsConstructSb = new StringBuilder();
			// 全字段构造方法
			StringBuilder allArgsConstructSb = new StringBuilder();
			// 全字段的赋值
			StringBuilder argsFiledConstructSb = new StringBuilder();
			// toString方法
			StringBuilder toStringSb = new StringBuilder();
			
			// 空构造方法
			noArgsConstructSb.append("    public "+className+"() { "+separator);
			noArgsConstructSb.append("        "+separator);
			noArgsConstructSb.append("    }"+separator+separator);
			
			List<ColumnInfo> primaryKeys = tbInfo.getPrimaryKeys();
			// 生成字段
			List<ColumnInfo> columnList = tbInfo.getColumnList();
			int size = columnList.size();
			if(size>0) {
				// 空构造方法
				allArgsConstructSb.append("    public "+className+"(");
				// toString方法
				toStringSb.append("    @Override"+separator);
				toStringSb.append("    public String toString() {"+separator);
				toStringSb.append("        return \""+className+" [");
				
				Set<String> temp = new HashSet<String>(); 
				for(int idx=0; idx<size; idx++) {
					ColumnInfo columnInfo = columnList.get(idx);
					String columnName = columnInfo.getColumnName();
					String columnRemarks = columnInfo.getColumnRemarks();
					String javaTypeShortName = columnInfo.getJavaTypeShortName();
					String javaTypeFullName = columnInfo.getJavaTypeFullName();
					
					boolean isPrimaryKey = primaryKeys.indexOf(columnInfo)!=-1;
					
					// 生成 import 
					if(!"byte[]".equals(javaTypeFullName)) {
					   if(!temp.contains(javaTypeFullName)) {
						  importSb.append("import "+javaTypeFullName+";"+ separator);
						  temp.add(javaTypeFullName);
					   }
					}
					
					
					
					// 生成字段
					String fieldName = OtherKit.trimFieldPrefix(columnName.toLowerCase(), codeConfig.getFieldPrefix());
					fieldName = CamelizeKit.underLine2Camelize(fieldName);
					// getter/setter方法名称
					String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					fleldSb.append("    /* "+(columnRemarks==null?"":columnRemarks) + (isPrimaryKey?"(主键)":"") + "   数据库列名称:  "+columnName.toUpperCase()+" */ "+ separator);
					
					// 生成注解
					if(logicDelete && columnName.equalsIgnoreCase(logicColumn)) {
						fleldSb.append("    @TableLogic"+separator);
					}
					int position = primaryKeys.indexOf(columnInfo);
					if(-1 == position) {
						fleldSb.append("    @TableField(\""+columnName.toUpperCase()+"\")"+separator);
					} else {
						fleldSb.append("    @TableId(\""+columnName.toUpperCase()+"\")"+separator);
					}
					
					fleldSb.append("    private "+javaTypeShortName+" "+fieldName+";"+ separator+separator);
					
					// 生成 getter、setter方法
					// getter 方法
					getAndSetMethodSb.append("    /** "+(columnRemarks==null?"":columnRemarks)+ (isPrimaryKey?"(主键)":"") +"   数据库列名称:  "+columnName.toUpperCase()+" **/"+separator);
					getAndSetMethodSb.append("    public "+javaTypeShortName + ("BOOLEAN".equals(javaTypeShortName) ? " is" : " get") + methodName +"() { "+separator);
					getAndSetMethodSb.append("        return this."+fieldName+";"+ separator);
					getAndSetMethodSb.append("    }"+ separator+separator);
					
					// setter 方法
					getAndSetMethodSb.append("    /** "+(columnRemarks==null?"":columnRemarks)+ (isPrimaryKey?"(主键)":"") +"   数据库列名称:  "+columnName.toUpperCase()+" **/"+separator);
					getAndSetMethodSb.append("    public void set" + methodName +"("+javaTypeShortName+" "+fieldName+") { "+separator);
					getAndSetMethodSb.append("       this."+fieldName+" = "+fieldName+";"+ separator);
					getAndSetMethodSb.append("    }"+ separator+separator);
					
					// 全字段构造方法
					if(size-1 == idx) {// 最后一个参数
						allArgsConstructSb.append(javaTypeShortName+" "+fieldName+"");
						
						// toString方法
						toStringSb.append(""+fieldName+"=\" +"+" "+fieldName+"+\"]\"; ");
					} else {
						allArgsConstructSb.append(javaTypeShortName+" "+fieldName+", ");
						
						// toString方法
						toStringSb.append(""+fieldName+"=\" +"+" "+fieldName+" + \", ");
					}
					
					argsFiledConstructSb.append("        this."+fieldName+" = "+fieldName+";"+separator);
					
				}
				
				// toString方法
				toStringSb.append(separator+"    }"+separator+separator);
				
				// 全字段构造方法
				allArgsConstructSb.append(") { "+separator);
				allArgsConstructSb.append(argsFiledConstructSb);
				allArgsConstructSb.append("    }"+separator+separator);
			}
			
			// 组合成实体类
			
			
			// import 
			content.append(importSb);
			content.append(separator);
			
			// 生成注释信息
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
			content.append("/**" + separator);
			//sb.append("<pre>" + separator);
			content.append(" * @author: " + (codeConfig.getAuthor()==null?"":codeConfig.getAuthor())+ separator);
			content.append(" * @description: " + (tbInfo.getTbRemarks()==null?"":tbInfo.getTbRemarks()) + separator);
			content.append(" * @createDate: " + sdf.format(new Date()) + separator);
			//sb.append("</pre>" + separator);
			content.append(" */" + separator);
			content.append("@TableName(\""+tbName+"\")" + separator);
			
			// class 
			content.append("public class "+className+" implements Serializable {"+ separator+separator);
			// 序列号
			content.append("    private static final long serialVersionUID = "+getSerialVersionUID()+";" + separator+separator);
			
			// 字段
			content.append(fleldSb+separator);
			
			// 空构造方法
			content.append(noArgsConstructSb);
			// 全字段构造方法
			content.append(allArgsConstructSb);
			// getter、setter方法
			content.append(getAndSetMethodSb);
			// toString方法
			content.append(toStringSb);
			
			content.append("}"+separator);
		}
		
		fileInfo.setContent(content.toString());
		
		return fileInfo;
	}
	
	/**
	 * 生成 mapper 的 java 接口类
	 */
	private FileInfo generatorMapperJava(TableInfo tbInfo, CodeMetaData codeMetaData) {
		FileInfo fileInfo = new FileInfo();
		
		StringBuilder sb = new StringBuilder();
		String tbName = tbInfo.getTbName();
		String className = codeMetaData.getEntityName(); 
		String tbRemarks = tbInfo.getTbRemarks();
		
		String packageName = codeMetaData.getRepositoryFullPackage(); 
		// mapper 类名称
		String mapperClassName = codeMetaData.getRepositoryName(); 
		// 实体类名称
		String entityClassName = codeMetaData.getEntityFullName(); 
		String lowerClassName = className.substring(0,1).toLowerCase()+className.substring(1);
		
		fileInfo.setPackageName(packageName);
		fileInfo.setFileName(mapperClassName);
		fileInfo.setFileType("java");
		fileInfo.setTbName(tbName);
		
		
		// 当前表是否支持逻辑删除
		boolean logicDelete = tbInfo.isLogicDelete();
		String logicColumn = tbInfo.getLogicColumn();
		
		sb.append("package "+packageName+";"+separator+separator);
		sb.append("import "+entityClassName+";"+separator);
		sb.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;"+separator);
		
		StringBuilder bodyBuilder = new StringBuilder();
		StringBuilder impBuilder = new StringBuilder();
		bodyBuilder.append("public interface "+mapperClassName+" extends BaseMapper<"+className+">  {"+separator+separator);
		bodyBuilder.append("    /* "+tbName+"["+tbRemarks+"]-全字段新增方法  */"+separator);
		bodyBuilder.append("    public int insert("+className+" "+lowerClassName+");"+separator+separator);
		bodyBuilder.append("    /* "+tbName+"["+tbRemarks+"]-仅插入非空字段新增方法  */"+separator);
		bodyBuilder.append("    public int insertSelective("+className+" "+lowerClassName+");"+separator+separator);
		
		Set<String> temp = new HashSet<String>();
		List<ColumnInfo> primaryKeys = tbInfo.getPrimaryKeys();
		if(primaryKeys.size()>0) {
			ColumnInfo idInfo = primaryKeys.get(0);
			// 列名称
			String columnName = idInfo.getColumnName();
			// 类型短名称
			String javaTypeShortName = idInfo.getJavaTypeShortName();
			// 类型全名称
			String javaTypeFullName = idInfo.getJavaTypeFullName();
			// 字段名称
			String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
			
			if(!temp.contains(javaTypeFullName)) {
				impBuilder.append("import "+javaTypeFullName+";"+separator);
				
				temp.add(javaTypeFullName);
			}
			
			bodyBuilder.append("    /* "+tbName+"("+tbRemarks+")-根据主键删除方法["+(logicDelete ? "逻辑删除, 逻辑删除列: "+logicColumn : "")+", 主键: "+columnName+"]  */"+separator);
			bodyBuilder.append("    public int deleteByPrimaryKey("+javaTypeShortName+" "+fieldName+");"+separator+separator);
			
			bodyBuilder.append("    /* "+tbName+"("+tbRemarks+")-全字段更新方法[不包括主键: "+columnName+(logicDelete ? ", 逻辑删除列: "+logicColumn : "")+"]  */"+separator);
			bodyBuilder.append("    public int updateByPrimaryKey("+className+" "+lowerClassName+");"+separator+separator);
			
			bodyBuilder.append("    /* "+tbName+"("+tbRemarks+")-非空更新方法[不包括主键: "+columnName+(logicDelete ? ", 逻辑删除列: "+logicColumn : "")+"]  */"+separator);
			bodyBuilder.append("    public int updateByPrimaryKeySelective("+className+" "+lowerClassName+");"+separator+separator);
			
			bodyBuilder.append("    /* "+tbName+"("+tbRemarks+")-根据主键查找单条数据方法[主键: "+columnName+"]  */"+separator);
			bodyBuilder.append("    public "+className+" selectByPrimaryKey("+javaTypeShortName+" "+fieldName+");"+separator+separator);
		}
		if(impBuilder.length()>0) {
		   impBuilder.append(separator);
		}
		bodyBuilder.append("}"+separator);
		
		sb.append(impBuilder);
		
		// 生成注释信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		sb.append("/**" + separator);
		//sb.append("<pre>" + separator);
		sb.append(" * @author: " + (codeConfig.getAuthor()==null?"":codeConfig.getAuthor())+ separator);
		sb.append(" * @description: " + (tbInfo.getTbRemarks()==null?"":tbInfo.getTbRemarks()) + separator);
		sb.append(" * @createDate: " + sdf.format(new Date()) + separator);
		//sb.append("</pre>" + separator);
		sb.append(" */" + separator);
		
		sb.append(bodyBuilder);

		fileInfo.setContent(sb.toString());
		
		return fileInfo;
	}
	
	/**
	 * 生成 mapper 的 xml 文件
	 * @param tbInfo
	 */
	private FileInfo generatorMapperXML(TableInfo tbInfo, CodeMetaData codeMetaData) {
		FileInfo fileInfo = new FileInfo();
		StringBuilder content = new StringBuilder();
		String tbName = tbInfo.getTbName();
		// mapper 类名称
		String mapperClassName = codeMetaData.getRepositoryFullName();
		// 实体类名称
		String entityClassName = codeMetaData.getEntityFullName(); 
		
		fileInfo.setPackageName(codeMetaData.getRepositoryFullPackage());
		fileInfo.setFileName(codeMetaData.getRepositoryName());
		fileInfo.setFileType("xml");
		fileInfo.setTbName(tbName);
		
		// 当前表是否支持逻辑删除
		boolean logicDelete = tbInfo.isLogicDelete();
		String logicColumn = tbInfo.getLogicColumn();
		String logicDeleteValue = tbInfo.getLogicDeleteValue();
		String logicNotDeleteValue = tbInfo.getLogicNotDeleteValue();
		ColumnInfo logicColumnInfo = tbInfo.findLogicColumnInfo();
		
		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+separator);
		content.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">"+separator);
		content.append("<mapper namespace=\""+mapperClassName+"\">"+separator+separator);
		
		// resultMap 构造
		content.append("  <resultMap id=\"BaseResultMap\" type=\""+entityClassName+"\">"+separator);
		// 字段映射 - 主键
		List<ColumnInfo> primaryKeys = tbInfo.getPrimaryKeys();
		for(int idx=0; idx<primaryKeys.size(); idx++) {
			ColumnInfo idInfo = primaryKeys.get(idx);
			// 列名称
			String columnName = idInfo.getColumnName();
			// JDBC 类型名称
			String jdbcTypeName = idInfo.getJdbcTypeName();
			// 字段名称
			String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
			// 列描述
			String columnRemarks = idInfo.getColumnRemarks();
			if(!columnRemarks.isEmpty()) {
			   content.append("    <!-- "+tbName+"."+columnName+": "+columnRemarks+" -->"+separator);
			}
			content.append("    <id column=\""+columnName.toUpperCase()+"\" jdbcType=\""+jdbcTypeName+"\" property=\""+fieldName+"\" />"+separator);
		}
		// 字段映射 - 非主键列
		List<ColumnInfo> columnList = tbInfo.getColumnList();
		for(int idx=0; idx<columnList.size(); idx++) {
			ColumnInfo columnInfo = columnList.get(idx);
			int pos = primaryKeys.indexOf(columnInfo);
			// 非主键
			if(-1 == pos) { 
				// 列名称
				String columnName = columnInfo.getColumnName();
				// JDBC 类型名称
				String jdbcTypeName = columnInfo.getJdbcTypeName();
				// 字段名称
				String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
				fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
				// 列描述
				String columnRemarks = columnInfo.getColumnRemarks();
				if(!isEmpty(columnRemarks)) {
					content.append("    <!-- "+tbName+"."+columnName+": "+columnRemarks+" -->"+separator);
				}
				content.append("    <result column=\""+columnName.toUpperCase()+"\" jdbcType=\""+jdbcTypeName+"\" property=\""+fieldName+"\" />"+separator);
			}
		}
		content.append("  </resultMap>"+separator+separator);
		
		// sql fragement 构造
		// 注释
		StringBuilder commentBuilder = new StringBuilder();
		commentBuilder.append("  <!--    "+separator);
		commentBuilder.append("      table: "+tbName+"("+tbInfo.getTbRemarks()+")"+separator);
		// 首先循环主键 - 单独成行
		StringBuilder idBuilder = new StringBuilder();
		StringBuilder idClnBuilder = new StringBuilder();
		StringBuilder updateWhereClnBuilder = new StringBuilder();
		for(int idx=0; idx<primaryKeys.size(); idx++) {
			ColumnInfo idInfo = primaryKeys.get(idx);
			// 列名称
			String columnName = idInfo.getColumnName();
			// 列描述
			String columnRemarks = idInfo.getColumnRemarks();
			// JDBC 类型
			String jdbcTypeName = idInfo.getJdbcTypeName();
			String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
			// java字段名称
			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
			if(idx==0) {
				idBuilder.append("    "+"    "+columnName+",");
				
				// 逻辑列默认值
				if(logicDelete && columnName.equalsIgnoreCase(logicColumn)) {
					String logicValue = getLogicValue(logicColumn, logicNotDeleteValue, logicColumnInfo);
					idClnBuilder.append("    "+"    "+logicValue+",");
				} else {
					idClnBuilder.append("    "+"    #{"+fieldName+",jdbcType="+jdbcTypeName+"},");
				}
			} else {
				idBuilder.append(columnName+",");
				
				// 逻辑列默认值
				if(logicDelete && columnName.equalsIgnoreCase(logicColumn)) {
					String logicValue = getLogicValue(logicColumn, logicNotDeleteValue, logicColumnInfo);
					idClnBuilder.append(logicValue+",");
				} else {
					idClnBuilder.append("#{"+fieldName+",jdbcType="+jdbcTypeName+"},");
				}
			}
			// 组装注释
			commentBuilder.append("        column: "+columnName+"("+columnRemarks+")"+separator);
			
			updateWhereClnBuilder.append(" "+columnName+" = #{"+fieldName+",jdbcType="+jdbcTypeName+"} AND ");
		}
		if(updateWhereClnBuilder.length()>0) {
			updateWhereClnBuilder.delete(updateWhereClnBuilder.length()-5, updateWhereClnBuilder.length());
		}
		// 处理最后一位逗号
		if(idBuilder.length()>0) {
			idBuilder.delete(idBuilder.length()-1, idBuilder.length());
			idClnBuilder.delete(idClnBuilder.length()-1, idClnBuilder.length());
		}
		// 处理非主键字段 - 没5个换行
		StringBuilder colBuilder = new StringBuilder();
		StringBuilder colClnBuilder = new StringBuilder();
		StringBuilder updateClnBuilder = new StringBuilder();
		StringBuilder updateClolumnBuilder = new StringBuilder();
		
		boolean flag = false;
		int count = 0;
		for(int idx=0; idx<columnList.size(); idx++) {
			ColumnInfo columnInfo = columnList.get(idx);
			int pos = primaryKeys.indexOf(columnInfo);
			if(-1 == pos) {
				count++;
				// 列名称
				String columnName = columnInfo.getColumnName();
				// 列描述
				String columnRemarks = columnInfo.getColumnRemarks();
				// JDBC 类型
				String jdbcTypeName = columnInfo.getJdbcTypeName();
				String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
				// java字段名称
				fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
				
				flag = false;
				// 当参数列的个数小于 lineSize 时, 需要对第1个元素处理
				if((idx)%LINE_SIZE == 1 || ((columnList.size()<=LINE_SIZE) && idx==0)) {
					colBuilder.append("    "+"    ");
					colClnBuilder.append("    "+"    ");
				}
				colBuilder.append(columnName+",");
				
				
				if(logicDelete && columnName.equalsIgnoreCase(logicColumn)) {
					String logicValue = getLogicValue(logicColumn, logicNotDeleteValue, logicColumnInfo);
					colClnBuilder.append(logicValue+",");
					
					// 逻辑删除字段不能赋值
					updateClnBuilder.append("");
				} else {
					colClnBuilder.append("#{"+fieldName+",jdbcType="+jdbcTypeName+"},");
					
					updateClnBuilder.append("        "+columnName+" = #{"+fieldName+",jdbcType="+jdbcTypeName+"},"+separator);
				}
				
				if(count!=0) {
					//换行标志
					boolean newLineFlag = (count)%LINE_SIZE == 0;
					if(newLineFlag) {
					   flag = true;
					   colBuilder.append(separator);
					   colClnBuilder.append(separator);
					}
				}
				// 组装注释
				commentBuilder.append("        column: "+columnName+"("+columnRemarks+")"+separator);
				
				if(logicDelete && logicColumn.equalsIgnoreCase(columnName)) {
					// 更新语句不能包含 逻辑删除列 
				} else {
					updateClolumnBuilder.append("      <if test=\""+fieldName+" != null\">"+separator);
					updateClolumnBuilder.append("        "+columnName+" = #{"+fieldName+",jdbcType="+jdbcTypeName+"},"+separator);
					updateClolumnBuilder.append("      </if>"+separator);
				}
			}
		}
		if(idBuilder.length()>0) {
		   idBuilder.append(",");
		   idClnBuilder.append(",");
		}
		if(colBuilder.length()>0) {
			//colBuilder.insert(0, "    ");
			//colClnBuilder.insert(0, "    ");
			if(flag) {
				colBuilder.delete(colBuilder.length()-separator.length()-1, colBuilder.length());
				colClnBuilder.delete(colClnBuilder.length()-separator.length()-1, colClnBuilder.length());
			} else {
				colBuilder.delete(colBuilder.length()-1, colBuilder.length());
				colClnBuilder.delete(colClnBuilder.length()-1, colClnBuilder.length());
			}
		}
		if(updateClnBuilder.length()>0) {
			updateClnBuilder.delete(updateClnBuilder.length()-separator.length()-1, updateClnBuilder.length());
		}
		
		commentBuilder.append("  -->");
		
		// 组装最终的 sql fragement
		content.append(commentBuilder+separator);
		content.append("  <sql id=\"Base_Column_List\">"+separator);
		if(idBuilder.length()>0) {
		   content.append(idBuilder+separator);
		}
		if(colBuilder.length()>0) {
		   content.append(colBuilder+separator);
		}
		content.append("  </sql>"+separator+separator);
		
		
		
		// 构建 insert
		content.append("  <insert id=\"insert\" parameterType=\""+entityClassName+"\">"+separator);
		content.append("    insert into "+tbName+" ("+separator);
		// 构建字段
		if(idBuilder.length()>0) {
			content.append(idBuilder+separator);
		}
		content.append(colBuilder+separator);
		//sb.append("    <include refid=\"Base_Column_List\" />");
		content.append("    )"+separator);
		content.append("    values ("+separator);
		if(idBuilder.length()>0) {
			content.append(idClnBuilder+separator);
		}
		content.append(colClnBuilder+separator);
		content.append("");
		content.append("    )"+separator);
		content.append("");
		content.append("");
		content.append("  </insert>"+separator+separator);
		
		// insertSelective
		content.append("  <insert id=\"insertSelective\" parameterType=\""+entityClassName+"\">"+separator);
		content.append("    insert into "+tbName+" "+separator);
		
		
		// 组装字段
		StringBuilder columnBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		for(int idx=0; idx<columnList.size(); idx++) {
			ColumnInfo columnInfo = columnList.get(idx);
			// 列名称
			String columnName = columnInfo.getColumnName();
			// JDBC 类型
			String jdbcTypeName = columnInfo.getJdbcTypeName();
			String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
			// java字段名称
			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
			
			columnBuilder.append("      <if test=\""+fieldName+" != null\">"+separator);
			columnBuilder.append("        "+columnName+","+separator);
			columnBuilder.append("      </if>"+separator);
			if(logicDelete && columnName.equalsIgnoreCase(logicColumn)) {
				valueBuilder.append("      <if test=\""+fieldName+" != null\">"+separator);
				String logicValue = getLogicValue(logicColumn, logicNotDeleteValue, logicColumnInfo);
				valueBuilder.append("        "+logicValue+","+separator);
				valueBuilder.append("      </if>"+separator);
			} else {
				valueBuilder.append("      <if test=\""+fieldName+" != null\">"+separator);
				valueBuilder.append("        #{"+fieldName+",jdbcType="+jdbcTypeName+"},"+separator);
				valueBuilder.append("      </if>"+separator);
			}
		}
		
		content.append("    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">"+separator);
		content.append(columnBuilder);
		content.append("    </trim>"+separator);
		content.append("    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">"+separator);
		content.append(valueBuilder);
		content.append("    </trim>"+separator);
		content.append("  </insert>"+separator+separator);
					
		// 构建 delete/update
		if(!primaryKeys.isEmpty()) {
			ColumnInfo idInfo = primaryKeys.get(0);
			String idType = idInfo.getJavaTypeFullName();
			String columnName = idInfo.getColumnName();
			String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
			String jdbcTypeName = idInfo.getJdbcTypeName();
			
			if(logicDelete) {// 逻辑删除
				// ColumnInfo logicColumnInfo = tbInfo.findLogicColumnInfo();
				// logicColumnInfo.getJavaTypeFullName();-- 
				content.append("  <update id=\"deleteByPrimaryKey\" parameterType=\""+idType+"\">"+separator);
				String where = logicWhere(logicColumn, logicDeleteValue, logicColumnInfo);
				content.append("    update "+tbName+" set "+where+" where "+columnName+"=#{"+fieldName+",jdbcType="+jdbcTypeName+"} "+separator);
				content.append("  </update>"+separator+separator);
			} else {// 物理逻辑删除
				content.append("  <delete id=\"deleteByPrimaryKey\" parameterType=\""+idType+"\">"+separator);
				content.append("    delete from "+tbName+" where "+columnName+" = #{"+fieldName+",jdbcType="+jdbcTypeName+"}"+separator);
				content.append("  </delete>"+separator+separator);
			}
		} else {
			// 没有主键, 暂时不知道如何处理
		}
		
		// 构建 update
		if(primaryKeys.size()>0) {
			// updateByPrimaryKey
			content.append("  <update id=\"updateByPrimaryKey\" parameterType=\""+entityClassName+"\">"+separator);
			content.append("    update "+tbName+" "+separator);
			content.append("    set "+separator);
			content.append(updateClnBuilder+separator);
			content.append("    where "+separator+"      "+updateWhereClnBuilder+" "+separator);
			content.append("  </update>"+separator+separator);
			
			// updateByPrimaryKeySelective 
			content.append("  <update id=\"updateByPrimaryKeySelective\" parameterType=\""+entityClassName+"\">"+separator);
			content.append("    update "+tbName+" "+separator);
			content.append("    <set>"+separator);
			content.append(updateClolumnBuilder);
			content.append("    </set>"+separator);
			content.append("    where "+separator+"      "+updateWhereClnBuilder+" "+separator);
			content.append("  </update>"+separator+separator);
		}
		content.append("");
		content.append("");
		content.append("");
		content.append("");
		content.append("");
		
		// 构建 select
		if(!primaryKeys.isEmpty()) {
			// 逻辑删除增加的语句
			String logicWhere = logicDelete ?  " AND ("+logicWhere(logicColumn, logicNotDeleteValue, logicColumnInfo)+") " : "";
			
			ColumnInfo idInfo = primaryKeys.get(0);
			String idType = idInfo.getJavaTypeFullName();
			String columnName = idInfo.getColumnName();
			String fieldName = camelCase(columnName.toLowerCase().split("_"), 0);
			fieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
			String jdbcTypeName = idInfo.getJdbcTypeName();
			content.append("  <select id=\"selectByPrimaryKey\" parameterType=\""+idType+"\" resultMap=\"BaseResultMap\">"+separator);
			content.append("    select "+separator);
			content.append("      <include refid=\"Base_Column_List\" /> "+separator);
			content.append("    from "+tbName+" "+separator);
			content.append("    where ("+columnName+" = #{"+fieldName+",jdbcType="+jdbcTypeName+"}) "+logicWhere+separator);
			content.append("  </select> "+separator+separator);
		} else {
			// 没有主键, 暂时不知道如何处理
		}
		content.append("");
		content.append("");
		
		content.append("");
		content.append("</mapper>");
		
		fileInfo.setContent(content.toString());
		
		return fileInfo;
	}
	
	/**
	 * 生成 service 接口
	 * @param tbInfo
	 */
	private FileInfo generatorService(TableInfo tbInfo, CodeMetaData codeMetaData) {
		FileInfo fileInfo = new FileInfo();
		StringBuilder content = new StringBuilder();
		
		String tbName = tbInfo.getTbName();
		// 实体名称
		String className = codeMetaData.getEntityName();
		String entityLowerClassName = className.substring(0, 1).toLowerCase()+className.substring(1);
		String serviceClassName = codeMetaData.getServiceName(); 
		// 实体类名称
		String entityClassName = codeMetaData.getEntityFullName(); 
		
		// 设置 package
		String packageName = codeMetaData.getServiceFullPackage();
		content.append("package "+packageName+";" + separator + separator);
		content.append("import "+entityClassName+";"+separator);
		content.append("import com.baomidou.mybatisplus.extension.service.IService;"+separator);
		
		StringBuilder impBuilder = new StringBuilder();
		Set<String> temp = new HashSet<String>();
		List<ColumnInfo> primaryKeys = tbInfo.getPrimaryKeys();
		if(primaryKeys.size()>0) {
			ColumnInfo idInfo = primaryKeys.get(0);
			// 类型全名称
			String javaTypeFullName = idInfo.getJavaTypeFullName();
			if(!temp.contains(javaTypeFullName)) {
				impBuilder.append("import "+javaTypeFullName+";"+separator);
				temp.add(javaTypeFullName);
			}
		}
		// import 
		content.append(impBuilder);
		content.append(separator);
		
		// 生成注释信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		content.append("/**" + separator);
		//sb.append("<pre>" + separator);
		content.append(" * @author: " + (codeConfig.getAuthor()==null?"":codeConfig.getAuthor())+ separator);
		content.append(" * @description: " + (tbInfo.getTbRemarks()==null?"":tbInfo.getTbRemarks()) + separator);
		content.append(" * @createDate: " + sdf.format(new Date()) + separator);
		//sb.append("</pre>" + separator);
		content.append(" */" + separator);
		
		content.append("public interface "+serviceClassName+" extends IService<"+className+"> { "+separator+separator);
		
		// 保存
		content.append("	/** 保存  **/ "+separator);
		content.append("	public "+className+" insert("+className+" "+entityLowerClassName+");"+separator+separator);
		
		// 根据主键删除
		if(!primaryKeys.isEmpty()) {
			ColumnInfo idInfo = primaryKeys.get(0);
			String javaTypeShortName = idInfo.getJavaTypeShortName();
			String columnName = idInfo.getColumnName();
			String idField = camelCase(columnName.split("_"), 0);
			idField = idField.substring(0, 1).toLowerCase()+idField.substring(1);
			content.append("	/** 根据主键删除  **/ "+separator);
			content.append("	public void delete("+javaTypeShortName+" "+idField+"); "+separator+separator);
		}
		
		// 根据实体对象删除
		content.append("	/** 根据实体对象删除  **/ "+separator);
		content.append("	public void delete("+className+" "+entityLowerClassName+"); " + separator+separator);
		
		// 更新
		content.append("	/** 根据实体更新  **/ "+separator);
		content.append("	public void update("+className+" "+entityLowerClassName+"); " + separator+separator);
		
		// 保存或更新
		content.append("	/** 根据实体对象删除  **/ "+separator);
		content.append("	public void merge("+className+" "+entityLowerClassName+"); " + separator+separator);
		
		// 根据主键查找实体
		if(!primaryKeys.isEmpty()) {
			ColumnInfo idInfo = primaryKeys.get(0);
			String javaTypeShortName = idInfo.getJavaTypeShortName();
			String columnName = idInfo.getColumnName();
			String idField = camelCase(columnName.split("_"), 0);
			idField = idField.substring(0, 1).toLowerCase()+idField.substring(1);
			content.append("	/** 根据主键查找实体  **/ "+separator);
			content.append("	public "+className+" findOne("+javaTypeShortName+" "+idField+"); "+separator+separator);
		}
		
		content.append("}"+separator+separator);
		
		fileInfo.setPackageName(packageName);
		fileInfo.setFileName(serviceClassName);
		fileInfo.setFileType("java");
		fileInfo.setTbName(tbName);
		fileInfo.setContent(content.toString());
		
		return fileInfo;
	}
	
	/**
	 * 生成 service 接口 实现类
	 * @param tbInfo
	 */
	private FileInfo generatorServiceImpl(TableInfo tbInfo, CodeMetaData codeMetaData) {
		FileInfo fileInfo = new FileInfo();
		StringBuilder content = new StringBuilder();
		
		String tbName = tbInfo.getTbName();
		// 实体名称
		String className = codeMetaData.getEntityName(); 
		// 实体类小写
		String entityLowerClassName = className.substring(0, 1).toLowerCase()+className.substring(1);
		String serviceClassName = codeMetaData.getServiceImplName();
		// 实体类名称
		String entityClassName = codeMetaData.getEntityFullName();
		// Mapper 接口类名称
		String mapperClassName   = codeMetaData.getRepositoryFullName(); 
		String iserviceClassName = codeMetaData.getServiceFullName(); 
		// service 小写变量
		String serviceName = codeMetaData.getServiceName();
		String lowerServiceName = className + serviceName.substring(serviceName.indexOf(className) + className.length());
		lowerServiceName = lowerServiceName.substring(0, 1).toLowerCase() + lowerServiceName.substring(1);
		// mapper 小写变量
		String lowerMapperName = codeMetaData.getRepositoryName();
		lowerMapperName = className + lowerMapperName.substring(lowerMapperName.indexOf(className) + className.length());
		lowerMapperName = lowerMapperName.substring(0, 1).toLowerCase() + lowerMapperName.substring(1);
		
		// 设置 package
		String packageName = codeMetaData.getServiceImplFullPackage(); 
		content.append("package "+packageName+";" + separator + separator);
		content.append("import org.springframework.beans.factory.annotation.Autowired;"+separator);
		content.append("import org.springframework.stereotype.Service;"+separator);
		content.append("import org.springframework.transaction.annotation.Transactional;"+separator);
		content.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;"+separator);
		content.append("import "+entityClassName+";"+separator);
		content.append("import "+mapperClassName+";"+separator);
		content.append("import "+iserviceClassName+";"+separator);
		
		StringBuilder impBuilder = new StringBuilder();
		Set<String> temp = new HashSet<String>();
		List<ColumnInfo> primaryKeys = tbInfo.getPrimaryKeys();
		if(primaryKeys.size()>0) {
			ColumnInfo idInfo = primaryKeys.get(0);
			// 类型全名称
			String javaTypeFullName = idInfo.getJavaTypeFullName();
			if(!temp.contains(javaTypeFullName)) {
				impBuilder.append("import "+javaTypeFullName+";"+separator);
				temp.add(javaTypeFullName);
			}
		}
		// import 
		content.append(impBuilder);
		content.append(separator);
		
		// 生成注释信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		content.append("/**" + separator);
		//sb.append("<pre>" + separator);
		content.append(" * @author: " + (codeConfig.getAuthor()==null?"":codeConfig.getAuthor())+ separator);
		content.append(" * @description: " + (tbInfo.getTbRemarks()==null?"":tbInfo.getTbRemarks()) + separator);
		content.append(" * @createDate: " + sdf.format(new Date()) + separator);
		//sb.append("</pre>" + separator);
		content.append(" */" + separator);
		
		content.append("@Transactional(rollbackFor=Exception.class)" + separator);
		content.append("@Service(\""+lowerServiceName+"\")" + separator);
		content.append("public class "+serviceClassName+" extends ServiceImpl<"+className+"Mapper, "+className+"> implements "+serviceName+" { "+separator+separator);
		
		// 属性
		content.append("	@Autowired "+separator);
		content.append("	private "+codeMetaData.getRepositoryName()+" "+lowerMapperName+"; "+separator+separator);
		
		if(!primaryKeys.isEmpty()) {
			ColumnInfo idInfo = primaryKeys.get(0);
			String columnName = idInfo.getColumnName();
			String idFieldUpper = camelCase(columnName.split("_"), 0);
			String idField = idFieldUpper.substring(0, 1).toLowerCase()+idFieldUpper.substring(1);
			String javaTypeShortName = idInfo.getJavaTypeShortName();
			
			// 保存
			content.append("	/** 保存  **/ "+separator);
			content.append("	@Override "+separator);
			content.append("	@Transactional(rollbackFor=Exception.class) "+separator);
			content.append("	public "+className+" insert("+className+" "+entityLowerClassName+") { "+separator);
			content.append("		int count = "+lowerMapperName+".insertSelective("+entityLowerClassName+"); "+separator);
			content.append("		// 主键 "+separator);
			content.append("		"+javaTypeShortName+" "+idField+" = "+entityLowerClassName+".get"+idFieldUpper+"(); "+separator);
			content.append("		"+className+" new"+className+" = "+lowerMapperName+".selectByPrimaryKey("+idField+"); "+separator);
			content.append("		return new"+className+"; "+separator);
			content.append("	} "+separator+separator);
			
			// 根据主键删除
			content.append("	/** 根据主键删除  **/ "+separator);
			content.append("	@Override "+separator);
			content.append("	@Transactional(rollbackFor=Exception.class) "+separator);
			content.append("	public void delete("+javaTypeShortName+" "+idField+") { "+separator);
			content.append("		if("+idField+"!=null) { "+separator);
			content.append("			int count = "+lowerMapperName+".deleteByPrimaryKey("+idField+"); "+separator);
			content.append("		}"+separator);
			content.append("	 "+separator);
			content.append("	} "+separator+separator);
			
			// 根据实体对象删除
			content.append("	/** 根据实体对象删除  **/ "+separator);
			content.append("	@Override "+separator);
			content.append("	@Transactional(rollbackFor=Exception.class) "+separator);
			content.append("	public void delete("+className+" "+entityLowerClassName+") { " + separator);
			content.append("		"+javaTypeShortName+" "+idField+" = "+entityLowerClassName+".get"+idFieldUpper+"(); "+separator);
			content.append("		if("+idField+"!=null) { "+separator);
			content.append("			int count = "+lowerMapperName+".deleteByPrimaryKey("+idField+"); "+separator);
			content.append("		}"+separator);
			content.append("	 "+separator);
			content.append("	} "+separator+separator);
			
			// 更新
			content.append("	/** 根据实体更新  **/ "+separator);
			content.append("	@Override "+separator);
			content.append("	@Transactional(rollbackFor=Exception.class) "+separator);
			content.append("	public void update("+className+" "+entityLowerClassName+") { " + separator);
			content.append("		"+javaTypeShortName+" "+idField+" = "+entityLowerClassName+".get"+idFieldUpper+"(); "+separator);
			content.append("		if("+idField+"!=null) { "+separator);
			content.append("			int count = "+lowerMapperName+".updateByPrimaryKeySelective("+entityLowerClassName+"); "+separator);
			content.append("		}"+separator);
			content.append("	 "+separator);
			content.append("	} "+separator+separator);
			
			// 保存或更新
			content.append("	/** 根据实体对象删除  **/ "+separator);
			content.append("	@Override "+separator);
			content.append("	@Transactional(rollbackFor=Exception.class) "+separator);
			content.append("	public void merge("+className+" "+entityLowerClassName+") { " + separator);
			content.append("		int count = -1; "+separator);
			content.append("		"+className+" new"+className+" = null; "+separator);
			content.append("		"+javaTypeShortName+" "+idField+" = "+entityLowerClassName+".get"+idFieldUpper+"();"+separator);
			content.append("		if("+idField+"==null) { "+separator);
			content.append("			// 新增"+separator);
			content.append("			count = "+lowerMapperName+".insertSelective("+entityLowerClassName+");"+separator);
			content.append("		} else {"+separator);
			content.append("			new"+className+" = "+lowerMapperName+".selectByPrimaryKey("+idField+");"+separator);
			content.append("			if(new"+className+"==null) {"+separator);
			content.append("				// 新增 "+separator);
			content.append("				count = "+lowerMapperName+".insertSelective("+entityLowerClassName+");"+separator);
			content.append("			} else { "+separator);
			content.append("				// 修改"+separator);
			content.append("				count = "+lowerMapperName+".updateByPrimaryKeySelective("+entityLowerClassName+");"+separator);
			content.append("			}"+separator);
			content.append("		}"+separator);
			content.append("	 "+separator);
			content.append("	 	"+idField+" = "+entityLowerClassName+".get"+idFieldUpper+"();"+separator);
			content.append("	 	new"+className+" = "+lowerMapperName+".selectByPrimaryKey("+idField+");"+separator);
			content.append("	 	"+separator);
			//content.append("	 	return new"+className+";"+separator);
			content.append("	} "+separator+separator);
			
			// 根据主键查找实体
			content.append("	/** 根据主键查找实体  **/ "+separator);
			content.append("	@Override "+separator);
			//content.append("	@Transactional(rollbackFor=Exception.class) "+separator);
			content.append("	public "+className+" findOne("+javaTypeShortName+" "+idField+") { "+separator);
			content.append("		"+className+" "+entityLowerClassName+" = "+lowerMapperName+".selectByPrimaryKey("+idField+"); "+separator);
			content.append("		return "+entityLowerClassName+"; "+separator);
			content.append("	} "+separator+separator);
		}
		
		content.append("}"+separator+separator);
		
		fileInfo.setPackageName(packageName);
		fileInfo.setFileName(serviceClassName);
		fileInfo.setFileType("java");
		fileInfo.setTbName(tbName);
		fileInfo.setContent(content.toString());
		
		return fileInfo;
	}
	
	/**
	 * 生成 controller 接口 实现类
	 * @param tbInfo
	 */
	private FileInfo generatorController(TableInfo tbInfo, CodeMetaData codeMetaData) {
		FileInfo fileInfo = new FileInfo();
		StringBuilder content = new StringBuilder();
		
		String tbName = tbInfo.getTbName();
		// 实体名称
		String className = codeMetaData.getEntityName();
		// 实体类小写
		String entityLowerClassName = className.substring(0, 1).toLowerCase()+className.substring(1);
		// service 全名称
		String iserviceClassName = codeMetaData.getServiceFullName();
		String packageName = codeMetaData.getControllerFullPackage();
		String controllerClassName = codeMetaData.getControllerName();
		// service 名称
		String serviceClassName = codeMetaData.getServiceName(); 
		// service 小写变量
		String lowerServiceName = entityLowerClassName + serviceClassName.substring(serviceClassName.indexOf(className)+className.length());
		
		content.append("package "+packageName+";"+separator+separator);
		
		content.append("import org.springframework.beans.factory.annotation.Autowired;"+separator);
		content.append("import org.springframework.web.bind.annotation.RequestMapping;"+separator);
		content.append("import org.springframework.web.bind.annotation.RestController;"+separator);
		content.append("import "+iserviceClassName+";"+separator+separator);
		
		// 生成注释信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		content.append("/**" + separator);
		//sb.append("<pre>" + separator);
		content.append(" * @author: " + (codeConfig.getAuthor()==null?"":codeConfig.getAuthor())+ separator);
		content.append(" * @description: " + (tbInfo.getTbRemarks()==null?"":tbInfo.getTbRemarks()) + separator);
		content.append(" * @createDate: " + sdf.format(new Date()) + separator);
		//sb.append("</pre>" + separator);
		content.append(" */" + separator);
		
		content.append("@RestController"+separator);
		content.append("@RequestMapping(\"/"+entityLowerClassName+"\")"+separator);
		content.append("public class "+controllerClassName+" {"+separator+separator);
		content.append("	@Autowired"+separator);
		content.append("	private "+serviceClassName+" "+lowerServiceName+";"+separator);
		content.append("	"+separator);
		content.append("	"+separator);
		content.append("}"+separator+separator);
		
		fileInfo.setPackageName(packageName);
		fileInfo.setFileName(controllerClassName);
		fileInfo.setFileType("java");
		fileInfo.setTbName(tbName);
		fileInfo.setContent(content.toString());
		
		return fileInfo;
	}
	
	
	
	/**
	 * 获取排除指定表的信息
	 */
	private List<TableInfo> getExcludeTableInfo(Connection con, String tables){
		String[] tbs = tables.split("\\s+");
		List<TableInfo> list = getAllTableMataDataInfo(con);
		
		List<TableInfo> excludeList = new ArrayList<TableInfo>();
		for(String tbName : tbs) {
			TableInfo tbInfo = new TableInfo(tbName);
			int position = list.indexOf(tbInfo);
			if(-1==position) {
			   excludeList.add(tbInfo);
			}
		}
		// 重置集合
		list = excludeList;
		
		mergeTableInfo(con, list);
		
		return list;
	}
	
	/**
	 * 获取指定表的信息
	 */
	private List<TableInfo> getIncludeTableInfo(Connection con, String tables){
		String[] tbs = tables.split("\\s+");
		List<TableInfo> list = getAllTableMataDataInfo(con);
		
		List<TableInfo> includeList = new ArrayList<TableInfo>();
		for(String tbName : tbs) {
			TableInfo tbInfo = new TableInfo(tbName);
			int position = list.indexOf(tbInfo);
			if(-1!=position) {
			   includeList.add(tbInfo);
			}
		}
		// 重置集合
		list = includeList;
		
		mergeTableInfo(con, list);
		
		return list;
	}
	
	/**
	 * 获取所有表的信息
	 */
	private List<TableInfo> getAllTableInfo(Connection con){
		List<TableInfo> list = getAllTableMataDataInfo(con);
		mergeTableInfo(con, list);
		
		return list;
	}

	/**
	 * 合并表和列的数据
	 */
	private void mergeTableInfo(Connection con, List<TableInfo> list) {
		if(list!=null) {
			for(TableInfo tbInfo : list) {
				// 获取表所有的列
				List<ColumnInfo> columnList = getTableColumnInfo(con, tbInfo);
				tbInfo.setColumnList(columnList);
				
				
				// 获取表所有主键列
				List<ColumnInfo> pkCplomnList = getTablePkColomns(con, tbInfo, columnList);
				tbInfo.setPrimaryKeys(pkCplomnList);
				
				// 对list进行排序
				Collections.sort(columnList, new ColumnInfoComparator()); 
				//System.out.println();
			}
		}
	}
	
	/**
	 * 获取表的主键
	 */
	private List<ColumnInfo> getTablePkColomns(Connection con, TableInfo tbInfo, List<ColumnInfo> columnList) {
		String tbName = tbInfo.getTbName();
		List<ColumnInfo> pkCplomnList = new ArrayList<ColumnInfo>();
		List<String> pkList = getPrimaryKeys(con, tbName);
		if(pkList!=null) {
			for(String pk : pkList) {
				ColumnInfo pkInfo = new ColumnInfo(tbName.toLowerCase(), pk.toLowerCase());
				int position = columnList.indexOf(pkInfo);
				if(-1!=position) {
					ColumnInfo newPkInfo = columnList.get(position);
					 // 设置主键标识为true
					newPkInfo.setPrimaryKey(true);
					pkCplomnList.add(newPkInfo);
				}
			}
		}
		
		return pkCplomnList;
	}
	
	/**
	 * 获取主键属性
	 */
	private List<String> getPrimaryKeys(Connection conn, String tbName) {
		List<String> pkList = new ArrayList<String>();
		
		tbName = tbName.toUpperCase().trim();
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			String catalog = conn.getCatalog();
			String schema = dbConfig.getSchema();
			
			// 获取单表的主键
			rs = metaData.getPrimaryKeys(catalog, schema, tbName);  
			while(rs.next()){  
			    String pk = rs.getString("COLUMN_NAME");  
			    pkList.add(pk);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(null, null, rs);
		}
		
		return pkList;
	}
	
	/**
	 * 获取表的元数据:  表名、表名描述
	 */
	private List<TableInfo> getAllTableMataDataInfo(Connection con) { 
		List<TableInfo> list = new ArrayList<TableInfo>();
		
		ResultSet rs = null;
		try {
			String catalog = con.getCatalog();
			String schema = dbConfig.getSchema();
			DatabaseMetaData dbmd = con.getMetaData(); 
			rs = dbmd.getTables(catalog, schema, null, new String[]{"TABLE"});
			while(rs.next()) {
				// 获取表名称
				String tbName = rs.getString("TABLE_NAME");
				// 获取表的描述信息
				String tbRemarks = rs.getString("REMARKS");
				
				TableInfo tbInfo = new TableInfo();
				// 表名
				tbInfo.setTbName(tbName.toLowerCase());
				// 表描述
				tbInfo.setTbRemarks(tbRemarks);
				
				list.add(tbInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(null, null, rs);
		}
		
		return list;
	}
	
	/**
	 * 获取单表的列信息
	 */
	private List<ColumnInfo> getTableColumnInfo(Connection conn, TableInfo tbInfo) {
		List<ColumnInfo> list = new ArrayList<ColumnInfo>();
		
		String tbName = tbInfo.getTbName();
		tbName = tbName.toUpperCase().trim();
		
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			String catalog = conn.getCatalog();
			String schema = dbConfig.getSchema();
			
			// 配置的逻辑删除列
			String logicColumn = codeConfig.getLogicColumn();
			
			rs = metaData.getColumns(catalog, schema, tbName, "%"); 
			while(rs.next()) { 
				// 列名称
			    String columnName = rs.getString("COLUMN_NAME"); 
			    // 列描述
			    String columnRemarks = rs.getString("REMARKS"); 
			    // 数据类型
			    //String columnType = rs.getString("TYPE_NAME"); 
			    // 长度
			    int length = rs.getInt("COLUMN_SIZE"); 
			    // 小数位数
			    int scale = rs.getInt("DECIMAL_DIGITS"); 
			    // 是否允许为空(true 表示允许为空, flase表示不允许)
			    boolean nullable = rs.getInt("NULLABLE") == 1;
			    
			    int jdbcType = rs.getInt("DATA_TYPE");
			    TypeMapping mapping = TYPE_MAPPING.get(jdbcType);
			    String jdbcTypeName      = mapping.getJdbcTypeName();
			    String javaTypeShortName = mapping.getJavaTypeShortName();
			    String javaTypeFullName  = mapping.getJavaTypeFullName();
			    
			    ColumnInfo colInfo = new ColumnInfo(tbName.toLowerCase(), columnName.toLowerCase());
			    colInfo.setColumnRemarks(columnRemarks);
			    colInfo.setLength(length);
			    colInfo.setScale(scale);
			    colInfo.setNullable(nullable);
			    colInfo.setJdbcTypeName(jdbcTypeName);
			    colInfo.setJavaTypeShortName(javaTypeShortName);
			    colInfo.setJavaTypeFullName(javaTypeFullName);
			    // 设置主键标识为false
			    colInfo.setPrimaryKey(false);
			    
			    list.add(colInfo);
			    
			    // 当前列是逻辑删除列
			    if(logicColumn.equalsIgnoreCase(columnName)) {
			    	String logicDeleteValue = codeConfig.getLogicDeleteValue();
					String logicNotDeleteValue = codeConfig.getLogicNotDeleteValue();
					
					tbInfo.setLogicDelete(true);
					tbInfo.setLogicColumn(columnName);
					tbInfo.setLogicDeleteValue(logicDeleteValue);
					tbInfo.setLogicNotDeleteValue(logicNotDeleteValue);
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(null, null, rs);
		}
		
		return list;
	}
	
	/**
	 * 逻辑列过滤语句
	 */
	private String logicWhere(String logicColumn, String logicValue, ColumnInfo logicColumnInfo) {
		String where = "";
		
		/*
		// 神通数据库
		String[] types = {
				     "TINYINT", "SMALLINT", "REAL", "INTEGER", "BIGINT", "DOUBLE", "FLOAT", "DECIMAL", "NUMERIC", 
				     "BIT", "BOOLEAN"};
		*/
		
		// mysql数据库
		String[] types = {
				     "TINYINT", "SMALLINT", "REAL", "INTEGER", "BIGINT", "DOUBLE", "FLOAT", "DECIMAL", "NUMERIC", 
				     "BIT", "BOOLEAN"};
		
		List<String> list = Arrays.asList(types);
		String jdbcTypeName = logicColumnInfo.getJdbcTypeName();
		String javaTypeFullName = logicColumnInfo.getJavaTypeFullName();
		if(javaTypeFullName.equals(String.class.getName())) {
			where = logicColumn+"='"+logicValue+"'";
		} else 
		if(list.contains(jdbcTypeName)) {
			where = logicColumn+"="+logicValue+"";
		}
		
		return where;
	}
	
	/**
	 * 逻辑列默认值
	 */
	private String getLogicValue(String logicColumn, String logicValue, ColumnInfo logicColumnInfo) {
		String value = "";
		
		/*
		// 神通数据库
		String[] types = {
				     "TINYINT", "SMALLINT", "REAL", "INTEGER", "BIGINT", "DOUBLE", "FLOAT", "DECIMAL", "NUMERIC", 
				     "BIT", "BOOLEAN"};
		*/
		
		// mysql数据库
		String[] types = {
				     "TINYINT", "SMALLINT", "REAL", "INTEGER", "BIGINT", "DOUBLE", "FLOAT", "DECIMAL", "NUMERIC", 
				     "BIT", "BOOLEAN"};
		
		List<String> list = Arrays.asList(types);
		String jdbcTypeName = logicColumnInfo.getJdbcTypeName();
		String javaTypeFullName = logicColumnInfo.getJavaTypeFullName();
		if(javaTypeFullName.equals(String.class.getName())) {
			value = "'"+logicValue+"'";
		} else 
		if(list.contains(jdbcTypeName)) {
			value = ""+logicValue+"";
		}
		
		return value;
	}
	
	/**
	   * 转驼峰命名
	 */
	private String camelCase(String[] tbNames, int position) {
		StringBuilder tbName = new StringBuilder();
		for(int idx=position; idx<tbNames.length; idx++) {
			String name = tbNames[idx];
			if(!isEmpty(name)){
				name = name.trim().toLowerCase();
				tbName.append(name.substring(0, 1).toUpperCase());
				if(name.length()>1){
					tbName.append(name.substring(1).toLowerCase());
				}
			}
		}
		
		return tbName.toString();
	}
	
	/**
	 * 字符串为空判断
	 * @param str
	 * @return
	 */
	private boolean isEmpty(String str) {
		return str==null || "".equals(str.trim());
	}
	
	
	/**
	 * 获取数据库连接
	 */
	private Connection getConnection() {
		Connection conn  = null;
		try {
			Class.forName(dbConfig.getDirverClass());
			conn  = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	/**
	 * 关闭数据库资源
	 */
	private void closeResource(Connection conn, Statement stmt, ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt = null;
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}
	}
	
	
	
	
	
	
	
	private static final Map<Integer, TypeMapping> TYPE_MAPPING = new HashMap<Integer, TypeMapping>();
	
	static {
		/**
		 * mybatis-generator-core-1.3.6.jar 
		 * 数据类型参考类:  org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl 
		 * java.sql.JDBCType 
		 */
		TYPE_MAPPING.put(Types.ARRAY,          new TypeMapping(Types.ARRAY,         "ARRAY",         Object.class.getName()));
	    TYPE_MAPPING.put(Types.DATALINK,       new TypeMapping(Types.DATALINK,      "DATALINK",      Object.class.getName()));
	    TYPE_MAPPING.put(Types.NULL,           new TypeMapping(Types.NULL,          "NULL",          Object.class.getName()));
	    TYPE_MAPPING.put(Types.OTHER,          new TypeMapping(Types.OTHER,         "OTHER",         Object.class.getName()));
	    TYPE_MAPPING.put(Types.REF,            new TypeMapping(Types.REF,           "REF",           Object.class.getName()));
	    TYPE_MAPPING.put(Types.DISTINCT,       new TypeMapping(Types.DISTINCT,      "DISTINCT",      Object.class.getName()));
	    TYPE_MAPPING.put(Types.JAVA_OBJECT,    new TypeMapping(Types.JAVA_OBJECT,   "JAVA_OBJECT",   Object.class .getName()));
	    TYPE_MAPPING.put(Types.STRUCT,         new TypeMapping(Types.STRUCT,        "STRUCT",        Object.class.getName()));
	  
	    // 字符类型
	    TYPE_MAPPING.put(Types.CHAR,           new TypeMapping(Types.CHAR,          "CHAR",          String.class.getName()));
	    TYPE_MAPPING.put(Types.CLOB,           new TypeMapping(Types.CLOB,          "CLOB",          String.class.getName()));
	    TYPE_MAPPING.put(Types.LONGVARCHAR,    new TypeMapping(Types.LONGVARCHAR,   "LONGVARCHAR",   String.class.getName()));
	    TYPE_MAPPING.put(Types.NCHAR,          new TypeMapping(Types.NCHAR,         "NCHAR",         String.class.getName()));
	    TYPE_MAPPING.put(Types.NCLOB,          new TypeMapping(Types.NCLOB,         "NCLOB",         String.class.getName()));
	    TYPE_MAPPING.put(Types.NVARCHAR,       new TypeMapping(Types.NVARCHAR,      "NVARCHAR",      String.class.getName()));
	    TYPE_MAPPING.put(Types.LONGNVARCHAR,   new TypeMapping(Types.LONGNVARCHAR,  "LONGNVARCHAR",  String.class.getName()));
	    TYPE_MAPPING.put(Types.VARCHAR,        new TypeMapping(Types.VARCHAR,       "VARCHAR",       String.class.getName()));
	    
	    // 数字类型
	    TYPE_MAPPING.put(Types.TINYINT,        new TypeMapping(Types.TINYINT,       "TINYINT",       Byte.class.getName()));
	    TYPE_MAPPING.put(Types.SMALLINT,       new TypeMapping(Types.SMALLINT,      "SMALLINT",      Short.class.getName()));
	    TYPE_MAPPING.put(Types.REAL,           new TypeMapping(Types.REAL,          "REAL",          Float.class.getName()));
	    TYPE_MAPPING.put(Types.INTEGER,        new TypeMapping(Types.INTEGER,       "INTEGER",       Integer.class.getName()));
	    TYPE_MAPPING.put(Types.BIGINT,         new TypeMapping(Types.BIGINT,        "BIGINT",        Long.class.getName()));
	    TYPE_MAPPING.put(Types.DOUBLE,         new TypeMapping(Types.DOUBLE,        "DOUBLE",        Double.class.getName()));
	    TYPE_MAPPING.put(Types.FLOAT,          new TypeMapping(Types.FLOAT,         "FLOAT",         Double.class.getName()));
	    TYPE_MAPPING.put(Types.DECIMAL,        new TypeMapping(Types.DECIMAL,       "DECIMAL",       Double.class.getName())); // BigDecimal.class.getName()
	    TYPE_MAPPING.put(Types.NUMERIC,        new TypeMapping(Types.NUMERIC,       "NUMERIC",       Double.class.getName())); // BigDecimal.class.getName()
	    
	    // 字节类型
	    TYPE_MAPPING.put(Types.BINARY,         new TypeMapping(Types.BINARY,        "BINARY",        "byte[]"));
	    TYPE_MAPPING.put(Types.BLOB,           new TypeMapping(Types.BLOB,          "BLOB",          "byte[]"));
	    TYPE_MAPPING.put(Types.LONGVARBINARY,  new TypeMapping(Types.LONGVARBINARY, "LONGVARBINARY", "byte[]"));
	    TYPE_MAPPING.put(Types.VARBINARY,      new TypeMapping(Types.VARBINARY,     "VARBINARY",     "byte[]"));
	    
	    // 布尔类型
	    TYPE_MAPPING.put(Types.BIT,            new TypeMapping(Types.BIT,           "BIT",           Boolean.class.getName()));
	    TYPE_MAPPING.put(Types.BOOLEAN,        new TypeMapping(Types.BOOLEAN,       "BOOLEAN",       Boolean.class.getName()));
	    
	    // 日期类型
	    TYPE_MAPPING.put(Types.DATE,           new TypeMapping(Types.DATE,          "DATE",          Date.class.getName()));
	    TYPE_MAPPING.put(Types.TIME,           new TypeMapping(Types.TIME,          "TIME",          Date.class.getName()));
	    TYPE_MAPPING.put(Types.TIMESTAMP,      new TypeMapping(Types.TIMESTAMP,     "TIMESTAMP",     Date.class.getName()));
	    // JDK 1.8 types
	    TYPE_MAPPING.put(Types.TIME_WITH_TIMEZONE,      new TypeMapping(Types.TIME_WITH_TIMEZONE,      "TIME_WITH_TIMEZONE",      "java.time.OffsetTime")); 
	    TYPE_MAPPING.put(Types.TIMESTAMP_WITH_TIMEZONE, new TypeMapping(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP_WITH_TIMEZONE", "java.time.OffsetDateTime")); 
	
	    // 数据库特有的数据类型(神通数据库)  ----------------------------------------------------------------------------------
	    TYPE_MAPPING.put(-999,      new TypeMapping(-999,     "VARCHAR",     String.class.getName()));// -999 =====> TEXT 
	    // ROWID SQLXML REF_CURSOR 
	}
	
	static class TypeMapping {
		
		// JDBC 数据类型
		private Integer jdbcType;
		// JDBC 数据类型 - 字符串表现形式
		private String jdbcTypeName;
		// java数据类型短名称
		private String javaTypeShortName;
		// java 数据类型全名称
		private String javaTypeFullName;
		
		public TypeMapping() {
			
		}
		
		public TypeMapping(Integer jdbcType, String jdbcTypeName, String javaTypeFullName) {
			this.jdbcType = jdbcType;
			this.jdbcTypeName = jdbcTypeName;
			this.javaTypeFullName = javaTypeFullName;
			
			int pos = javaTypeFullName.lastIndexOf(".");
			// 如果 pos=-1, 则 pos+1=0, 截取全部字符串
			javaTypeShortName = javaTypeFullName.substring(pos+1);
		}

		public Integer getJdbcType() {
			return jdbcType;
		}

		public void setJdbcType(Integer jdbcType) {
			this.jdbcType = jdbcType;
		}

		public String getJdbcTypeName() {
			return jdbcTypeName;
		}

		public void setJdbcTypeName(String jdbcTypeName) {
			this.jdbcTypeName = jdbcTypeName;
		}

		public String getJavaTypeFullName() {
			return javaTypeFullName;
		}

		public void setJavaTypeFullName(String javaTypeFullName) {
			this.javaTypeFullName = javaTypeFullName;
		}

		public String getJavaTypeShortName() {
			return javaTypeShortName;
		}
		
		
	}
	
	/**
	 * 
	 * @author hexian
	 * 自定义排序器, 将主键列排在前面
	 */
	static class ColumnInfoComparator implements Comparator<ColumnInfo> {

		@Override
		public int compare(ColumnInfo c1, ColumnInfo c2) {
			if(null!=c1 && null!=c2) {
				Boolean p1 = c1.isPrimaryKey();
				Boolean p2 = c2.isPrimaryKey();
				
				// 比较的结果为: true 往前排, false 往后排
				return -p1.compareTo(p2);
			}
			
			return 0;
		} 
		
	}
	
}
