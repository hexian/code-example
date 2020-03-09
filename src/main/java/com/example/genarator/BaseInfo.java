package com.example.genarator;

import java.util.List;

/**
 * 
 * @author hexian
 *     实体类、接口、服务类的父类与父接口
 */
public class BaseInfo {

	
	private String entityParentClass;
	
	private String entityParentInterfaces;
	
	private List<String> repositoryParentInterfaces;
	
	private List<String> serviceParentInterfaces;
	
	private String serviceImplParentClass;
	
	private List<String> serviceImplParentInterfaces;

	public String getEntityParentClass() {
		return entityParentClass;
	}

	public void setEntityParentClass(String entityParentClass) {
		this.entityParentClass = entityParentClass;
	}

	public String getEntityParentInterfaces() {
		return entityParentInterfaces;
	}

	public void setEntityParentInterfaces(String entityParentInterfaces) {
		this.entityParentInterfaces = entityParentInterfaces;
	}

	public List<String> getRepositoryParentInterfaces() {
		return repositoryParentInterfaces;
	}

	public void setRepositoryParentInterfaces(List<String> repositoryParentInterfaces) {
		this.repositoryParentInterfaces = repositoryParentInterfaces;
	}

	public List<String> getServiceParentInterfaces() {
		return serviceParentInterfaces;
	}

	public void setServiceParentInterfaces(List<String> serviceParentInterfaces) {
		this.serviceParentInterfaces = serviceParentInterfaces;
	}

	public String getServiceImplParentClass() {
		return serviceImplParentClass;
	}

	public void setServiceImplParentClass(String serviceImplParentClass) {
		this.serviceImplParentClass = serviceImplParentClass;
	}

	public List<String> getServiceImplParentInterfaces() {
		return serviceImplParentInterfaces;
	}

	public void setServiceImplParentInterfaces(List<String> serviceImplParentInterfaces) {
		this.serviceImplParentInterfaces = serviceImplParentInterfaces;
	}
	
	
}
