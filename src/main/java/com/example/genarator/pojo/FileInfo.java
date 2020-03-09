package com.example.genarator.pojo;

/**
 * 
 * @author hexian
 * 需要保存的文件
 */
public class FileInfo {
	
	/**
	 * 文件包名称
	 */
	private String packageName;
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 文件类型: java、xml
	 */
	private String fileType;
	
	/**
	 * 文件内容
	 */
	private String content;
	
	/**
	 * 表名称
	 */
	private String tbName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFileName() { 
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}
	
	public boolean isEmptyFile() {
		return isEmpty(packageName) || isEmpty(fileName) ||isEmpty(fileType) ||isEmpty(content);
	}
	
	private boolean isEmpty(String str) {
		return str==null || "".equals(str.trim());
	}

	@Override
	public String toString() {
		return "FileInfo [packageName=" + packageName + ", fileName=" + fileName + ", fileType=" + fileType + ", congtent=" + content + ", tbName=" + tbName + "]";
	}
	
}
