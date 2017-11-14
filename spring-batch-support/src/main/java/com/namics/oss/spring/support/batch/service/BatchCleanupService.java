package com.namics.oss.spring.support.batch.service;

/**
 * BatchCleanupService.
 *
 * @author lboesch, Namics AG
 * @since 24.08.17 16:11
 */
public interface BatchCleanupService {

	/**
	 * deletes old job executions with the context data.
	 *
	 * @return amount of deleted job executions.
	 */
	int cleanBatchTables();
}
