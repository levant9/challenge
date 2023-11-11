package pl.levant.challenge.forecast.weather.persistance;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pl.levant.challenge.forecast.weather.model.Forecast;

@Entity
@Table(name = "forecast")
public class ForecastRecord {

    @EmbeddedId
    private ForecastId id;
    private int minTemperature;
    private int maxTemperature;
    private int windSpeed;
    private String windDirection;

    public Forecast toDomain() {
        return new Forecast(
                id.getDate(),
                minTemperature,
                maxTemperature,
                windSpeed,
                windDirection
        );
    }
}
