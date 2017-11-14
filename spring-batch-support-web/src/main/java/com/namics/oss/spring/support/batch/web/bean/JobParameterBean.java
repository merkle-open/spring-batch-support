/*
 * Copyright 2000-2015 namics ag. All rights reserved.
 */

package com.namics.oss.spring.support.batch.web.bean;

import com.namics.oss.spring.support.batch.model.ParameterType;

/**
 * JobParameterBean.
 *
 * @author lboesch, Namics AG
 * @since 01.05.2015
 */
public class JobParameterBean {

	protected String name;
	protected String value;
	protected ParameterType parameterType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JobParameterBean name(String name) {
		setName(name);
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public JobParameterBean value(String value) {
		setValue(value);
		return this;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public JobParameterBean parameterType(ParameterType parameterType) {
		setParameterType(parameterType);
		return this;
	}
}
