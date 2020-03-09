package com.example.genarator.metadata.factory.impl;

import com.example.genarator.config.CodeConfig;
import com.example.genarator.metadata.factory.CodeMetaDataFactory;

/**
 * 
 * @author hexian
 * CodeMetaData工厂类默认实现 可以考虑使用单例来生成此类
 */
public class JpaCodeMetaDataFactoryImpl implements CodeMetaDataFactory {
	
	private static final JpaCodeMetaDataFactoryImpl instance = new JpaCodeMetaDataFactoryImpl();

	private CodeConfig codeConfig;
	
    private JpaCodeMetaDataFactoryImpl() {
    	
    }

    public static CodeMetaDataFactory getInstance(CodeConfig codeConfig) {
    	instance.codeConfig = codeConfig;
        return instance;
    }
    
    @Override
	public String[] getTrimTablePrefix() {
		return codeConfig.getTablePrefix();
	}
	
}
