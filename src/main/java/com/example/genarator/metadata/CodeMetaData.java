package com.example.genarator.metadata;

/**
 * 
 * @author hexian
 * 从表转换成代码生成的元数据信息
 *
 */
public class CodeMetaData {
	
	/**
	 * 包的基础路径
	 */
	private String basePackageName;
	
	
	
	/**
	 * entity的简单名称
	 */
	private String entityName;
	
	/**
	 * entity的全包名称
	 */
	private String entityFullPackage;
	
	/**
	 * entity的全路径名称
	 */
	private String entityFullName;

	
	
	/**
	 * repository的简单名称
	 */
	private String repositoryName;
	
	/**
	 * repository的全包名称
	 */
	private String repositoryFullPackage;
	
	/**
	 * repository的全路径名称
	 */
	private String repositoryFullName;
	
	
	
	/**
	 * service的简单名称
	 */
	private String serviceName;
	
	/**
	 * service的全包名称
	 */
	private String serviceFullPackage;
	
	/**
	 * service的全路径名称
	 */
	private String serviceFullName;
	
	
	
	/**
	 * serviceImpl的简单名称
	 */
	private String serviceImplName;
	
	/**
	 * serviceImpl的全包名称
	 */
	private String serviceImplFullPackage;
	
	/**
	 * serviceImpl的全路径名称
	 */
	private String serviceImplFullName;
	
	
	
	/**
	 * controller的简单名称
	 */
	private String controllerName;
	
	/**
	 * controller的全包名称
	 */
	private String controllerFullPackage;
	
	/**
	 * controller的全路径名称
	 */
	private String controllerFullName;
	

	public String getBasePackageName() {
		return basePackageName;
	}

	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityFullPackage() {
		return entityFullPackage;
	}

	public void setEntityFullPackage(String entityFullPackage) {
		this.entityFullPackage = entityFullPackage;
	}

	public String getEntityFullName() {
		return entityFullName;
	}

	public void setEntityFullName(String entityFullName) {
		this.entityFullName = entityFullName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getRepositoryFullPackage() {
		return repositoryFullPackage;
	}

	public void setRepositoryFullPackage(String repositoryFullPackage) {
		this.repositoryFullPackage = repositoryFullPackage;
	}

	public String getRepositoryFullName() {
		return repositoryFullName;
	}

	public void setRepositoryFullName(String repositoryFullName) {
		this.repositoryFullName = repositoryFullName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceFullPackage() {
		return serviceFullPackage;
	}

	public void setServiceFullPackage(String serviceFullPackage) {
		this.serviceFullPackage = serviceFullPackage;
	}

	public String getServiceFullName() {
		return serviceFullName;
	}

	public void setServiceFullName(String serviceFullName) {
		this.serviceFullName = serviceFullName;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public void setServiceImplName(String serviceImplName) {
		this.serviceImplName = serviceImplName;
	}

	public String getServiceImplFullPackage() {
		return serviceImplFullPackage;
	}

	public void setServiceImplFullPackage(String serviceImplFullPackage) {
		this.serviceImplFullPackage = serviceImplFullPackage;
	}

	public String getServiceImplFullName() {
		return serviceImplFullName;
	}

	public void setServiceImplFullName(String serviceImplFullName) {
		this.serviceImplFullName = serviceImplFullName;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getControllerFullPackage() {
		return controllerFullPackage;
	}

	public void setControllerFullPackage(String controllerFullPackage) {
		this.controllerFullPackage = controllerFullPackage;
	}

	public String getControllerFullName() {
		return controllerFullName;
	}

	public void setControllerFullName(String controllerFullName) {
		this.controllerFullName = controllerFullName;
	}

	@Override
	public String toString() {
		String br = "\n";
		return "CodeMetaData [" + br + 
					"  basePackageName=         " + basePackageName + br + 
					"  entityName=              " + entityName + br + 
					"  entityFullPackage=       " + entityFullPackage + br + 
					"  entityFullName=          " + entityFullName + br + 
					"  repositoryName=          " + repositoryName + br + 
					"  repositoryFullPackage=   " + repositoryFullPackage + br + 
					"  repositoryFullName=      " + repositoryFullName + br + 
					"  serviceName=             " + serviceName + br + 
					"  serviceFullPackage=      " + serviceFullPackage + br + 
					"  serviceFullName=         " + serviceFullName + br + 
					"  serviceImplName=         " + serviceImplName + br + 
					"  serviceImplFullPackage=  " + serviceImplFullPackage + br + 
					"  serviceImplFullName=     " + serviceImplFullName + br + 
					"  controllerName=          " + controllerName + br + 
					"  controllerFullPackage=   " + controllerFullPackage + br + 
					"  controllerFullName=      " + controllerFullName + br + 
				"]";
	}
	
	
}
