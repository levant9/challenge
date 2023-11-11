package pl.levant.challenge.forecast.scraper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.levant.challenge.forecast.scraper.client.SevenTimerClient;
import pl.levant.challenge.forecast.weather.model.Location;

import static org.assertj.core.api.Assertions.assertThat;

class ForecastScraperComponentTest {

    @Mock
    private SevenTimerClient clientMock;

    private final ForecastScraperComponent component = new ForecastScraperComponent(
            new ForecastProviderService(clientMock)
    );

    @Test
    void given_whenRetrievingForecast_thenParseProperResponse() {
        var forecastFeed = component.retrieveForecasts(Location.Stavanger.getLongitude(), Location.Stavanger.getLatitude());
        assertThat(forecastFeed).isNotNull();
    }

}