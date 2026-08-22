package ru.prplhd.tasktracker.scheduler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.prplhd.tasktracker.scheduler.client.BackendClient;
import ru.prplhd.tasktracker.scheduler.dto.DailyReportDataDto;
import ru.prplhd.tasktracker.scheduler.message.EmailSendingTask;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final BackendClient backendClient;

    private final ReplyingKafkaTemplate<String, DailyReportDataDto, String> replyingKafkaTemplate;

    private final KafkaTemplate<String, EmailSendingTask> kafkaTemplate;

    public void sendDailyReports(Instant from, Instant to) {
        List<DailyReportDataDto> dailyReportDataDtos = backendClient.getDailyReportData(from, to);

        for (DailyReportDataDto dailyReportDataDto : dailyReportDataDtos) {

            ProducerRecord<String, DailyReportDataDto> record = new ProducerRecord<>("SUMMARIZATION_REQUESTS", dailyReportDataDto);

            RequestReplyFuture<String, DailyReportDataDto, String> future = replyingKafkaTemplate.sendAndReceive(record);

            future.whenComplete((result, exception) -> {

                if (exception != null) {
                    log.error("Failed to send and receive daily report data {}", dailyReportDataDto.email(), exception);
                    return;
                }

                sendDailyReport(result.value(), dailyReportDataDto.email());

            });
        }

    }

    private void sendDailyReport(String dailyReport, String email) {
        EmailSendingTask emailSendingTask = new EmailSendingTask(
                email,
                "Your Daily Task Report",
                dailyReport
        );

        CompletableFuture<SendResult<String, EmailSendingTask>> future = kafkaTemplate.send("EMAIL_SENDING_TASKS", emailSendingTask);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send daily report email task for {}", email, exception);
            } else {
                log.info("Message for {} sent successfully: {}", email, result.getRecordMetadata());
            }
        });
    }
}
