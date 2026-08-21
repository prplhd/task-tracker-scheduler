package ru.prplhd.tasktracker.scheduler.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.prplhd.tasktracker.scheduler.dto.DailyReportDataDto;

import java.time.Instant;
import java.util.List;

@Component
public class BackendClient {

    private static final String API_KEY_HEADER = "X-Internal-Api-Key";

    private final RestClient restClient;

    public BackendClient(RestClient.Builder builder,
                         @Value("${backend.url}") String backendUrl,
                         @Value("${backend.internal-api-key}") String internalApiKey
    ) {
        this.restClient = builder
                .baseUrl(backendUrl)
                .defaultHeader(API_KEY_HEADER, internalApiKey)
                .build();
    }

    public List<DailyReportDataDto> getDailyReportData(Instant from, Instant to) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/daily-report-data")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<DailyReportDataDto>>() {});
    }
}
