package pl.levant.challenge.forecast.weather.rest;

import pl.levant.challenge.forecast.weather.model.Forecast;

import java.time.LocalDate;
import java.util.List;

public record ForecastRestResponse(double latitude, double longitude, List<ForecastRest> forecasts) {

    static ForecastRestResponse from(double latitude, double longitude, List<Forecast> forecasts) {
        var forecastsRest = forecasts.stream()
                .map(forecast -> new ForecastRest(
                        forecast.date(),
                        forecast.date().getDayOfWeek().toString(),
                        forecast.minTemperature(),
                        forecast.maxTemperature(),
                        forecast.windSpeed(),
                        forecast.windDirection()
                ))
                .toList();
        return new ForecastRestResponse(latitude, longitude, forecastsRest);
    }

    public record ForecastRest(
            LocalDate date, String dayOfWeek, int minTemperature, int maxTemperature, int windSpeed, String windDirection
    ) {
    }
}
