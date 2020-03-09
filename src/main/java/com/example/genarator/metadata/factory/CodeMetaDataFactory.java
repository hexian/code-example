package com.example.genarator.metadata.factory;

import java.util.Arrays;
import java.util.List;

import com.example.genarator.kit.CamelizeKit;
import com.example.genarator.metadata.CodeMetaData;

/**
 * 
 * @author hexian
 * CodeMetaData工厂类
 */
public interface CodeMetaDataFactory {
	
	// 排除那些表(一般用来排除与数据库系统相关的表)
	List<String> EXCLUDE_TABLE = Arrays.asList("".replace("\\s{0,}", "").trim().split(","));
	
	// 去掉表前缀
	public String[] getTrimTablePrefix();
	
	/**
	 * <pre>
	 * 可以在这个方法中写排除不需要的表等逻辑
	 * 
	 * @param tableName 传入表的名称
	 * @return 返回true表示当前表可以用于代码生成,
	 * 返回false表示当前表不可以用于代码生成
	 * 
	 * </pre>
	 */
	public default boolean includeTable(String tableName) {
		if(tableName == null || "".equals(tableName.trim()) || EXCLUDE_TABLE.contains(tableName)) {
		   return false;	
		}
		/**
		String[] names = tableName.split("_");
		if(names.length >= 3) {
			return true;
		}
		*/
		return true;
	}
	
	/**
	 * 生成元数据
	 */
 	public default CodeMetaData genCodeMetaData(String tableName, String basePackageName) {
		CodeMetaData metaData = null;
		
		// 返回false则跳过当前表转类
		if(includeTable(tableName)) {
			// 获取基础包名称
			String packageName = genBasePackageName(basePackageName, tableName);
			// 去掉前面的 '.'
			if(packageName.startsWith(".")) {
			   packageName = packageName.substring(1);
			}
			// 去掉后后面的 '.'
			if(packageName.endsWith(".")) {
			   packageName = packageName.substring(0, packageName.length()-1);
			}
			
			
			
			String entityBasePackageName = genEntityBasePackageName(tableName);
			// 添加前缀
			if(!entityBasePackageName.startsWith(".")) {
			   entityBasePackageName = "." + entityBasePackageName;
			}
			// 添加后缀
			if(!entityBasePackageName.endsWith(".")) {
			   entityBasePackageName = entityBasePackageName + ".";
			}
			
			// entity 简单名称
			String entityName = genEntityName(tableName);
			// 去掉前面的 '.'
			if(entityName.startsWith(".")) {
			   entityName = entityName.substring(1);
			}
			// 去掉后后面的 '.'
			if(entityName.endsWith(".")) {
			   entityName = entityName.substring(0, entityName.length()-1);
			}
			
			// entity 的全名称
			String entityFullName = packageName +entityBasePackageName + entityName;
			// entity 的全包名称
			String entityFullPackage = packageName +entityBasePackageName;
			entityFullPackage = entityFullPackage.substring(0, entityFullPackage.length()-1);
			
			
			
			String repositoryBasePackageName = genRepositoryBasePackageName(tableName);
			// 添加前缀
			if(!repositoryBasePackageName.startsWith(".")) {
				repositoryBasePackageName = "." + repositoryBasePackageName;
			}
			// 添加后缀
			if(!repositoryBasePackageName.endsWith(".")) {
				repositoryBasePackageName = repositoryBasePackageName + ".";
			}
			// repository 简单名称
			String repositoryName = genRepositoryName(tableName);
			// 去掉前面的 '.'
			if(repositoryName.startsWith(".")) {
				repositoryName = repositoryName.substring(1);
			}
			// 去掉后后面的 '.'
			if(repositoryName.endsWith(".")) {
				repositoryName = repositoryName.substring(0, repositoryName.length()-1);
			}
			// repository 的全名称
			String repositoryFullName = packageName +repositoryBasePackageName + repositoryName;
			// repository 的全包名称
			String repositoryFullPackage = packageName +repositoryBasePackageName;
			repositoryFullPackage = repositoryFullPackage.substring(0, repositoryFullPackage.length()-1);
			
			
			
			String serviceBasePackageName = genServiceBasePackageName(tableName);
			// 添加前缀
			if(!serviceBasePackageName.startsWith(".")) {
				serviceBasePackageName = "." + serviceBasePackageName;
			}
			// 添加后缀
			if(!serviceBasePackageName.endsWith(".")) {
				serviceBasePackageName = serviceBasePackageName + ".";
			}
			// service 简单名称
			String serviceName = genServiceName(tableName);
			// 去掉前面的 '.'
			if(serviceName.startsWith(".")) {
				serviceName = serviceName.substring(1);
			}
			// 去掉后后面的 '.'
			if(serviceName.endsWith(".")) {
				serviceName = serviceName.substring(0, serviceName.length()-1);
			}
			// service 的全名称
			String serviceFullName = packageName + serviceBasePackageName + serviceName;
			// service 的全包名称
			String serviceFullPackage = packageName +serviceBasePackageName;
			serviceFullPackage = serviceFullPackage.substring(0, serviceFullPackage.length()-1);
			
			
			
			String serviceImplBasePackageName = genServiceImplBasePackageName(tableName);
			// 添加前缀
			if(!serviceImplBasePackageName.startsWith(".")) {
				serviceImplBasePackageName = "." + serviceImplBasePackageName;
			}
			// 添加后缀
			if(!serviceImplBasePackageName.endsWith(".")) {
				serviceImplBasePackageName = serviceImplBasePackageName + ".";
			}
			// service 简单名称
			String serviceImplName = genServiceImplName(tableName);
			// 去掉前面的 '.'
			if(serviceImplName.startsWith(".")) {
				serviceImplName = serviceImplName.substring(1);
			}
			// 去掉后后面的 '.'
			if(serviceImplName.endsWith(".")) {
				serviceImplName = serviceImplName.substring(0, serviceImplName.length()-1);
			}
			// serviceImpl 的全名称
			String serviceImplFullName = packageName + serviceImplBasePackageName + serviceImplName;
			// serviceImpl 的全包名称
			String serviceImplFullPackage = packageName +serviceImplBasePackageName;
			serviceImplFullPackage = serviceImplFullPackage.substring(0, serviceImplFullPackage.length()-1);
			
			
			
			String controllerBasePackageName = genControllerBasePackageName(tableName);
			// 添加前缀
			if(!controllerBasePackageName.startsWith(".")) {
				controllerBasePackageName = "." + controllerBasePackageName;
			}
			// 添加后缀
			if(!controllerBasePackageName.endsWith(".")) {
				controllerBasePackageName = controllerBasePackageName + ".";
			}
			// controller 简单名称
			String controllerName = genControllerName(tableName);
			// 去掉前面的 '.'
			if(controllerName.startsWith(".")) {
				controllerName = controllerName.substring(1);
			}
			// 去掉后后面的 '.'
			if(controllerName.endsWith(".")) {
				controllerName = controllerName.substring(0, controllerName.length()-1);
			}	
			
			// controller 的全名称
			String controllerFullName = packageName + controllerBasePackageName + controllerName;
			// serviceImpl 的全包名称
			String controllerFullPackage = packageName +controllerBasePackageName;
			controllerFullPackage = controllerFullPackage.substring(0, controllerFullPackage.length()-1);
			
			
			
			
			metaData = new CodeMetaData();
			metaData.setBasePackageName(packageName);
			
			metaData.setEntityName(entityName);
			metaData.setEntityFullPackage(entityFullPackage);
			metaData.setEntityFullName(entityFullName);
			
			metaData.setRepositoryName(repositoryName);
			metaData.setRepositoryFullPackage(repositoryFullPackage);
			metaData.setRepositoryFullName(repositoryFullName);
			
			metaData.setServiceName(serviceName);
			metaData.setServiceFullPackage(serviceFullPackage);
			metaData.setServiceFullName(serviceFullName);
			
			metaData.setServiceImplName(serviceImplName);
			metaData.setServiceImplFullPackage(serviceImplFullPackage);
			metaData.setServiceImplFullName(serviceImplFullName);
			
			metaData.setControllerName(controllerName);
			metaData.setControllerFullPackage(controllerFullPackage);
			metaData.setControllerFullName(controllerFullName);
		}
		
		return metaData;
	}
	
	
	
	// ============   默认方法实现    ==========================================================================
	/**
	 * 返回 Entity 实体类的包名
	 */
	public default String genEntityBasePackageName(String tableName) {
		return ".entity";
	}
	
	/**
	 * 返回 Repository 接口的包名
	 */
	public default String genRepositoryBasePackageName(String tableName) {
		return ".repository";
	}
	
	/**
	 * 返回 Service 接口的包名
	 */
	public default String genServiceBasePackageName(String tableName) {
		return ".service";
	}
	
	/**
	 * 返回 ServiceImpl 接口类的包名
	 */
	public default String genServiceImplBasePackageName(String tableName) {
		return ".service.impl";
	}
	
	/**
	 * 返回 controller 接口类的包名
	 */
	public default String genControllerBasePackageName(String tableName) {
		return ".controller";
	}
	
	/**
	 * 获取包的基本名称
	 */
	public default String genBasePackageName(String packagePrefixName ,String tableName) {
		if(!packagePrefixName.endsWith(".")) {
			packagePrefixName = packagePrefixName + ".";
		}
		String packageName = packagePrefixName + trimTablePrefix(tableName).toLowerCase().replace("_", "");
		
		return packageName;
	}
	
	/**
	 * 生成 Entity 的策略
	 */
	public default String genEntityName(String tableName) {
		if(tableName==null) {
			return null;
		}
		
		// 生成实体的策略
		// 1.去掉前缀
		String trimPrefixTableName = tableName;
		String[] trimTablePrefix = getTrimTablePrefix();
		if(trimTablePrefix!=null && trimTablePrefix.length>0) {
		   trimPrefixTableName = trimTablePrefix(tableName);
		} 
		
		// 2.下划线转驼峰
		String entityName = CamelizeKit.underLine2Camelize(trimPrefixTableName);
		// 3.实体名称
		entityName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
		
		return entityName;
	}
	
	/**
	 * 不使用replace, 改为使用substring
	 * @param tableName
	 * @return
	 */
	public default String trimTablePrefix(String tableName) {
		String trimPrefixTableName = tableName;
		String[] trimTablePrefix = getTrimTablePrefix();
		if(trimTablePrefix!=null && trimTablePrefix.length>0) {
			String upperCaseTableName = tableName.toUpperCase();
			for(String prefix : trimTablePrefix) {
				prefix = prefix.toUpperCase();
				if(upperCaseTableName.startsWith(prefix)) {
				   upperCaseTableName = upperCaseTableName.substring(prefix.length());
				}
			}
			
			// 如果是以 _ 开头
			if(upperCaseTableName.startsWith("_")) {
				upperCaseTableName = upperCaseTableName.substring(1);
			}
			// 如果是以 _ 结尾开头
			if(upperCaseTableName.endsWith("_")) {
				upperCaseTableName = upperCaseTableName.substring(0, upperCaseTableName.length()-1);
			}
			
			trimPrefixTableName = upperCaseTableName;
		} 
		
		return trimPrefixTableName;
	}
	
	/**
	 * 生成 Repository 的策略
	 */
	public default String genRepositoryName(String tableName) {
		if(tableName==null) {
			return null;
		}
		
		// 最终的 repositoryName 名称
		String repositoryName = /** "I"  + */ genEntityName(tableName) + "Repository";
		
		return repositoryName;
	}

	/**
	 * 生成 Service 接口的策略
	 */
	public default String genServiceName(String tableName) {
		if(tableName==null) {
			return null;
		}
		
		// 最终的 serviceName 名称
		String serviceName = /**"I" + */genEntityName(tableName) + "Service";
		
		return serviceName;
	}
	
	/**
	 * 生成 Service 接口实现类的策略
	 */
	public default String genServiceImplName(String tableName) {
		if(tableName==null) {
			return null;
		}
		
		// 最终的 serviceName 名称
		String serviceImplName = genEntityName(tableName) + "ServiceImpl";
		
		return serviceImplName;
	}
	
	/**
	 * 生成 Controller 接口实现类的策略
	 */
	public default String genControllerName(String tableName) {
		if(tableName==null) {
			return null;
		}
		
		// 最终的 serviceName 名称
		String serviceImplName = genEntityName(tableName) + "Controller";
		
		return serviceImplName;
	}
	
}
