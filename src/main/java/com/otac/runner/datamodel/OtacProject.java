package com.otac.runner.datamodel;

import java.util.Map;

public class OtacProject {
	private Map<String, Map<String, String>> actors;

	public Map<String, Map<String, String>>  getActors() {
		return actors;
	}

	public void setActors(Map<String, Map<String, String>>  actors) {
		this.actors = actors;
	}
}
