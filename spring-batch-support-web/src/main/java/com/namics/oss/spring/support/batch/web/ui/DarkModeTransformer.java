package com.namics.oss.spring.support.batch.web.ui;

import io.micrometer.core.instrument.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * DarkModeTransformer.
 *
 * @author lboesch, Namics AG
 * @since 06.04.20 15:19
 */
public class DarkModeTransformer implements ResourceTransformer {
	private static final Logger LOG = LoggerFactory.getLogger(DarkModeTransformer.class);

	protected boolean isDarkMode;

	private static String NON_DARK_MODE_CSS = "assets/css/bootstrap.min.css";
	private static String DARK_MODE_CSS = "assets/css/bootstrap.dark.min.css";

	public DarkModeTransformer(boolean isDarkMode) {
		this.isDarkMode = isDarkMode;
	}

	@Override
	public Resource transform(HttpServletRequest httpServletRequest, Resource resource, ResourceTransformerChain resourceTransformerChain) throws IOException {
		if (isDarkMode) {
			LOG.info("about to show content in darkMode, replace links for css file.");
			try {
				resource = resourceTransformerChain.transform(httpServletRequest, resource);
				String html = IOUtils.toString(resource.getInputStream(), UTF_8);
				html = html.replace(NON_DARK_MODE_CSS, DARK_MODE_CSS);
				return new TransformedResource(resource, html.getBytes());
			} catch (Exception e) {
				LOG.info("problem with darkmode, use default resource instead.");
			}
		}
		return resource;
	}
}
