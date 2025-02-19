package com.namics.oss.spring.support.batch.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringBatchSupportProperties.
 *
 * @author lboesch, Namics AG
 * @since 21.08.2017 11:00
 */
@ConfigurationProperties(prefix = SpringBatchSupportProperties.SPRING_BATCH_PROPERTIES_PREFIX)
public class SpringBatchSupportProperties {
	public static final String SPRING_BATCH_PROPERTIES_PREFIX = "com.namics.oss.spring.support.batch";
	private Web web = new Web();
	private CleanUp cleanUp = new CleanUp();

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

	public static class Web {
		public static final String SPRING_BATCH_PROPERTIES_WEB = SPRING_BATCH_PROPERTIES_PREFIX + ".web";
		private String servletName = "batchJobServlet";
		private String servletMapping = "/batch/*";
		private boolean darkMode;

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
		public static final String SPRING_BATCH_PROPERTIES_CLEAN_UP = SPRING_BATCH_PROPERTIES_PREFIX + ".clean-up";
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
