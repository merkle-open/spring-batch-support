package com.namics.oss.spring.support.batch.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.namics.oss.spring.support.batch.model.Job;
import com.namics.oss.spring.support.batch.model.JobParameterBean;
import com.namics.oss.spring.support.batch.service.JobService;

import jakarta.inject.Inject;

@RestController
@RequestMapping("/jobs")
public class JobDataController {
    public static final String EXECUTION_TIMESTAMP = "executionTimestamp";
    public static final String JOB_PARAMETERS = "jobParameters";

    private final JobService jobService;
    private final JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

    @Inject
    public JobDataController(final JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Job> getJobs() {
        return jobService.getJobs();
    }

    @RequestMapping(value = "/{jobName}", method = RequestMethod.GET)
    public Job getJobs(@PathVariable("jobName") String jobName) {
        return jobService.getJob(jobName);
    }

    @RequestMapping(value = "/{jobName}/parameters", method = RequestMethod.GET)
    public List<JobParameterBean> getJobParameters(@PathVariable("jobName") String jobName) {
        final Job job = jobService.getJob(jobName);
        return job.getJobParameters()
                .stream()
                .filter(parameter -> !EXECUTION_TIMESTAMP.equalsIgnoreCase(parameter.getKey()))
                .map(parameter ->
                        new JobParameterBean(
                                parameter.getKey(),
                                parameter.getValue(),
                                parameter.getParameterType()
                        )
                )
                .toList();
    }

    @RequestMapping(value = "/{jobName}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startJob(@PathVariable("jobName") String jobName, @RequestParam(required = false) MultiValueMap<String, String> payload) {
        Optional
                .ofNullable(payload.getFirst(JOB_PARAMETERS))
                .map(PropertiesConverter::stringToProperties)
                .map(jobParametersConverter::getJobParameters)
                .ifPresentOrElse(
                        jobParameters -> jobService.startJob(jobName, jobParameters),
                        () -> jobService.startJob(jobName)
                );
    }

    @RequestMapping(value = "/{jobName}/stop", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void stopJob(@PathVariable("jobName") String jobName) {
        jobService.stopJob(jobName);
    }
}
