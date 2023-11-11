package pl.levant.challenge.forecast.weather.usecase;

import lombok.RequiredArgsConstructor;
import pl.levant.challenge.forecast.weather.model.Forecast;
import pl.levant.challenge.forecast.weather.model.Location;
import pl.levant.challenge.forecast.weather.persistance.ForecastRecord;
import pl.levant.challenge.forecast.weather.persistance.WeatherRepository;

import java.util.List;

@RequiredArgsConstructor
public class GetForecasts {

    private final WeatherRepository weatherRepository;

    public List<Forecast> get(int days, Location location) {
        return weatherRepository.getForecasts(days, location.getLatitude(), location.getLongitude()).stream()
                .map(ForecastRecord::toDomain)
                .toList();
    }
}
