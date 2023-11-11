package pl.levant.challenge.forecast.scraper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
public class SevenTimerRestClient implements SevenTimerClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SevenTimerRestClient(
            WebClient.Builder webClientBuilder,
            String sevenTimerBaseUrl,
            ObjectMapper objectMapper
    ) {
        webClient = webClientBuilder
                .baseUrl(sevenTimerBaseUrl)
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public ForecastOutput readForecastForEightDays(double longitude, double latitude) {
        log.info("Getting forecast from 7Time for long {} and lat {}", longitude, latitude);
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("lon", longitude)
                        .queryParam("lat", latitude)
                        .queryParam("product", "civil")
                        .queryParam("output", "json")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();
        log.debug("Response {}", response);
        try {
            return objectMapper.readValue(response, ForecastOutput.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse response from 7time", e);
            throw new IllegalStateException("Failed to parse response from 7time", e);
        }
    }
}
