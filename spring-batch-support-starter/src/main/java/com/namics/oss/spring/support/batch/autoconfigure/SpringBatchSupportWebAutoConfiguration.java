package com.namics.oss.spring.support.batch.autoconfigure;

import com.namics.oss.spring.support.batch.web.config.SpringBatchSupportWebServletConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.inject.Inject;

import static org.springframework.util.StringUtils.hasText;

/**
 * NamicsConfigurationWebAutoConfiguration.
 * add servlet for spring batch ui if spring-batch-support-web is available.
 *
 * @author lboesch, Namics AG
 * @since 21.08.2017 08:50
 */
@Configuration
@ConditionalOnClass(SpringBatchSupportWebServletConfig.class)
@EnableConfigurationProperties(SpringBatchSupportProperties.class)
public class SpringBatchSupportWebAutoConfiguration {

	protected static final String defaultServletMapping = "/batch/*";
	protected static final String defaultServletName = "batchJobServlet";

	@Inject
	protected SpringBatchSupportProperties springBatchSupportProperties;

	@Bean
	public ServletRegistrationBean springBatchSupportServlet() {

		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(SpringBatchSupportWebServletConfig.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setApplicationContext(applicationContext);

		ServletRegistrationBean registrationBean = new ServletRegistrationBean(dispatcherServlet, getServletMapping());
		registrationBean.setName(getServletName());
		registrationBean.setLoadOnStartup(1);
		return registrationBean;
	}

	@Bean("springBatchSupportViewMode")
	public String springBatchSupportViewMode() {
		return getIsDarkModeEnabled() ? "darkMode" : "default";
	}

	protected String getServletMapping() {
		if (hasText(springBatchSupportProperties.getWeb().getServletMapping())) {
			return springBatchSupportProperties.getWeb().getServletMapping();
		}
		return defaultServletMapping;
	}

	protected String getServletName() {
		if (hasText(springBatchSupportProperties.getWeb().getServletName())) {
			return springBatchSupportProperties.getWeb().getServletName();
		}
		return defaultServletName;
	}

	protected boolean getIsDarkModeEnabled() {
		return springBatchSupportProperties.getWeb().isDarkMode();
	}


}
