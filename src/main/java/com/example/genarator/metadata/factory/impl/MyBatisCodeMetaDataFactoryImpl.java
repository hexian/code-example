package com.example.genarator.metadata.factory.impl;

import com.example.genarator.config.CodeConfig;
import com.example.genarator.metadata.factory.CodeMetaDataFactory;

/**
 * 
 * @author hexian
 * CodeMetaData工厂类默认实现 可以考虑使用单例来生成此类
 */
public class MyBatisCodeMetaDataFactoryImpl implements CodeMetaDataFactory {
	
	private static final MyBatisCodeMetaDataFactoryImpl instance = new MyBatisCodeMetaDataFactoryImpl();

	private CodeConfig codeConfig;
	
    private MyBatisCodeMetaDataFactoryImpl() {
    	
    }

    public static CodeMetaDataFactory getInstance(CodeConfig codeConfig) {
    	instance.codeConfig = codeConfig;
        return instance;
    }
	
    @Override
	public String[] getTrimTablePrefix() {
		return codeConfig.getTablePrefix();
	}
    
	/**
	 * 返回 Repository 接口的包名
	 */
	@Override
	public String genRepositoryBasePackageName(String tableName) {
		return ".mapper";
	}
	
	/**
	 * 生成 Repository 的策略
	 */
	@Override
	public String genRepositoryName(String tableName) {
		if(tableName==null) {
			return null;
		}
		
		// 最终的 repositoryName 名称
		String repositoryName = "I" + genEntityName(tableName) + "Mapper";
		
		return repositoryName;
	}
	
}
