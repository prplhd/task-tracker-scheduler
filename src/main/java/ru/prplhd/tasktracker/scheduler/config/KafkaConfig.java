package ru.prplhd.tasktracker.scheduler.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import ru.prplhd.tasktracker.scheduler.dto.DailyReportDataDto;
import ru.prplhd.tasktracker.scheduler.message.EmailSendingTask;

import java.time.Duration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic summarizationRequestTopic() {
        return TopicBuilder.name("SUMMARIZATION_REQUESTS")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic summarizationResponseTopic() {
        return TopicBuilder.name("SUMMARIZATION_RESPONSES")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ReplyingKafkaTemplate<String, DailyReportDataDto, String> replyingKafkaTemplate(
            ProducerFactory<String, DailyReportDataDto> producerFactory,
            ConcurrentKafkaListenerContainerFactory<String, String> factory
    ) {
        ReplyingKafkaTemplate<String, DailyReportDataDto, String> template = new ReplyingKafkaTemplate<>(
                producerFactory,
                factory.createContainer("SUMMARIZATION_RESPONSES")
        );

        template.setDefaultReplyTimeout(Duration.ofSeconds(40));

        return template;
    }

    @Bean
    public KafkaTemplate<String, EmailSendingTask> emailSendingKafkaTemplate(
            ProducerFactory<String, EmailSendingTask> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
