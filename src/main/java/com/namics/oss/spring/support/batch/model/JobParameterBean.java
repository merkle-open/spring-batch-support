package com.namics.oss.spring.support.batch.model;

public class JobParameterBean {
	private final String name;
	private final String value;
	private final ParameterType parameterType;

	public JobParameterBean(String name, String value, ParameterType parameterType) {
		this.name = name;
		this.value = value;
		this.parameterType = parameterType;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}
}
