package com.namics.oss.spring.support.batch.autoconfigure;

import com.namics.oss.spring.support.batch.config.TaskExecutorBatchConfigurer;
import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.service.impl.JobServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.SystemPropertyUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Class.forName;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.StringUtils.hasText;

/**
 * AutomaticJobRegistrarConfigurationSupport.
 * This configuration looks for jobs in a modular fashion, meaning that every job configuration file gets its own
 * Child-ApplicationContext.
 *
 * @author lboesch, Namics AG
 * @since 03.07.17 15:38
 */
@Configuration
@EnableConfigurationProperties(SpringBatchSupportProperties.class)
@ConditionalOnMissingBean({ SpringBatchSupportAutoConfiguration.class })
@Import(TaskExecutorBatchConfigurer.class)
@EnableBatchProcessing(modular = true)
public class AutomaticJobRegistrarConfigurationSupport extends SpringBatchSupportAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutomaticJobRegistrarConfigurationSupport.class);

	private static final String DEFAULT_PACKAGE_TO_SCAN = "spring.batch.jobs";

	@Inject
	protected SpringBatchSupportProperties springBatchSupportProperties;

	@Inject
	protected PlatformTransactionManager transactionManager;


	@Override
	protected void addApplicationContextFactories(AutomaticJobRegistrar automaticJobRegistrar) throws Exception {
		registerJobsFromJavaConfig(automaticJobRegistrar);
	}


	protected void registerJobsFromJavaConfig(AutomaticJobRegistrar automaticJobRegistrar) throws ClassNotFoundException, IOException {
		List<Class<?>> classes = findMyTypes(getDefaultPackageToScanForBatchJobs());
		for (Class<?> clazz : classes) {
			LOGGER.info("Register jobs from {}", clazz);
			automaticJobRegistrar.addApplicationContextFactory(new GenericApplicationContextFactory(clazz));
		}
	}

	protected List<Class<?>> findMyTypes(String basePackage) throws IOException, ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		List<Class<?>> candidates = new ArrayList<>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
		                           + "/" + "**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (isCandidate(metadataReader)) {
					candidates.add(forName(metadataReader.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	protected String resolveBasePackage(String basePackage) {
		return convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	protected boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException {
		try {
			Class<?> c = forName(metadataReader.getClassMetadata().getClassName());
			if (c.getAnnotation(Configuration.class) != null) {
				return true;
			}
		} catch (Throwable e) {
			LOGGER.debug("error {}", e);
		}
		return false;
	}

	protected String getDefaultPackageToScanForBatchJobs() {
		if (hasText(springBatchSupportProperties.getJobConfiguration().getPackageName())) {
			return springBatchSupportProperties.getJobConfiguration().getPackageName();
		}
		return DEFAULT_PACKAGE_TO_SCAN;
	}


	@Bean
	@ConditionalOnMissingBean(JobRepository.class)
	public JobRepository batchSimpleJobRepository(DataSource dataSource) throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.afterPropertiesSet();
		return factory.getObject();
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

}
