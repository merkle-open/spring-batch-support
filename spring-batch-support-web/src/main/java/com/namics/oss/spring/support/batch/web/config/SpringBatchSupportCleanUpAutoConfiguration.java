package com.namics.oss.spring.support.batch.web.config;

import com.namics.oss.spring.support.batch.service.BatchCleanupService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import javax.sql.DataSource;

import static com.namics.oss.spring.support.batch.web.config.SpringBatchSupportProperties.CleanUp.SPRING_BATCH_PROPERTIES_CLEAN_UP;
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
@ConditionalOnProperty(name = SPRING_BATCH_PROPERTIES_CLEAN_UP + ".enabled", havingValue = "true")
@ConditionalOnBean({ DataSource.class, TaskScheduler.class })
@ConditionalOnMissingBean(BatchCleanupService.class)
public class SpringBatchSupportCleanUpAutoConfiguration {
	private final static String DEFAULT_CRON_EXPRESSION = "0/10 0/1 * 1/1 * ? *";

	private final SpringBatchSupportProperties springBatchSupportProperties;
	private final DataSource dataSource;
	private final TaskScheduler taskScheduler;

	@Inject
	public SpringBatchSupportCleanUpAutoConfiguration(
			SpringBatchSupportProperties springBatchSupportProperties,
			DataSource dataSource,
			TaskScheduler taskScheduler
	) {
        this.springBatchSupportProperties = springBatchSupportProperties;
        this.dataSource = dataSource;
		this.taskScheduler = taskScheduler;
	}

	@Bean
	public BatchCleanupService batchCleanupService() {
		int keepDays = springBatchSupportProperties.getCleanUp().getKeepDays();
		isTrue(keepDays >= 0, "keepDays property has to be greater or equal than 0");
		return new BatchCleanupService(keepDays, new JdbcTemplate(dataSource));
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
		return DEFAULT_CRON_EXPRESSION;
	}

}
