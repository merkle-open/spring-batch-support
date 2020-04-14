package com.namics.oss.spring.support.batch.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringBatchSupportProperties.
 *
 * @author lboesch, Namics AG
 * @since 21.08.2017 11:00
 */
@ConfigurationProperties(prefix = SpringBatchSupportProperties.NAMICS_SPRING_BATCH_PROPERTIES_PREFIX)
public class SpringBatchSupportProperties {

	public static final String NAMICS_SPRING_BATCH_PROPERTIES_PREFIX = "com.namics.oss.spring.support.batch";

	private Web web = new Web();

	private CleanUp cleanUp = new CleanUp();

	private JobConfiguration jobConfiguration = new JobConfiguration();

	public CleanUp getCleanUp() {
		return cleanUp;
	}

	public void setCleanUp(CleanUp cleanUp) {
		this.cleanUp = cleanUp;
	}

	public Web getWeb() {
		return web;
	}

	public void setWeb(Web web) {
		this.web = web;
	}


	public JobConfiguration getJobConfiguration() {
		return jobConfiguration;
	}

	public void setJobConfiguration(JobConfiguration jobConfiguration) {
		this.jobConfiguration = jobConfiguration;
	}

	public static class JobConfiguration {
		public static final String NAMICS_SPRING_BATCH_PROPERTIES_JOB_CONFIG = NAMICS_SPRING_BATCH_PROPERTIES_PREFIX + ".job-configuration";

		/**
		 * package used to scan for @Configuration classes which holds job configs.
		 */
		private String packageName;

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
	}

	public static class Web {

		public static final String NAMICS_SPRING_BATCH_PROPERTIES_WEB = NAMICS_SPRING_BATCH_PROPERTIES_PREFIX + ".web";

		/**
		 * the servlet-name
		 */
		private String servletName;

		/**
		 * the  mapping
		 */
		private String servletMapping;

		/**
		 * if darkmode is enabled in ui.
		 */
		private boolean darkMode = false;

		public String getServletName() {
			return servletName;
		}

		public void setServletName(String servletName) {
			this.servletName = servletName;
		}

		public String getServletMapping() {
			return servletMapping;
		}

		public void setServletMapping(String servletMapping) {
			this.servletMapping = servletMapping;
		}

		public boolean isDarkMode() {
			return darkMode;
		}

		public void setDarkMode(boolean darkMode) {
			this.darkMode = darkMode;
		}
	}

	public static class CleanUp {

		public static final String NAMICS_SPRING_BATCH_PROPERTIES_CLEAN_UP = NAMICS_SPRING_BATCH_PROPERTIES_PREFIX + ".clean-up";

		/**
		 * cleanup of batch tables enabled. defaults to false.
		 */
		private boolean enabled;

		/**
		 * days to keep job executions before deletion. defaults to 10 days.
		 */
		private int keepDays = 10;

		/**
		 * cron expression for the scheduling of the clean up task. defaults to 1x/hour.
		 */
		private String cron = "0/10 0/1 * 1/1 * ?";

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public int getKeepDays() {
			return keepDays;
		}

		public void setKeepDays(int keepDays) {
			this.keepDays = keepDays;
		}
	}
}
