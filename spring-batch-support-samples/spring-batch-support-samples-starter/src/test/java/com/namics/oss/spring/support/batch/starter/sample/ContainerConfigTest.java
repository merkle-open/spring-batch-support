package com.namics.oss.spring.support.batch.starter.sample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ContainerConfigTest.
 *
 * @author lboesch, Namics AG
 * @since 03.11.17 10:36
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = { "TEST" })
@SpringBootTest(classes = Application.class)
class ContainerConfigTest {
	private static final Logger LOG = LoggerFactory.getLogger(ContainerConfigTest.class);


	@Test
	void testContextStart() {
		assertTrue(true, "context successful");
		LOG.info("Context started successfully");
	}

}