package com.otac.runner.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.otac.runner.datamodel.Project;
import com.otac.runner.datamodel.GitEntry;
import com.otac.runner.utils.FileUtils;
import com.otac.runner.utils.ProjectUtils;

public class WorkspaceHandler {
	private String workspace;
	public static final String PROJECT_DIRECTORY = "/project";
	
	public WorkspaceHandler(final String workspace){
		this.workspace = workspace;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	
	public List<String> getAllProjectName(){
		StringBuilder pathToProjects = new StringBuilder(workspace);
		pathToProjects.append(PROJECT_DIRECTORY);
		return FileUtils.getAllDirectoryName(pathToProjects.toString());
	}
	
	public Project getProject(final String projectName){
		try {
			return ProjectUtils.loadProjectFromFile(ProjectUtils.projectAbsolutePath(workspace, PROJECT_DIRECTORY, projectName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean createGitEntryFile(List<GitEntry> gitEntries){
//		try{
//			WorkspaceUtils.createGitEntryFile(gitEntries, workspace);
//		}catch(IOException e){
//			return false;
//		}
		return true;
	}
	
	public boolean developProject(final Project project){
		if(project == null){
			return false;
		}
		
		Properties otacCode = new Properties();
		
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			otacCode.load(classLoader.getResourceAsStream(ProjectUtils.OTAC_CODE_FILE));
		} catch (IOException e) {
			
		}
		
		String pathToProject = workspace;
		
		ProjectUtils.createDataModel(project, pathToProject, otacCode);
		ProjectUtils.createMainServer(project, pathToProject, otacCode);
		ProjectUtils.createConfigurations(project, pathToProject, otacCode);
		ProjectUtils.createHealthCheck(project, pathToProject, otacCode);

		ClassLoader classLoader = this.getClass().getClassLoader();
		File samplePomFile = new File(classLoader.getResource("utils/samplePom.xml").getFile());

		ProjectUtils.createPom(project, pathToProject, samplePomFile);
		
		return true;		
	}
	
	public boolean createProjectFile(final Project project){
		File projectDIR = new File(ProjectUtils.projectAbsolutePath(workspace, PROJECT_DIRECTORY, project.getName()));
		if(!projectDIR.exists() || !projectDIR.isDirectory()){
			projectDIR.mkdir();
		}
		try{
			ProjectUtils.createProjectFile(project, ProjectUtils.projectAbsolutePath(workspace, PROJECT_DIRECTORY, project.getName()));
		}catch(IOException e){
			return false;
		}
		return true;
	}
}
