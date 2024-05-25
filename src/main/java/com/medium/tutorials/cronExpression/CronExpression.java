package com.medium.tutorials.cronExpression;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;

import static com.cronutils.model.CronType.QUARTZ;

@Slf4j
public class CronExpression {
    // parser for quartz cron expression
    static CronParser cronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));;

    public static void main(String[] args) {
        CronExpression cronExpression = new CronExpression();
        cronExpression.describeCron("* * * ? * *");
        cronExpression.describeCron("0 * * ? * *");
        cronExpression.describeCron("0 15,30,45 * ? * *");
        cronExpression.describeCron("0 0 12 ? * MON-FRI");
    }

    void describeCron(String cron) {
        CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);
        log.info("Cron " + cron + " description " + descriptor.describe(cronParser.parse(cron)));

        ZonedDateTime now = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(cronParser.parse(cron));
        log.info("Next execution " + executionTime.nextExecution(now));
        log.info("Timestamp " + now + " satisfies cron expression " + executionTime.isMatch(now));
    }
}
