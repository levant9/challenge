package pl.levant.challenge.forecast.weather.model;

import java.util.List;

public record ForecastFeed(Location location, List<Forecast> forecasts) {
}
