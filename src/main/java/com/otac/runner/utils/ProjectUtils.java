package com.otac.runner.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.otac.runner.datamodel.DataModel;
import com.otac.runner.datamodel.Fields;
import com.otac.runner.datamodel.Project;

public class ProjectUtils {
	public static final String PROJECT_FILENAME = "/project.json";
	public static final String PROJECT_SRC = "/code/src/main/java";
	public static final String PROJECT_CODE = "/code";
	public static final String RESOURCE_NAME = "resources";
	public static final String UTILS_NAME = "utils";
	public static final String CONFIGURATION = "Configuration";
	public static final String HEALTH = "Health";
	public static final String ACCESS_MODIFIER = "private";
	public static final String OTAC_CODE_FILE = "utils/otac.code";
	public static final String METHOD_GETTER_KEY = "METHOD_GETTER";
	public static final String METHOD_SETTER_KEY = "METHOD_SETTER";
	public static final String VARIABLE_DECLARATION_KEY = "VARIABLE_DECLARATION";
	
	public static Project loadProjectFromFile(final String projectDirectory) throws IOException {
		String fileContent = FileUtils.readFile(projectDirectory.concat(PROJECT_FILENAME));
		return JsonUtils.unserializeProject(fileContent);
	}

	public static void createProjectFile(final Project project, final String filePath) throws IOException {
		String jsonContent = JsonUtils.serialize(project);
		FileUtils.writeToFile(filePath.concat(PROJECT_FILENAME), jsonContent);
	}
	
	public static String projectAbsolutePath(final String workspace, final String projectDIR, final String projectName){
		StringBuilder pathToProject = new StringBuilder(workspace);
		pathToProject.append(projectDIR);
		pathToProject.append("/");
		pathToProject.append(projectName);
		return pathToProject.toString();
	}

	public static void createDataModel(Project project, String pathToProject, final Properties otacCode) {
		File sourceDirectory = new File(pathToProject.concat(PROJECT_SRC));
		if(!sourceDirectory.exists() || !sourceDirectory.isDirectory()){
			sourceDirectory.mkdir();
		}

		String filePath = sourceDirectory.getAbsolutePath();
		
		// creating dataModel
		for(DataModel dataModel : project.getDataModels()){
			String dataModelClassCode = createCodeFromDataModel(otacCode, dataModel, "\n", "\t");
			String pathToClass = getPathToClassFromFullName(dataModel.getFullName());
			
			try {
				FileUtils.writeToFile(filePath.concat(pathToClass), dataModelClassCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// creating Resources
		for(DataModel dataModel : project.getDataModels()){
			String utilsPackage = appendFullName(project.getGroupId(), UTILS_NAME);
			String utilsClassName = dataModel.getName().concat("Utils");
			String utilsClassCode = createCodeForUtils(dataModel, utilsPackage, utilsClassName, "\n", "\t");
			
			utilsPackage = appendFullName(utilsPackage, utilsClassName);
			String pathToClass = getPathToClassFromFullName(utilsPackage);
			
			try {
				FileUtils.writeToFile(filePath.concat(pathToClass), utilsClassCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// creating Utils
		for(DataModel dataModel : project.getDataModels()){
			String resourcesPackage = appendFullName(project.getGroupId(), RESOURCE_NAME);
			String resourceClassName = dataModel.getName().concat("Resource");
			String resourceClassCode = createCodeForResources(dataModel, resourcesPackage, resourceClassName, "\n", "\t");
			
			resourcesPackage = appendFullName(resourcesPackage, resourceClassName);
			String pathToClass = getPathToClassFromFullName(resourcesPackage);
			
			try {
				FileUtils.writeToFile(filePath.concat(pathToClass), resourceClassCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void createMainServer(Project project, String pathToProject, final Properties otacCode) {
		File sourceDirectory = new File(pathToProject.concat(PROJECT_SRC));
		if(!sourceDirectory.exists() || !sourceDirectory.isDirectory()){
			sourceDirectory.mkdir();
		}

		String filePath = sourceDirectory.getAbsolutePath();
		
		// creating Main
		String mainPackage = project.getGroupId();
		String mainClassName = project.getName().concat("Server");
		mainClassName = getFancyVariableName(mainClassName);
		String mainClassCode = createCodeForMainClass(project, mainPackage, mainClassName, "\n", "\t");
			
		mainPackage = appendFullName(mainPackage, mainClassName);
		String pathToClass = getPathToClassFromFullName(mainPackage);
			
			try {
				FileUtils.writeToFile(filePath.concat(pathToClass), mainClassCode);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		
	}
	
	public static void createConfigurations(Project project, String pathToProject, final Properties otacCode) {
		File sourceDirectory = new File(pathToProject.concat(PROJECT_SRC));
		if(!sourceDirectory.exists() || !sourceDirectory.isDirectory()){
			sourceDirectory.mkdir();
		}

		String filePath = sourceDirectory.getAbsolutePath();
		
		// creating Configuration
		String configurationPackage = appendFullName(project.getGroupId(), CONFIGURATION.toLowerCase());
		String configurationClassName = project.getName().concat(CONFIGURATION);
		configurationClassName = getFancyVariableName(configurationClassName);
		String configurationClassCode = createCodeForConfiguration(project, configurationPackage, configurationClassName, "\n", "\t");
			
		configurationPackage = appendFullName(configurationPackage, configurationClassName);
		String pathToClass = getPathToClassFromFullName(configurationPackage);
			
			try {
				FileUtils.writeToFile(filePath.concat(pathToClass), configurationClassCode);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		
	}	
	
	public static void createHealthCheck(Project project, String pathToProject, final Properties otacCode) {
		File sourceDirectory = new File(pathToProject.concat(PROJECT_SRC));
		if(!sourceDirectory.exists() || !sourceDirectory.isDirectory()){
			sourceDirectory.mkdir();
		}

		String filePath = sourceDirectory.getAbsolutePath();
		
		// creating Configuration
		String healthPackage = appendFullName(project.getGroupId(), HEALTH.toLowerCase());
		String healthClassName = "template".concat(HEALTH);
		healthClassName = getFancyVariableName(healthClassName.concat("Check"));
		String healthClassCode = createCodeForHealthCheck(healthPackage, healthClassName, "\n", "\t");
			
		healthPackage = appendFullName(healthPackage, healthClassName);
		String pathToClass = getPathToClassFromFullName(healthPackage);
			
			try {
				FileUtils.writeToFile(filePath.concat(pathToClass), healthClassCode);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		
	}
	
	public static void createPom(Project project, String pathToProject, File samplePomFile) {
		File sourceDirectory = new File(pathToProject.concat(PROJECT_CODE));
		if(!sourceDirectory.exists() || !sourceDirectory.isDirectory()){
			sourceDirectory.mkdir();
		}

		String filePath = sourceDirectory.getAbsolutePath();
		String mainPackage = project.getGroupId();
		String mainClassName = project.getName().concat("Server");
		mainClassName = getFancyVariableName(mainClassName);
			
		mainPackage = appendFullName(mainPackage, mainClassName);
		
		try {
			String samplePomContent = FileUtils.readFile(samplePomFile.getAbsolutePath());
			samplePomContent = samplePomContent.replace("#GROUP_ID#", project.getGroupId());
			samplePomContent = samplePomContent.replace("#ARTIFACT_ID#", project.getArtifactId());
			samplePomContent = samplePomContent.replace("#VERSION#", project.getVersion());
			samplePomContent = samplePomContent.replace("#MAIN_CLASS#", mainPackage);
			
			FileUtils.writeToFile(filePath.concat("/pom.xml"), samplePomContent);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String createCodeForHealthCheck(String healthPackage, String healthClassName,
			String newLine, String tab) {
		
		StringBuilder code = new StringBuilder("package ");
		code.append(healthPackage);
		code.append(";");
		code.append(newLine);
		code.append(newLine);
		// adding dependency
		code.append("import com.codahale.metrics.health.HealthCheck;");
		code.append(newLine);	
		code.append(newLine);	

		code.append("public class ");	
		code.append(healthClassName);
		code.append(" extends HealthCheck {");
		code.append(newLine);		
		code.append(tab);

		code.append("private final String template;");
		code.append(newLine);		
		code.append(newLine);		
		code.append(tab);
		
		code.append("public TemplateHealthCheck(String template) {");
		code.append(newLine);		
		code.append(tab);
		code.append(tab);
		code.append("this.template = template;");
		code.append(newLine);		
		code.append(tab);
		code.append("}");
		code.append(newLine);

		code.append(newLine);
		code.append(tab);
		code.append("@Override");
		code.append(newLine);		
		code.append(tab);
		code.append("protected Result check() throws Exception {");
		code.append(newLine);		
		code.append(tab);		
		code.append(tab);
		code.append("final String saying = String.format(template, \"TEST\");");
		code.append(newLine);		
		code.append(tab);		
		code.append(tab);
		code.append("if (!saying.contains(\"TEST\")) {");
		code.append(newLine);		
		code.append(tab);		
		code.append(tab);
		code.append(tab);
		code.append("return Result.unhealthy(\"template doesn't include a name\");");
		code.append(newLine);		
		code.append(tab);		
		code.append(tab);
		code.append("}");
		code.append(newLine);		
		code.append(tab);		
		code.append(tab);
		code.append("return Result.healthy();");
		code.append(newLine);		
		code.append(tab);
		code.append("}");
		code.append(newLine);
		code.append(newLine);				
		code.append(tab);


		code.append("}");
				
		return code.toString();
	}

	private static String createCodeForConfiguration(Project project, String configurationPackage,
			String configurationClassName, String newLine, String tab) {
		
		StringBuilder code = new StringBuilder("package ");
		code.append(configurationPackage);
		code.append(";");
		code.append(newLine);
		code.append(newLine);
		// adding dependency
		code.append("import io.dropwizard.Configuration;");
		code.append(newLine);
		code.append("import com.fasterxml.jackson.annotation.JsonProperty;");
		code.append(newLine);
		code.append("import org.hibernate.validator.constraints.NotEmpty;");
		code.append(newLine);	
		code.append(newLine);	

		code.append("public class ");	
		code.append(configurationClassName);
		code.append(" extends Configuration {");
		code.append(newLine);		
		code.append(tab);

		code.append("");
		code.append(newLine);		
		code.append(tab);

		code.append("@NotEmpty");
		code.append(newLine);		
		code.append(tab);
		code.append("private String template;");
		code.append(newLine);		
		code.append(newLine);		
		code.append(tab);

		code.append("@JsonProperty");
		code.append(newLine);		
		code.append(tab);
		code.append("public String getTemplate() {");
		code.append(newLine);		
		code.append(tab);
		code.append(tab);
		code.append("return template;");
		code.append(newLine);		
		code.append(tab);
		code.append("}");
		code.append(newLine);
		code.append(newLine);				
		code.append(tab);

		code.append("@JsonProperty");
		code.append(newLine);		
		code.append(tab);
		code.append("public void setTemplate(String template) {");
		code.append(newLine);		
		code.append(tab);
		code.append(tab);
		code.append("this.template = template;");
		code.append(newLine);		
		code.append(tab);
		code.append("}");
		code.append(newLine);
		
		code.append("}");
				
		return code.toString();
	}

	private static String appendFullName(String groupId, String packageName){
		if(groupId == null){
			return StringUtils.EMPTY;
		}
		StringBuilder packageN = new StringBuilder(groupId);
		packageN.append(".");
		packageN.append(packageName);
		
		return packageN.toString();
	}
	private static String getPathToClassFromFullName(String name) {
		StringBuilder pathToClass = new StringBuilder("/");
		pathToClass.append(name.replaceAll("\\.", "/"));
		pathToClass.append(".java");
		
		return pathToClass.toString();
	}

	private static String createCodeForMainClass(Project project, String mainPackage,
			String mainClassName, String newLine, String tab) {

		String pathToResources = appendFullName(project.getGroupId(), RESOURCE_NAME);
		String pathToConfiguration = appendFullName(project.getGroupId(), CONFIGURATION.toLowerCase());
		String configurationClass = getFancyVariableName(project.getName()).concat(CONFIGURATION);
		pathToConfiguration = appendFullName(pathToConfiguration, configurationClass);

		String healthPackage = appendFullName(project.getGroupId(), HEALTH.toLowerCase());
		String healthClassName = "template".concat(HEALTH);
		healthClassName = getFancyVariableName(healthClassName.concat("Check"));
		healthPackage = appendFullName(healthPackage, healthClassName);
		
		StringBuilder addingResource = new StringBuilder();

		StringBuilder code = new StringBuilder("package ");
		code.append(mainPackage);
		code.append(";");
		code.append(newLine);
		code.append(newLine);
		// adding dependency
		code.append("import io.dropwizard.Application;");
		code.append(newLine);
		code.append("import io.dropwizard.setup.Bootstrap;");
		code.append(newLine);
		code.append("import io.dropwizard.setup.Environment;");
		code.append(newLine);
		code.append("import io.dropwizard.views.ViewBundle;");
		code.append(newLine);
		code.append("import ");
		code.append(healthPackage);
		code.append(";");

		code.append("import ");
		code.append(pathToConfiguration);
		code.append(";");
		code.append(newLine);

		for(DataModel dataModel : project.getDataModels()){
			String resourceClass = dataModel.getName().concat("Resource");			
			code.append(newLine);
			code.append("import  ");
			code.append(appendFullName(pathToResources,resourceClass));
			code.append(";");
			
			addingResource.append(tab);
			addingResource.append(tab);
			addingResource.append(resourceClass);
			addingResource.append(" ");
			addingResource.append(getFancyVariableNameLower(resourceClass));
			addingResource.append(" = new ");
			addingResource.append(resourceClass);
			addingResource.append("();");
			addingResource.append(newLine);
			addingResource.append(tab);
			addingResource.append(tab);
			addingResource.append("environment.jersey().register(");
			addingResource.append(getFancyVariableNameLower(resourceClass));
			addingResource.append(");");
			
			addingResource.append(newLine);
			addingResource.append(newLine);
		}
		code.append(newLine);		
		code.append(newLine);	

		code.append("public class ");	
		code.append(mainClassName);
		code.append(" extends Application<");
		code.append(configurationClass);
		code.append("> {");	
		code.append(newLine);		
		code.append(tab);	
		code.append("public static void main(String[] args) throws Exception {");
		code.append(newLine);		
		code.append(tab);	
		code.append(tab);	
		code.append("new ");
		code.append(mainClassName);
		code.append("().run(args);");
		code.append(newLine);		
		code.append(tab);	
		code.append("}");	
		
		code.append(newLine);		
		code.append(tab);	
		code.append("@Override");
		code.append(newLine);		
		code.append(tab);	
		code.append("public String getName() {");
		code.append(newLine);		
		code.append(tab);	
		code.append(tab);	
		code.append("return \"");	
		code.append(project.getName());
		code.append("\";");	
		code.append(newLine);		
		code.append(tab);		
		code.append("}");

		code.append(newLine);	
		code.append(newLine);		
		code.append(tab);	
		code.append("@Override");
		code.append(newLine);		
		code.append(tab);	
		code.append("public void initialize(Bootstrap<");
		code.append(configurationClass);
		code.append("> bootstrap) {");
		code.append(newLine);		
		code.append(tab);	
		code.append(tab);	
		code.append("bootstrap.addBundle(new ViewBundle<");	
		code.append(configurationClass);
		code.append(">());");	
		code.append(newLine);		
		code.append(tab);		
		code.append("}");	

		code.append(newLine);	
		code.append(newLine);		
		code.append(tab);	
		code.append("@Override");
		code.append(newLine);		
		code.append(tab);	
		code.append("public void run(");
		code.append(configurationClass);
		code.append(" configuration, Environment environment) {");
		code.append(newLine);
		
        
		addingResource.append(tab);
		addingResource.append(tab);
        addingResource.append("final ");
        addingResource.append(healthClassName);
        addingResource.append(" healthCheck = new ");
        addingResource.append(healthClassName);
        addingResource.append("(configuration.getTemplate());");
        addingResource.append("environment.healthChecks().register(\"template\", healthCheck);");
		code.append(addingResource.toString());
		
		code.append(newLine);
		code.append(tab);
		code.append("}");
		code.append(newLine);
		code.append("}");
		
		return code.toString();
	}
	
	private static String createCodeForResources(DataModel dataModel, String packageName, String resourceName, String newLine, String tab) {

		StringBuilder code = new StringBuilder("package ");
		code.append(packageName);
		code.append(";");
		code.append(newLine);
		code.append(newLine);
		// adding dependency
		code.append("import javax.ws.rs.GET;");
		code.append(newLine);
		code.append("import javax.ws.rs.POST;");
		code.append(newLine);
		code.append("import javax.ws.rs.Path;");
		code.append(newLine);
		code.append("import javax.ws.rs.Produces;");
		code.append(newLine);
		code.append("import javax.ws.rs.core.MediaType;");
		code.append(newLine);
		code.append("import javax.ws.rs.core.Response;");
		code.append(newLine);
		code.append(newLine);		
		code.append("import ");
		code.append(dataModel.getFullName());
		code.append(";");
		code.append(newLine);
		code.append(newLine);

		code.append("@Path(\"/");
		code.append(dataModel.getName().toLowerCase());
		code.append("\")");		
		code.append(newLine);
		
		code.append("public class ");
		code.append(resourceName);
		code.append("{");
		code.append(newLine);

		code.append(newLine);
		code.append(tab);
		code.append("@GET");
		code.append(newLine);
		code.append(tab);
		code.append("@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})");
		code.append(newLine);
		code.append(tab);
		code.append("public Response get");
		code.append(dataModel.getName());
		code.append("() {");
		code.append(newLine);
		code.append(tab);
		code.append(tab);

		code.append(dataModel.getName());
		code.append(" ");
		code.append(dataModel.getName().toLowerCase());
		code.append(" = new ");
		code.append(dataModel.getName());
		code.append("();");
		code.append(newLine);
		code.append(tab);
		code.append(tab);
		code.append("// do anything with ");
		code.append(dataModel.getName().toLowerCase());
		
		code.append(newLine);
		code.append(tab);
		code.append(tab);
		code.append("return Response.ok(");
		code.append(dataModel.getName().toLowerCase());
		code.append(").build();");
		code.append(newLine);
		code.append(tab);
		code.append("}");

		code.append(newLine);
		code.append(tab);
		code.append("@POST");
		code.append(newLine);
		code.append(tab);
		code.append("public Response create");
		code.append(dataModel.getName());
		code.append("(final ");
		code.append(dataModel.getName());
		code.append(" ");
		code.append(dataModel.getName().toLowerCase());
		code.append(") {");
		code.append(newLine);
		code.append(tab);
		code.append(tab);

		code.append("// do anything with ");
		code.append(dataModel.getName().toLowerCase());
		
		code.append(newLine);
		code.append(tab);
		code.append(tab);
		code.append("return Response.ok().build();");
		code.append(newLine);
		code.append(tab);
		code.append("}");

		
		code.append(newLine);
		code.append("}");
		return code.toString();
	}


	private static String createCodeForUtils(DataModel dataModel, String packageName, String utilsName,
			final String newLine, final String tab) {

		StringBuilder code = new StringBuilder("package ");
		code.append(packageName);
		code.append(";");
		code.append(newLine);
		code.append(newLine);
		// adding dependency
		code.append("import ");
		code.append(dataModel.getFullName());
		code.append(";");
		code.append(newLine);
		code.append(newLine);

		
		code.append("public class ");
		code.append(utilsName);
		code.append("{");
		code.append(newLine);
		code.append(tab);
		code.append("// write your code here for utils");		
		code.append(newLine);
		code.append("}");
		
		return code.toString();
	}

	public static String createCodeFromDataModel(final Properties otacCode, final DataModel dataModel, final String newLine, final String tab){
		char tabb = tab.charAt(0);
		String fullName = dataModel.getFullName();
		
		StringBuilder code = new StringBuilder("package ");
		code.append(fullName.substring(0, fullName.lastIndexOf('.')));
		code.append(";");
		code.append(newLine);
		code.append(newLine);
		code.append("public class ");
		code.append(dataModel.getName());
		code.append("{");
		
		// adding Variable
		for(final Fields field1 : dataModel.getFields()){
			final String otacVariableBluePrint = otacCode.get(VARIABLE_DECLARATION_KEY).toString().replace('>', tabb);		
			code.append(String.format(otacVariableBluePrint, field1.getType(), field1.getName()));
		}
		
		// adding Variable getter setter
		for(final Fields field2 : dataModel.getFields()){
			final String dataType = field2.getType();
			final String variableName = field2.getName();
			final String fancyVariableName = getFancyVariableName(variableName);
			
			// getter
			final String otacGetMethodBluePrint = otacCode.get(METHOD_GETTER_KEY).toString().replace('>', tabb);		
			code.append(String.format(otacGetMethodBluePrint, dataType, fancyVariableName, variableName));
			
			// setter
			final String otacSetMethodBluePrint = otacCode.get(METHOD_SETTER_KEY).toString().replace('>', tabb);			
			code.append(String.format(otacSetMethodBluePrint, fancyVariableName, dataType, variableName, variableName, variableName));
			
		}	

		code.append(newLine);
		code.append("}");
		
		return code.toString();
	}


	private static String getFancyVariableName(String variableName) {
		StringBuilder fancyVariableName = new StringBuilder();
		fancyVariableName.append(variableName.substring(0, 1).toUpperCase());
		fancyVariableName.append(variableName.substring(1));
	
		return fancyVariableName.toString();
	}
	private static String getFancyVariableNameLower(String variableName) {
		StringBuilder fancyVariableName = new StringBuilder();
		fancyVariableName.append(variableName.substring(0, 1).toLowerCase());
		fancyVariableName.append(variableName.substring(1));
	
		return fancyVariableName.toString();
	}
}
