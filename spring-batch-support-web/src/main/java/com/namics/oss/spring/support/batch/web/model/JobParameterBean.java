/*
 * Copyright 2000-2015 namics ag. All rights reserved.
 */

package com.namics.oss.spring.support.batch.web.model;

import com.namics.oss.spring.support.batch.model.ParameterType;

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
