package pl.levant.challenge.forecast.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import pl.levant.challenge.forecast.scraper.ForecastScraperComponent;
import pl.levant.challenge.forecast.weather.model.Forecast;
import pl.levant.challenge.forecast.weather.model.Location;
import pl.levant.challenge.forecast.weather.usecase.FeedForecasts;
import pl.levant.challenge.forecast.weather.usecase.GetForecasts;

import java.util.List;

@RequiredArgsConstructor
public class WeatherComponent {

    private final GetForecasts getForecastsUseCase;
    private final FeedForecasts feedForecastsUseCase;
    private final ForecastScraperComponent forecastScraperComponent;

    public List<Forecast> getForecast(int days, Location location) {
        return getForecastsUseCase.get(days, location);
    }

    @Scheduled(fixedDelayString = "${domain.forecast.feeding-rate}")
    public void feedForecast() {
        var stavangerLocation = Location.Stavanger;
        var forecastsFeed = forecastScraperComponent.retrieveForecasts(stavangerLocation.getLongitude(), stavangerLocation.getLatitude());
        feedForecastsUseCase.feed(forecastsFeed);
    }
}
