package com.example.readFromCsv.controller;

import com.example.readFromCsv.entity.TestEntity;
import com.example.readFromCsv.service.CSVService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CSVController {
    private final CSVService service;


    private final JobLauncher jobLauncher;


    private final Job job;

    public CSVController(CSVService service, JobLauncher jobLauncher, Job job) {
        this.service = service;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }


    @PostMapping("/readCsv")
    void ReadCsv() {
        service.readCsv();
    }

    @GetMapping("/findAll")
    List<TestEntity> findAll(@RequestParam Integer startScore, @RequestParam Integer endScore) {
        return service.finAll(startScore, endScore);
    }

    @PostMapping("/readData")
    void readData() {
        service.readData();
    }

    @GetMapping("/importCustomers")
    public void importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
