package com.medium.tutorials.cronExpression;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Locale;

import static com.cronutils.model.CronType.QUARTZ;

@Slf4j
public class CronExpression {
    public static void main(String[] args) {
        CronExpression cronExpression = new CronExpression();

        CronParser quartzCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));
        CronParser customCronParser = new CronParser(cronExpression.customCronDefinition());

        log.info("###### Quartz cron #######");
        cronExpression.describeCron(quartzCronParser, "* * * ? * *");
        cronExpression.describeCron(quartzCronParser, "0 * * ? * *");
        cronExpression.describeCron(quartzCronParser, "0 15,30,45 * ? * *");
        cronExpression.describeCron(quartzCronParser, "0 0 12 ? * MON-FRI");
        log.info("###### Custom cron #######");
        cronExpression.describeCron(customCronParser, "* * 10-12 1 * *");
        cronExpression.describeCron(customCronParser, "* * * * * 5");
        cronExpression.describeCron(customCronParser, "* * 12-14 L-1 * *");
        cronExpression.describeCron(customCronParser, "* * * * * 1,2,3");
    }

    void describeCron(CronParser parser, String cron) {
        CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);
        log.info("Cron " + cron + " description " + descriptor.describe(parser.parse(cron)));

        ZonedDateTime now = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
        log.info("Next execution " + executionTime.nextExecution(now));
        log.info("Timestamp " + now + " satisfies cron expression " + executionTime.isMatch(now));
    }

    CronDefinition customCronDefinition() {
        return CronDefinitionBuilder.defineCron()
                .withSeconds().and()
                .withMinutes().and()
                .withHours().and()
                .withDayOfMonth()
                .supportsHash().supportsL().supportsW().and()
                .withMonth().and()
                .withDayOfWeek()
                .withIntMapping(7, 0) //we support non-standard non-zero-based numbers!
                .supportsHash().supportsL().supportsW().and()
                .withYear().optional().and()
                .instance();
    }
}
