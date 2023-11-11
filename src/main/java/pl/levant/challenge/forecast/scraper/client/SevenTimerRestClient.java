package pl.levant.challenge.forecast.scraper.client;

import org.springframework.web.reactive.function.client.WebClient;

public class SevenTimerRestClient implements SevenTimerClient {

    private final WebClient webClient;

    public SevenTimerRestClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    @Override
    public ForecastOutput readForecastForEightDays(double longitude, double latitude) {
//        return webClient.get()
//
//                .retrieve();
        return null;
    }
}
