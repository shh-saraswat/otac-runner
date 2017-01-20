package com.otac.runner.utils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otac.runner.datamodel.Project;

import java.io.IOException;

/**
 * Json Utils
 * 
 * <P> Utility class that ease Json serialization/un-serialization
 * 
 */
public final class JsonUtils {
	
	// Utility class, though no constructor
	private JsonUtils() {}

	/**
	 * Json content type id
	 */
	public static final String JSON_CONTENT_TYPE = "application/json";

	/**
	 * Serialize an object with Json
	 * @param obj Object
	 * @return String
	 * @throws IOException 
	 */
	public static String serialize(final Object obj) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
		return mapper.writeValueAsString(obj);
		
	}

    /**
     * Un-serialize a Json into ProjectElemnt
     * @param projectElement String
     * @return ProjectElements
     * @throws IOException
     */
    public static Project unserializeProject(final String jsonContent) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        return mapper.readValue(jsonContent, Project.class);
    }
	
}
