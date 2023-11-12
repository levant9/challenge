package pl.levant.challenge.forecast.weather.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.levant.challenge.forecast.weather.model.ForecastFeed;
import pl.levant.challenge.forecast.weather.persistance.ForecastId;
import pl.levant.challenge.forecast.weather.persistance.ForecastRecord;
import pl.levant.challenge.forecast.weather.persistance.WeatherRepository;

@RequiredArgsConstructor
@Slf4j
public class FeedForecasts {

    private final WeatherRepository weatherRepository;

    public void feed(ForecastFeed forecastsFeed) {
        if (forecastsFeed.forecasts() == null || forecastsFeed.forecasts().isEmpty()) {
            throw new IllegalStateException("Empty feed detected");
        }
        var forecastRecords = forecastsFeed.forecasts().stream()
                .map(forecast -> new ForecastRecord(
                        new ForecastId(forecast.date(), forecastsFeed.latitude(), forecastsFeed.longitude()),
                        forecast.minTemperature(),
                        forecast.maxTemperature(),
                        forecast.windSpeed(),
                        forecast.windDirection()
                ))
                .toList();
        log.debug("Saving {} forecasts feed...", forecastRecords.size());
        weatherRepository.saveAll(forecastRecords);
        log.debug("Saved");
    }
}
