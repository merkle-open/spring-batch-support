package com.namics.oss.spring.support.batch.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.namics.oss.spring.support.batch.config.BatchConfig;
import com.namics.oss.spring.support.batch.web.ui.DarkModeTransformer;

import jakarta.inject.Inject;

@Configuration
@EnableConfigurationProperties(SpringBatchSupportProperties.class)
@Import({
		SpringBatchSupportCleanUpAutoConfiguration.class,
		BatchConfig.class
})
@ComponentScan(
		basePackages = {
				"com.namics.oss.spring.support.batch",
		},
		includeFilters = {
				@ComponentScan.Filter(Component.class),
				@ComponentScan.Filter(Service.class),
				@ComponentScan.Filter(Repository.class),
				@ComponentScan.Filter(RestController.class),
		}
)
public class SpringBatchSupportWebConfiguration implements WebMvcConfigurer {
    private final SpringBatchSupportProperties springBatchSupportProperties;

    @Inject
	public SpringBatchSupportWebConfiguration(
			final SpringBatchSupportProperties springBatchSupportProperties
	) {
        this.springBatchSupportProperties = springBatchSupportProperties;
    }

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:overview.html");
	}

	@Bean
	public ServletRegistrationBean<DispatcherServlet> springBatchSupportServlet() {
		final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(SpringBatchSupportWebConfiguration.class);

		final DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setApplicationContext(applicationContext);

		final ServletRegistrationBean<DispatcherServlet> registrationBean = new ServletRegistrationBean<>(
				dispatcherServlet,
				springBatchSupportProperties.getWeb().getServletMapping()
		);
		registrationBean.setName(springBatchSupportProperties.getWeb().getServletName());
		registrationBean.setLoadOnStartup(1);
		return registrationBean;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/*.html")
				.addResourceLocations("classpath:/META-INF/spring-batch/terrific/assets/")
				.resourceChain(true)
				.addTransformer(new DarkModeTransformer(springBatchSupportProperties.getWeb().isDarkMode()));

		registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/META-INF/spring-batch/terrific/assets/font/");
		registry.addResourceHandler("/assets/js/**").addResourceLocations("classpath:/META-INF/spring-batch/terrific/assets/js/");
		registry.addResourceHandler("/modules/**").addResourceLocations("classpath:/META-INF/spring-batch/terrific/modules/");
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/spring-batch/terrific/assets/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
