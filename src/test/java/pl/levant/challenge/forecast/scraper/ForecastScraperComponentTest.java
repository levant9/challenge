package pl.levant.challenge.forecast.scraper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.levant.challenge.forecast.scraper.client.ForecastOutput;
import pl.levant.challenge.forecast.scraper.client.SevenTimerClient;
import pl.levant.challenge.forecast.weather.model.Location;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ForecastScraperComponentTest {

    private final SevenTimerClient clientMock = Mockito.mock(SevenTimerClient.class);

    private final ForecastScraperComponent component = new ForecastScraperComponent(
            new ForecastProviderService(clientMock)
    );

    @Test
    void given_whenRetrievingForecast_thenParseProperResponse() {
        //given
        var location = Location.Stavanger;
        var dataSeries = List.of(
                new ForecastOutput.DataSeries(3, 10, windData()),
                new ForecastOutput.DataSeries(6, 11, windData()),
                new ForecastOutput.DataSeries(9, 11, windData()),
                new ForecastOutput.DataSeries(12, 12, windData()),
                new ForecastOutput.DataSeries(15, 9, windData()),
                new ForecastOutput.DataSeries(18, 6, windData())
        );
        when(clientMock.readForecastForEightDays(location.getLongitude(), location.getLatitude()))
                .thenReturn(new ForecastOutput("2023111306", dataSeries));
        //when
        var forecastFeed = component.retrieveForecasts(location.getLongitude(), location.getLatitude());
        //then
        assertThat(forecastFeed.longitude()).isEqualTo(location.getLongitude());
        assertThat(forecastFeed.latitude()).isEqualTo(location.getLatitude());
        assertThat(forecastFeed.forecasts()).hasSize(1);
        assertThat(forecastFeed.forecasts().get(0).date()).isEqualTo(LocalDate.of(2023, 11, 13));
        assertThat(forecastFeed.forecasts().get(0).maxTemperature()).isEqualTo(12);
        assertThat(forecastFeed.forecasts().get(0).minTemperature()).isEqualTo(6);
        assertThat(forecastFeed.forecasts().get(0).windDirection()).isEqualTo("S");
        assertThat(forecastFeed.forecasts().get(0).windSpeed()).isEqualTo(5);
    }

    @Test
    void given_whenRetrievedIncorrectDataWithoutDataseries_thenThrowException() {
        var location = Location.Stavanger;
        when(clientMock.readForecastForEightDays(location.getLongitude(), location.getLatitude()))
                .thenReturn(new ForecastOutput("2023111306", List.of()));
        assertThatThrownBy(() -> component.retrieveForecasts(location.getLongitude(), location.getLatitude()))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Wrong forecast output received");
    }

    @Test
    void given_whenRetrievedIncorrectInitDate_thenThrowException() {
        var location = Location.Stavanger;
        when(clientMock.readForecastForEightDays(location.getLongitude(), location.getLatitude()))
                .thenReturn(new ForecastOutput("2023111325", List.of(dataSeries())));
        assertThatThrownBy(() -> component.retrieveForecasts(location.getLongitude(), location.getLatitude()))
                .isExactlyInstanceOf(DateTimeParseException.class)
                .hasMessage("Text '2023111325' could not be parsed: Invalid value for HourOfDay (valid values 0 - 23): 25");
    }

    private static ForecastOutput.WindData windData() {
        return new ForecastOutput.WindData("S", 5);
    }

    private static ForecastOutput.DataSeries dataSeries() {
        return new ForecastOutput.DataSeries(3, 10, windData());
    }
}