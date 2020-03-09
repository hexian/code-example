package com.example.genarator.config;

public class DBConfig {
	
	private String dirverClass;
	
	private String url;
	
	private String username;
	
	private String password;
	
	private String schema;
	
	public DBConfig() {
		super();
	}

	public String getDirverClass() {
		return dirverClass;
	}

	public void setDirverClass(String dirverClass) {
		this.dirverClass = dirverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	

}
