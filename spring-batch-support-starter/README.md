# namics-spring-batch-starter

Spring-Batch module can be configured using Auto-Configuration. This document provides a basic overview on how to utilize the namics-spring-batch-starter. Detailed information on how to work with the starter may be observed in the namics-spring-batch-starter-sample project.

## Step 1: Add the required dependencies

Add the dependency for the module itself (i.e. spring-batch-support) and the corresponding starter module (i.e. spring-batch-support-starter) which is responsible for the auto-configuration of the module.

    <dependency>
        <groupId>com.namics.oss.spring.support.batch</groupId>
        <artifactId>spring-batch-support-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.namics.oss.spring.support.batch</groupId>
        <artifactId>spring-batch-support</artifactId>
        <version>1.0.0</version>
    </dependency>

Additionally, add the dependency spring-batch-support-web, if you want to provide a simple interface for management (start/stop) of batch jobs via REST-Controller.

    <dependency>
        <groupId>com.namics.oss.spring.support.batch</groupId>
        <artifactId>spring-batch-support-web</artifactId>
        <version>1.0.0</version>
    </dependency>


## Step 2: Configure the required properties of the starter

### Configuration of the batch jobs
Set the package, where your batch jobs were configured. This property is required, otherwise no jobs were found:

    # required properties for spring-batch
    com.namics.oss.spring.support.batch.job-configuration.package-name=com.namics.your-project.jobs

### Configuration of the web interface
The starter allows you to override the default settings for servlet-name and servlet-mapping.

    # Optional properties for spring-batch-web
    com.namics.oss.spring.support.batch.web.servlet-name=springBatch
    com.namics.oss.spring.support.batch.web.servlet-mapping=/batch/*
    
## Step 3: Enable cleanup scheduled method

Spring-Batch module has possibility to enable a scheduled method to clean up the spring batch database tables.

### Enable clean up task

Add the following property to enable the cleanup task. Ensure that a TaskScheduler is available in the context.

    # property to enable clean up
    com.namics.oss.spring.support.batch.clean-up.enabled=true
    
### Configuration of task

You can configure the days, how long the executions were persisted in the db. Default is 10 days. You could also configure the cron expression for scheduling of the cleanup task.

    # properties for clean up task configuration
    com.namics.oss.spring.support.batch.clean-up.keep-days=10
    com.namics.oss.spring.support.batch.clean-up.cron=0/10 0/1 * 1/1 * ?