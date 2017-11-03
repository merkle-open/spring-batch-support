package com.namics.oss.spring.support.batch.autoconfigure;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.DefaultJobLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
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
@Import({ SpringBatchSupportWebAutoConfiguration.class, SpringBatchDefaultServiceConfiguration.class, SpringBatchSupportCleanUpAutoConfiguration.class, AutomaticJobRegistrarConfigurationSupport.class })
public class SpringBatchSupportStarterAutoConfiguration implements Ordered, ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Bean
	public AutomaticJobRegistrar jobRegistrar(JobRegistry jobRegistry) throws Exception {
		AutomaticJobRegistrar registrar = new AutomaticJobRegistrar();
		registrar.setJobLoader(new DefaultJobLoader(jobRegistry));
		for (ApplicationContextFactory factory : applicationContext.getBeansOfType(ApplicationContextFactory.class).values()) {
			registrar.addApplicationContextFactory(factory);
		}
		return registrar;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
