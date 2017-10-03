/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.demo.controller;

import com.namics.oss.spring.support.batch.model.Job;
import com.namics.oss.spring.support.batch.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

/**
 * Managing jobs.
 *
 * @author lboesch, Namics AG
 * @since Jan 9, 2014
 */
@Controller
@RequestMapping("/old/jobs")
public class JobController {

	@Inject
	private JobService jobService;

	/**
	 * Returns a list of all registered jobs.
	 *
	 * @param model .
	 * @return .
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String listJobs(Model model) {
		List<Job> jobs = jobService.getJobs();
		model.addAttribute("jobs", jobs);
		return "jobOverview";
	}

	/**
	 * Creates an execution for a job (i.e. starts the job).
	 *
	 * @param jobName .
	 * @return .
	 */
	@RequestMapping(value = "/{jobName}/start", method = RequestMethod.POST)
	public String startJob(@PathVariable("jobName") String jobName) {
		jobService.startJob(jobName);
		return "redirect:../../jobs";
	}

	/**
	 * Stops the job.
	 *
	 * @param jobName .
	 * @return .
	 */
	@RequestMapping(value = "/{jobName}/stop", method = RequestMethod.POST)
	public String stopJob(@PathVariable("jobName") String jobName) {
		jobService.stopJob(jobName);
		return "redirect:../../jobs";
	}

}
