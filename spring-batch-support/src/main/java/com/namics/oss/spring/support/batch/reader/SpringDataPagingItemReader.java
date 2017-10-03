/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.util.Assert.notNull;

/**
 * Reader to read items in a paged manner from a {@link PagingAndSortingRepository}.
 * Implementation is stateful and writes data directly to protected properties due to ugly abstract class.
 *
 * @author aschaefer
 * @see org.springframework.batch.item.database.AbstractPagingItemReader
 * @see org.springframework.data.repository.PagingAndSortingRepository
 * @since 06.02.14 15:05
 */
public class SpringDataPagingItemReader<T> extends AbstractPagingItemReader<T> {

	private static final Logger LOG = LoggerFactory.getLogger(SpringDataPagingItemReader.class);

	protected PagingAndSortingRepository<T, ?> repository;
	protected Class<T> currentItemClass = null;


	/**
	 * Reader to read items in a paged manner from a {@link PagingAndSortingRepository}.
	 * Implementation is stateful and writes data directly to protected properties due to ugly abstract class.
	 *
	 * @param repository repository to use for reading pages, must not be null.
	 */
	public SpringDataPagingItemReader(PagingAndSortingRepository<T, ?> repository) {
		notNull(repository, "Repository must not be null.");
		this.repository = repository;
	}

	/**
	 * Implementation of reading of a page.
	 */
	@Override
	protected void doReadPage() {
		LOG.info("Read page {} ", getPage());
		if (this.results == null) {
			this.results = new CopyOnWriteArrayList<>();
		} else {
			this.results.clear();
		}
		Page<T> page = repository.findAll(new PageRequest(getPage(), getPageSize()));
		if (LOG.isInfoEnabled() && page.iterator().hasNext()) {
			T item = page.iterator().next();
			currentItemClass = (Class<T>) item.getClass();
		}
		LOG.info("Read {} {}", page.getNumber(), currentItemClass);
		this.results.addAll(page.getContent());
	}

	/**
	 * No custom implementation is required.
	 */
	@Override
	protected void doJumpToPage(int itemIndex) {
		// no operation required
	}

}
