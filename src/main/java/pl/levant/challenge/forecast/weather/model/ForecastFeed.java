package pl.levant.challenge.forecast.weather.model;

import java.util.List;

public record ForecastFeed(double longitude, double latitude, List<Forecast> forecasts) {
}
