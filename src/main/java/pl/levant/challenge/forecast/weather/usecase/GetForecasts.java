package pl.levant.challenge.forecast.weather.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import pl.levant.challenge.forecast.weather.model.Forecast;
import pl.levant.challenge.forecast.weather.model.Location;
import pl.levant.challenge.forecast.weather.persistance.ForecastRecord;
import pl.levant.challenge.forecast.weather.persistance.WeatherRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class GetForecasts {

    private final WeatherRepository weatherRepository;

    public List<Forecast> get(int days, Location location) {
        var currentDate = LocalDate.now();
        var numberOfDays = PageRequest.of(0, days);
        return weatherRepository.getForecasts(currentDate, location.getLatitude(), location.getLongitude(), numberOfDays)
                .stream()
                .map(ForecastRecord::toDomain)
                .toList();
    }
}
