package ru.prplhd.tasktracker.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prplhd.tasktracker.scheduler.client.BackendClient;
import ru.prplhd.tasktracker.scheduler.dto.DailyReportDataDto;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final BackendClient backendClient;

    public void sendDailyReports(Instant from, Instant to) {
        List<DailyReportDataDto> dailyReportDataDtos = backendClient.getDailyReportData(from, to);


    }
}
