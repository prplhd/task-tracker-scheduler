package ru.prplhd.tasktracker.scheduler.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.prplhd.tasktracker.scheduler.service.DailyReportService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class DailyReportScheduler {

    private static final String MOSCOW_TIME_ZONE = "Europe/Moscow";

    private final DailyReportService dailyReportService;

    @Scheduled(cron = "0 0 0 * * *", zone = MOSCOW_TIME_ZONE)
    public void runDailyReportJob() {
        ZoneId zoneId = ZoneId.of(MOSCOW_TIME_ZONE);

        ZonedDateTime startOfToday = LocalDate.now(zoneId).atStartOfDay(zoneId);

        Instant from = startOfToday.minusDays(1L).toInstant();

        Instant to = startOfToday.toInstant();

        dailyReportService.sendDailyReports(from, to);
    }
}
