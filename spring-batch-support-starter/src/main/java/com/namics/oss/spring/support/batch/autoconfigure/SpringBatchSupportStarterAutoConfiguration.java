package com.namics.oss.spring.support.batch.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * NamicsSpringBatchStarterAutoConfiguration.
 * Start point of this spring boot starter.
 *
 * @author lboesch, Namics AG
 * @since 20.08.17 15:53
 */
@Configuration
@Import({ SpringBatchSupportWebAutoConfiguration.class, AutomaticJobRegistrarConfigurationSupport.class, SpringBatchSupportCleanUpAutoConfiguration.class })
public class SpringBatchSupportStarterAutoConfiguration implements Ordered {

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
