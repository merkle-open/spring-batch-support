package com.namics.oss.spring.support.batch.ui;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import io.micrometer.core.instrument.util.IOUtils;
import jakarta.servlet.http.HttpServletRequest;

public class DarkModeTransformer implements ResourceTransformer {
	private static final Logger LOG = LoggerFactory.getLogger(DarkModeTransformer.class);
	private final static String NON_DARK_MODE_CSS = "/assets/css/bootstrap.min.css";
	private final static String DARK_MODE_CSS = "/assets/css/bootstrap.dark.min.css";

	protected boolean isDarkMode;

	public DarkModeTransformer(boolean isDarkMode) {
		this.isDarkMode = isDarkMode;
	}

	@Override
	public Resource transform(final HttpServletRequest request, final Resource resource, final ResourceTransformerChain transformerChain) {
		if (isDarkMode) {
			LOG.info("about to show content in darkMode, replace links for css file.");
			try {
				final Resource transformedResource = transformerChain.transform(request, resource);
				final String html = IOUtils
						.toString(transformedResource.getInputStream(), UTF_8)
						.replace(NON_DARK_MODE_CSS, DARK_MODE_CSS);
				return new TransformedResource(transformedResource, html.getBytes());
			} catch (Exception e) {
				LOG.info("problem with darkmode, use default resource instead.");
			}
		}
		return resource;
	}
}
