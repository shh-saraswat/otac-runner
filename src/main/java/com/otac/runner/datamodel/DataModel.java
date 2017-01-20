package com.otac.runner.datamodel;

import java.util.ArrayList;
import java.util.List;

public class DataModel{
	private String name;
	private String fullName;
	private List<Fields> fields;
	
	public DataModel(){
		fields = new ArrayList<Fields>();
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(final String name){
		this.name = name;
	}
	public String getFullName(){
		return this.fullName;
	}
	public void setFullName(final String fullName){
		this.fullName = fullName;
	}
	public List<Fields> getFields(){
		return this.fields;
	}
	public void setFields(final List<Fields> fields){
		this.fields = fields;
	}
	public void addField(final Fields fields){
		this.fields.add(fields);
	}
	public void update(final DataModel dataModel){
		this.name = dataModel.getName();
		this.fullName = dataModel.getFullName();
		this.fields = dataModel.getFields();		
	}
}