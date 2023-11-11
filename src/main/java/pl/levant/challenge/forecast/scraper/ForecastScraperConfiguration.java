package pl.levant.challenge.forecast.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.levant.challenge.forecast.scraper.client.SevenTimerRestClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class ForecastScraperConfiguration {

    @Value("${domain.seven-timer.client.base-url}")
    private String sevenTimerBaseUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        var httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }

    @Bean
    public ForecastScraperComponent forecastScraperComponent(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        return new ForecastScraperComponent(
                new ForecastProviderService(new SevenTimerRestClient(webClientBuilder, sevenTimerBaseUrl, objectMapper)));
    }
}
