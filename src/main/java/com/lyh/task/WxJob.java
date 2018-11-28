package com.lyh.task;

import com.lyh.service.IMemberService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

public class WxJob implements Job {
    @Autowired
    @Qualifier("memberService")
    private IMemberService memberService;
    private static Logger logger = LoggerFactory.getLogger(WxJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    }
    public static void main(String[] args) throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        logger.info("scheduler.start");
        JobDetail jobDetail = JobBuilder.newJob(WxJob.class).withIdentity("job1","group1").build();
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1800).repeatForever();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startNow().withSchedule(simpleScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail,trigger);
        TimeUnit.SECONDS.sleep(20 );
        scheduler.shutdown();
        logger.info("scheduler.shutdown");
    }
}
