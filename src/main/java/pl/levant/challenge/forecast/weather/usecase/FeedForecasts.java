package pl.levant.challenge.forecast.weather.usecase;

import lombok.RequiredArgsConstructor;
import pl.levant.challenge.forecast.weather.model.ForecastFeed;
import pl.levant.challenge.forecast.weather.persistance.ForecastRecord;
import pl.levant.challenge.forecast.weather.persistance.WeatherRepository;

@RequiredArgsConstructor
public class FeedForecasts {

    private final WeatherRepository weatherRepository;

    public void feed(ForecastFeed forecastsFeed) {
        var forecastRecords = forecastsFeed.forecasts().stream()
                .map(forecast -> new ForecastRecord())
                .toList();
        weatherRepository.saveAll(forecastRecords);
    }
}
