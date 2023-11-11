package pl.levant.challenge.forecast.scraper;

import lombok.RequiredArgsConstructor;
import pl.levant.challenge.forecast.weather.model.ForecastFeed;

@RequiredArgsConstructor
public class ForecastScraperComponent {

    private final ForecastProviderService forecastProviderService;

    public ForecastFeed retrieveForecasts(double longitude, double latitude) {
        return forecastProviderService.retrieveForecasts(longitude, latitude);
    }
}
