package pl.levant.challenge.forecast.weather;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import pl.levant.challenge.forecast.scraper.ForecastScraperComponent;
import pl.levant.challenge.forecast.weather.model.Forecast;
import pl.levant.challenge.forecast.weather.model.Location;
import pl.levant.challenge.forecast.weather.usecase.FeedForecasts;
import pl.levant.challenge.forecast.weather.usecase.GetForecasts;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class WeatherComponent {

    private final GetForecasts getForecastsUseCase;
    private final FeedForecasts feedForecastsUseCase;
    private final ForecastScraperComponent forecastScraperComponent;

    public List<Forecast> getForecasts(int days, Location location) {
        log.debug("{}", location);
        return getForecastsUseCase.get(days, location);
    }

    @Scheduled(fixedDelayString = "${domain.forecast.feeding-rate}")
    @Transactional
    public void feedForecast() {
        log.info("Feeding weather forecasts triggered for Stavanger...");
        var stavangerLocation = Location.Stavanger;
        var forecastsFeed = forecastScraperComponent.retrieveForecasts(stavangerLocation.getLongitude(), stavangerLocation.getLatitude());
        log.debug("Retrieved forecasts, feeding...");
        feedForecastsUseCase.feed(forecastsFeed);
    }
}
