package pl.levant.challenge.forecast.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.webflux.LogbookExchangeFilterFunction;
import pl.levant.challenge.forecast.scraper.client.SevenTimerRestClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class ForecastScraperConfiguration {

    @Value("${domain.seven-timer.client.base-url}")
    private String sevenTimerBaseUrl;

    @Bean
    public WebClient.Builder webClientBuilder(Logbook logbook) {
        var httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(30))
                .followRedirect(true);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(new LogbookExchangeFilterFunction(logbook));
    }

    @Bean
    public ForecastScraperComponent forecastScraperComponent(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        return new ForecastScraperComponent(
                new ForecastProviderService(new SevenTimerRestClient(webClientBuilder, sevenTimerBaseUrl, objectMapper)));
    }

    @Bean
    public Logbook logbook() {
        return Logbook.create();
    }
}
