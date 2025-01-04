package com.yuseogi.batchserver.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final static String DAY = "DAY";
    private final static String WEEK = "WEEK";
    private final static String MONTH = "MONTH";

    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;

    // 매일 자정
    @Scheduled(cron = "0 0 0 * * *")
    public void dailyRunJob() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("dateTerm", DAY)
            .addLocalDate("startDate", startDate)
            .addLocalDate("endDate", endDate)
            .toJobParameters();
        try {
            jobLauncher.run(jobRegistry.getJob("productSaleCountStatisticJob"), jobParameters);
            jobLauncher.run(jobRegistry.getJob("settlementJob"), jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    // 매주 월요일 자정
    @Scheduled(cron = "0 0 0 * * 1")
    public void weeklyRunJob() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = LocalDate.now();

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("dateTerm", WEEK)
            .addLocalDate("startDate", startDate)
            .addLocalDate("endDate", endDate)
            .toJobParameters();
        try {
            jobLauncher.run(jobRegistry.getJob("productSaleCountStatisticJob"), jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 매달 1일 자정
    @Scheduled(cron = "0 0 0 1 * *")
    public void monthlyRunJob() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("dateTerm", MONTH)
            .addLocalDate("startDate", startDate)
            .addLocalDate("endDate", endDate)
            .toJobParameters();
        try {
            jobLauncher.run(jobRegistry.getJob("productSaleCountStatisticJob"), jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // 매달 1, 5, 10, 15, 20, 25일 자정
    @Scheduled(cron = "0 0 0 1,5,10,15,20,25 * *")
    public void monthlySettlementRunJob() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("dateTerm", MONTH)
            .addLocalDate("startDate", startDate)
            .addLocalDate("endDate", endDate)
            .toJobParameters();
        try {
            jobLauncher.run(jobRegistry.getJob("settlementJob"), jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
