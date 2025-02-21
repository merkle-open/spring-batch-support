package com.namics.oss.spring.support.batch.model;

public class Parameter {
	private final String key;
	private final String value;
	private final ParameterType parameterType;

	public Parameter(String key, String value, ParameterType parameterType) {
		this.key = key;
		this.value = value;
		this.parameterType = parameterType;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}
}
