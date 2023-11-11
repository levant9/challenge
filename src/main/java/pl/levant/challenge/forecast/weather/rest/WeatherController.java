package pl.levant.challenge.forecast.weather.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.levant.challenge.forecast.weather.WeatherComponent;
import pl.levant.challenge.forecast.weather.model.Location;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherComponent weatherComponent;

    private static final String FORECAST_DAYS = "7";

    @GetMapping("/weather-forecasts/stavanger")
    public ForecastRestResponse getWeatherForecastForStavanger(@RequestParam(defaultValue = FORECAST_DAYS) int days) {
        log.debug("Retrieving (GET) weather forecasts for Stavanger for {} days", days);
        var locationStavanger = Location.Stavanger;
        var forecasts = weatherComponent.getForecasts(days, locationStavanger);
        log.debug("Retrieved forecasts {}", forecasts);
        return ForecastRestResponse.from(locationStavanger.getLatitude(), locationStavanger.getLongitude(), forecasts);
    }
}
