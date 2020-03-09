package com.example.genarator.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeetlTemplateConfig {
	
	private static final Logger LOG = LoggerFactory.getLogger(BeetlTemplateConfig.class);
	
	private BeetlTemplateConfig() {
		
	}
	
	public static GroupTemplate jpaGroupTemplate() {
		String templatesPath = "beetl/jpa";
		return groupTemplate(templatesPath);
	}
	
	public static GroupTemplate mybatisGroupTemplate() {
		String templatesPath = "beetl/mybatis";
		return groupTemplate(templatesPath);
	}

	public static GroupTemplate mybatisPlusGroupTemplate() {
		String templatesPath = "beetl/mp";
		return groupTemplate(templatesPath);
	}
	
	private static GroupTemplate groupTemplate(String templatesPath) {
		GroupTemplate gt = null;
		Configuration cfg = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(loader, templatesPath);
		try {
			cfg = Configuration.defaultConfiguration();
		} catch (IOException e) {
			LOG.error("获取模板引擎失败", e);
		}
		gt = new GroupTemplate(resourceLoader, cfg);
		
		return gt;
	}

	public static void main(String[] args) throws IOException {
		GroupTemplate gt = jpaGroupTemplate();
		
		Map<String, Object> sharedVars = new HashMap<>();
		gt.setSharedVars(sharedVars);
		sharedVars.put("sharedName", "hexian");
		
		Template t = gt.getTemplate("hello.btl");
		String str = t.render();
		LOG.info(str);
	}
}
