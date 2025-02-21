/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.model;

/**
 * Parameter.
 *
 * @author lboesch, Namics AG
 * @since 10.04.2014
 */
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
