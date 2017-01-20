package com.otac.runner.datamodel;

import java.util.Map;

public class OtacActor {
	private Map<String, Object> attributes;

	public Map<String, Object> getActors() {
		return attributes;
	}

	public void setActors(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
