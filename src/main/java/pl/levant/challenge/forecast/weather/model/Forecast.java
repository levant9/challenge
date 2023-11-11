package pl.levant.challenge.forecast.weather.model;

import java.time.LocalDate;

public record Forecast(
        LocalDate date, int minTemperature, int maxTemperature, int windSpeed, String windDirection
) {
}
