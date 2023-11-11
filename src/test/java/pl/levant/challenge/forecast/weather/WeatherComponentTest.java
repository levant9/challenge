package pl.levant.challenge.forecast.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.levant.challenge.forecast.scraper.ForecastScraperComponent;
import pl.levant.challenge.forecast.weather.helper.InMemoryWeatherRepository;
import pl.levant.challenge.forecast.weather.model.Location;
import pl.levant.challenge.forecast.weather.usecase.FeedForecasts;
import pl.levant.challenge.forecast.weather.usecase.GetForecasts;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherComponentTest {

    private WeatherComponent weatherComponent;
    @Mock
    private ForecastScraperComponent forecastScraperComponent;

    @BeforeEach
    public void setup() {
        var repository = new InMemoryWeatherRepository();
        weatherComponent = new WeatherComponent(
                new GetForecasts(repository),
                new FeedForecasts(repository),
                forecastScraperComponent
        );
    }

    @Test
    void givenLoadedForecast_whenGetForecastsForStavanger_thenReturnForecastForLast7Days() {
        var forecasts = weatherComponent.getForecasts(7, Location.Stavanger);
        assertThat(forecasts).isNotEmpty();
    }

}