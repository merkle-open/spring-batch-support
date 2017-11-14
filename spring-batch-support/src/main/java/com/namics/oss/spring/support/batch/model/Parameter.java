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
	protected String key;
	protected String value;
	protected ParameterType parameterType;

	public String getKey() {
		return key;
	}

	public Parameter setKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Parameter setValue(String value) {
		this.value = value;
		return this;
	}


	public Parameter value(String value) {
		setValue(value);
		return this;
	}

	public Parameter key(String key) {
		setKey(key);
		return this;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public Parameter parameterType(ParameterType parameterType) {
		setParameterType(parameterType);
		return this;
	}
}
