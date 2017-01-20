package com.otac.runner.datamodel;

public class Backend{
	private String host;
	private String port;
	private String type;
	private String userName;
	private String password;
	private String dbName;
	
	public String getHost(){
		return this.host;
	}
	public void setHost(final String host){
		this.host = host;
	}
	public String getPort(){
		return this.port;
	}
	public void setPort(final String port){
		this.port = port;
	}
	public String getType(){
		return this.type;
	}
	public void setType(final String type){
		this.type = type;
	}
	public String getUserName(){
		return this.userName;
	}
	public void setUserName(final String userName){
		this.userName = userName;
	}
	public String getPassword(){
		return this.password;
	}
	public void setPassword(final String password){
		this.password = password;
	}
	public String getDbName(){
		return this.dbName;
	}
	public void setDbName(final String dbName){
		this.dbName = dbName;
	}
}
