package pl.levant.challenge.forecast.weather.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.levant.challenge.forecast.weather.WeatherComponent;
import pl.levant.challenge.forecast.weather.model.Location;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherComponent weatherComponent;

    private static final String FORECAST_DAYS = "7";

    @GetMapping("/weather-forecasts/stavanger")
    public ForecastResponse getWeatherForecastForStavanger(@RequestParam(defaultValue = FORECAST_DAYS) int days) {
        var forecast = weatherComponent.getForecast(days, Location.Stavanger);
        return new ForecastResponse();
    }
}
