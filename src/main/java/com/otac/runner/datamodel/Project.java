package com.otac.runner.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Project{
	private String name;
	private String groupId;
	private String artifactId;
	private String version;
	private String packaging;
	private Backend backend;
	private List<DataModel> dataModels;
	
	public Project(){
		dataModels = new ArrayList<DataModel>();
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(final String name){
		this.name = name;
	}
	public String getGroupId(){
		return this.groupId;
	}
	public void setGroupId(final String groupId){
		this.groupId = groupId;
	}
	public String getArtifactId(){
		return this.artifactId;
	}
	public void setArtifactId(final String artifactId){
		this.artifactId = artifactId;
	}
	public String getVersion(){
		return this.version;
	}
	public void setVersion(final String version){
		this.version = version;
	}
	public String getPackaging(){
		return this.packaging;
	}
	public void setPackaging(final String packaging){
		this.packaging = packaging;
	}
	public Backend getBackend(){
		return this.backend;
	}
	public void setBackend(final Backend backend){
		this.backend = backend;
	}
	public List<DataModel> getDataModels() {
		return dataModels;
	}
	public void setDataModels(List<DataModel> dataModels) {
		this.dataModels = dataModels;
	}
	public void addDataModel(DataModel dataModel) {
		this.dataModels.add(dataModel);
	}
	public String getArtfifactFileName(){
		StringBuilder artifactFile = new StringBuilder(this.artifactId);
		artifactFile.append("-");
		artifactFile.append(this.version);
		artifactFile.append(".jar");
		return artifactFile.toString();		
	}
	public DataModel getDataModelByFullName(final String name){
		if(name.isEmpty()){
			return null;
		}
		try{
			for(DataModel dataModel : dataModels){
				if(dataModel.getFullName().equals(name)){
					return dataModel;
				}
			}		
		}catch(Exception e){
			return null;			
		}
		return null;
	}
	public void updateFixedValues(){
		artifactId = name.replace(' ', '-').toLowerCase();
		version = "0.0.1-SNAPSHOT";
		groupId = String.format("com.otac.%s", artifactId);
		packaging = "jar";
		
		for(DataModel dataModel: dataModels){
			final String fullName = String.format("%s.datamodel.%s", groupId, dataModel.getName());
			dataModel.setFullName(fullName);
		}
	}
}