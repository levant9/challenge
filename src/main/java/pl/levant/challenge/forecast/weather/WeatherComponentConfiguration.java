package pl.levant.challenge.forecast.weather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.levant.challenge.forecast.scraper.ForecastScraperComponent;
import pl.levant.challenge.forecast.weather.persistance.WeatherRepository;
import pl.levant.challenge.forecast.weather.usecase.FeedForecasts;
import pl.levant.challenge.forecast.weather.usecase.GetForecasts;

@Configuration
public class WeatherComponentConfiguration {

    @Bean
    public WeatherComponent weatherComponent(WeatherRepository weatherRepository, ForecastScraperComponent forecastScraperComponent) {
        return new WeatherComponent(
                new GetForecasts(weatherRepository),
                new FeedForecasts(weatherRepository),
                forecastScraperComponent
        );
    }
}
