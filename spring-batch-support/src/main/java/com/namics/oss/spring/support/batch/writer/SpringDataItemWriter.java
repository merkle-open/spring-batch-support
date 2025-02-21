/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * Writer to persist items using a spring data {@link org.springframework.data.repository.CrudRepository}.
 *
 * @author aschaefer
 * @since 06.02.14 15:31
 */
public class SpringDataItemWriter<T> implements ItemWriter<T> {
	private static final Logger LOG = LoggerFactory.getLogger(SpringDataItemWriter.class);
	private final CrudRepository<T, ?> repository;

	/**
	 * Writer to persist items using a spring data {@link org.springframework.data.repository.CrudRepository}.
	 *
	 * @param repository repository to use for persistence, must not be null
	 */
	public SpringDataItemWriter(CrudRepository<T, ?> repository) {
		notNull(repository, "spring data repository required.");
		this.repository = repository;
	}

	/**
	 * Persist items
	 *
	 * @param items items to be persisted.
	 */
	@Override
	public void write(Chunk<? extends T> items) {
		LOG.info("Write {} {} ", items.size(), items.iterator().hasNext() ? items.iterator().next().getClass() : "");
		if (LOG.isTraceEnabled()) {
			for (T item : items) {
				LOG.trace("write item {}", item);
			}
		}
		this.repository.save(items);
	}
}
