package com.otac.runner.datamodel;

public class GitEntry {
	private String gitRepo;
	private String projectName;
	
	public GitEntry(String gitRepo, String projectName) {
		this.gitRepo = gitRepo;
		this.projectName = projectName;
	}
	public String getGitRepo() {
		return gitRepo;
	}
	public void setGitRepo(String gitRepo) {
		this.gitRepo = gitRepo;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
