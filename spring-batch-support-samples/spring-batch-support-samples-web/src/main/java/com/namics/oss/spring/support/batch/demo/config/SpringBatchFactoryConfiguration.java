package com.namics.oss.spring.support.batch.demo.config;

import com.namics.oss.spring.support.batch.demo.job.JobConfig;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBatchFactoryConfiguration.
 *
 * @author lboesch, Namics AG
 * @since 30.08.17 10:35
 */
@Configuration
public class SpringBatchFactoryConfiguration {

	@Bean
	public ApplicationContextFactory job1() {
		return new GenericApplicationContextFactory(JobConfig.class);
	}


}
