package com.otac.runner.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static List<String> getAllDirectoryName(final String path){
		File allDirectory = new File(path);
		List<String> allDirectoryList = new ArrayList<String>();
		
		for(File file : allDirectory.listFiles()){
			if(file.isDirectory()){
				allDirectoryList.add(file.getName());
			}
		}
		
		return allDirectoryList;
	}
	public static String readFile(final String filePath) throws IOException{
		StringBuilder fileContent = new StringBuilder();
		String sCurrentLine;
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		while ((sCurrentLine = br.readLine()) != null) {
			fileContent.append(sCurrentLine);
			fileContent.append("\n");
		}
		br.close();
		return fileContent.toString();
	}
	
	public static void writeToFile(final String filePath, final String content) throws IOException{
		
        final File output = new File(filePath);
        output.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(output);
        writer.write(content);
        writer.flush();
        writer.close();
	}
}
