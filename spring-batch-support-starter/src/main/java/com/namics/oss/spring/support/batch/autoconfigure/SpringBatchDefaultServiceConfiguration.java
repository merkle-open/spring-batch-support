package com.namics.oss.spring.support.batch.autoconfigure;

import com.namics.oss.spring.support.batch.config.TaskExecutorBatchConfigurer;
import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.service.impl.JobServiceImpl;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * SpringBatchDefaultServiceConfiguration.
 *
 * @author lboesch, Namics AG
 * @since 03.11.17 09:09
 */
@Configuration
@Import({ TaskExecutorBatchConfigurer.class })
public class SpringBatchDefaultServiceConfiguration {
	protected AutomaticJobRegistrar registrar = new AutomaticJobRegistrar();

	@Inject
	protected ApplicationContext context;

	@Inject
	protected List<BatchConfigurer> batchConfigurers;


	@Inject
	protected DataSource dataSource;

	@Inject
	protected PlatformTransactionManager transactionManager;

	@Bean
	@ConditionalOnMissingBean(JobRepository.class)
	public JobRepository getJobRepository() throws Exception {
		BatchConfigurer batchConfigurer = getBatchConfigurer();
		if (batchConfigurer != null) {
			return batchConfigurer.getJobRepository();
		}
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.afterPropertiesSet();
//		factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
		return factory.getObject();
	}

	@Bean
	@ConditionalOnMissingBean(JobLauncher.class)
	public JobLauncher getJobLauncher() throws Exception {
		BatchConfigurer batchConfigurer = getBatchConfigurer();
		if (batchConfigurer != null) {
			return batchConfigurer.getJobLauncher();
		}
		return null;
	}

	@Bean
	@ConditionalOnMissingBean(JobLauncher.class)
	public JobExplorer getJobExplorer() throws Exception {
		BatchConfigurer batchConfigurer = getBatchConfigurer();
		if (batchConfigurer != null) {
			return batchConfigurer.getJobExplorer();
		}
		JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
		jobExplorerFactoryBean.setDataSource(dataSource);
		return jobExplorerFactoryBean.getObject();
	}

	@Bean
	@ConditionalOnMissingBean(JobOperator.class)
	public JobOperator jobOperator(JobExplorer jobExplorer, JobLauncher jobLauncher, ListableJobLocator jobRegistry, JobRepository jobRepository) throws Exception {
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobExplorer(jobExplorer);
		jobOperator.setJobLauncher(jobLauncher);
		jobOperator.setJobRegistry(jobRegistry);
		jobOperator.setJobRepository(jobRepository);
		return jobOperator;
	}

	@Bean
	@ConditionalOnMissingBean(JobService.class)
	public JobService jobService(JobOperator batchJobOperator,
	                             JobRegistry batchJobRegistry,
	                             JobExplorer jobExplorer,
	                             JobLauncher jobLauncher,
	                             JobRepository jobRepository) throws Exception {
		return new JobServiceImpl(jobExplorer, batchJobOperator, jobLauncher, batchJobRegistry, jobRepository);
	}


	@Bean
	@ConditionalOnMissingBean(JobBuilderFactory.class)
	public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository) throws Exception {
		return new JobBuilderFactory(jobRepository);
	}

	@Bean
	@ConditionalOnMissingBean(StepBuilderFactory.class)
	public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository) throws Exception {
		return new StepBuilderFactory(
				jobRepository, transactionManager);
	}


	@Bean
	@ConditionalOnMissingBean(MapJobRegistry.class)
	public MapJobRegistry batchJobRegistry() {
		return new MapJobRegistry();
	}


	@Bean
	@ConditionalOnMissingBean(StepScope.class)
	public static StepScope stepScope() {
		StepScope stepScope = new StepScope();
		stepScope.setAutoProxy(false);
		return stepScope;
	}

	protected BatchConfigurer getBatchConfigurer() {
		if (isEmpty(this.batchConfigurers)) {
			return null;
		}
		return this.batchConfigurers.get(0);
	}
}
