package pl.levant.challenge.forecast.scraper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.levant.challenge.forecast.scraper.client.SevenTimerRestClient;

@Configuration
public class ForecastScraperConfiguration {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ForecastScraperComponent forecastScraperComponent(WebClient.Builder webClientBuilder) {
        return new ForecastScraperComponent(
                new ForecastProviderService(new SevenTimerRestClient(webClientBuilder)));
    }
}
