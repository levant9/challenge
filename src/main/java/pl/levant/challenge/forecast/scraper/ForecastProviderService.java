package pl.levant.challenge.forecast.scraper;

import lombok.RequiredArgsConstructor;
import pl.levant.challenge.forecast.scraper.client.SevenTimerClient;
import pl.levant.challenge.forecast.weather.model.ForecastFeed;

@RequiredArgsConstructor
class ForecastProviderService {

    private final SevenTimerClient sevenTimerClient;

    public ForecastFeed retrieveForecasts(double longitude, double latitude) {
        var forecastOutput = sevenTimerClient.readForecastForEightDays(longitude, latitude);
        return null;
    }
}
