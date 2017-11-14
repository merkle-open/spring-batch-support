/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.processor;

import com.namics.oss.spring.convert.Converter;
import org.springframework.batch.item.ItemProcessor;

import static org.springframework.util.Assert.notNull;

/**
 * Processor that uses a converter object to convert the item from one type to another.
 *
 * @param <Source> Source type to be converted to a target object
 * @param <Target> Target type of result object as result of processing step
 * @author aschaefer, Namics AG
 * @see Converter
 * @since 27.02.14 16:13
 */
public class ConverterItemProcessor<Source, Target> implements ItemProcessor<Source, Target> {

	protected Converter<Source, Target> converter;

	/**
	 * Create a processor using a converter.
	 *
	 * @param converter Converter to use to process the item, must not be null.
	 */
	public ConverterItemProcessor(Converter<Source, Target> converter) {
		notNull(converter, "converter is required.");
		this.converter = converter;
	}

	/**
	 * Transform item into target type using a {@link Converter} object to convert the item from one type to another.
	 *
	 * @param item to be processed
	 * @return transformed object
	 * @throws Exception on error
	 */
	@Override
	public Target process(Source item) throws Exception {
		return converter.convert(item);
	}
}
