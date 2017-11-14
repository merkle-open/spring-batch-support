package com.namics.oss.spring.support.batch.starter.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * DummyController.
 *
 * @author lboesch, Namics AG
 * @since 29.08.17 12:55
 */
@Controller
public class TestController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String get() {
		return "batch";
	}
}
