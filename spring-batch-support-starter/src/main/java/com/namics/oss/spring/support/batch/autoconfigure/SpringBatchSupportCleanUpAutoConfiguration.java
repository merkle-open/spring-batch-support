package com.namics.oss.spring.support.batch.autoconfigure;

import com.namics.oss.spring.support.batch.service.BatchCleanupService;
import com.namics.oss.spring.support.batch.service.impl.BatchCleanupServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import static com.namics.oss.spring.support.batch.autoconfigure.SpringBatchSupportProperties.CleanUp.NAMICS_SPRING_BATCH_PROPERTIES_CLEAN_UP;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.StringUtils.hasText;

/**
 * SpringBatchCleanUpAutoConfiguration.
 * configure a cleanup task to clean spring batch tables with an @Scheduled method.
 *
 * @author lboesch, Namics AG
 * @since 25.08.2017 08:50
 */
@Configuration
@ConditionalOnProperty(name = NAMICS_SPRING_BATCH_PROPERTIES_CLEAN_UP + ".enabled", havingValue = "true")
@ConditionalOnBean({ DataSource.class, TaskScheduler.class })
@ConditionalOnMissingBean(BatchCleanupService.class)
@EnableConfigurationProperties(SpringBatchSupportProperties.class)
public class SpringBatchSupportCleanUpAutoConfiguration {

	private final String defaultCronExpression = "0/10 0/1 * 1/1 * ? *";

	@Inject
	protected SpringBatchSupportProperties springBatchSupportProperties;

	@Inject
	protected DataSource dataSource;

	@Inject
	protected TaskScheduler taskScheduler;

	@Bean
	public BatchCleanupService batchCleanupService() {
		int keepDays = springBatchSupportProperties.getCleanUp().getKeepDays();
		isTrue(keepDays >= 0, "keepDays property has to be greater or equal than 0");
		return new BatchCleanupServiceImpl(keepDays, new JdbcTemplate(dataSource));
	}

	@PostConstruct
	public void addCleanupAsScheduledCronTask() {
		//add cleanup of batch tables as a simple scheduled task to avoid problems with interfering table while cleanup.
		taskScheduler.schedule(batchCleanupService()::cleanBatchTables, new CronTrigger(getCornScheduling()));
	}

	protected String getCornScheduling() {
		if (hasText(springBatchSupportProperties.getCleanUp().getCron())) {
			return springBatchSupportProperties.getCleanUp().getCron();
		}
		return defaultCronExpression;
	}

}
