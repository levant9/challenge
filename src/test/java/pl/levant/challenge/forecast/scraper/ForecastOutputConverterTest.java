package pl.levant.challenge.forecast.scraper;

import org.junit.jupiter.api.Test;
import pl.levant.challenge.forecast.scraper.client.ForecastOutput;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForecastOutputConverterTest {

    @Test
    void convert_multipleDaysData_correctlyConverted() {
        //given
        double longitude = 1.0;
        double latitude = 2.0;
        var forecastOutput = createMultipleDaysForecastOutput();
        //when
        var forecastFeed = ForecastOutputConverter.convert(longitude, latitude, forecastOutput);
        //then
        assertNotNull(forecastFeed);
        assertEquals(longitude, forecastFeed.longitude());
        assertEquals(latitude, forecastFeed.latitude());
        var forecasts = forecastFeed.forecasts();
        assertNotNull(forecasts);
        assertEquals(2, forecasts.size());

        var firstForecast = forecasts.get(0);
        assertEquals(LocalDate.of(2023, 11, 11), firstForecast.date());
        assertEquals(20, firstForecast.minTemperature());
        assertEquals(28, firstForecast.maxTemperature());
        assertEquals(14, firstForecast.windSpeed());
        assertEquals("SE", firstForecast.windDirection());

        var secondForecast = forecasts.get(1);
        assertEquals(LocalDate.of(2023, 11, 12), secondForecast.date());
        assertEquals(21, secondForecast.minTemperature());
        assertEquals(30, secondForecast.maxTemperature());
        assertEquals(14, secondForecast.windSpeed());
        assertEquals("N", secondForecast.windDirection());
    }

    private ForecastOutput createMultipleDaysForecastOutput() {
        // Arrange
        var init = "2023111100";
        var dataSeriesList = List.of(
                new ForecastOutput.DataSeries(3, 22, new ForecastOutput.WindData("SE", 15)),
                new ForecastOutput.DataSeries(6, 28, new ForecastOutput.WindData("NW", 20)),
                new ForecastOutput.DataSeries(9, 24, new ForecastOutput.WindData("S", 12)),
                new ForecastOutput.DataSeries(12, 26, new ForecastOutput.WindData("SE", 18)),
                new ForecastOutput.DataSeries(15, 23, new ForecastOutput.WindData("SW", 16)),
                new ForecastOutput.DataSeries(18, 27, new ForecastOutput.WindData("W", 14)),
                new ForecastOutput.DataSeries(21, 21, new ForecastOutput.WindData("E", 8)),
                new ForecastOutput.DataSeries(24, 20, new ForecastOutput.WindData("N", 10)),
                new ForecastOutput.DataSeries(27, 30, new ForecastOutput.WindData("N", 10)),
                new ForecastOutput.DataSeries(30, 25, new ForecastOutput.WindData("N", 10)),
                new ForecastOutput.DataSeries(33, 22, new ForecastOutput.WindData("NE", 15)),
                new ForecastOutput.DataSeries(36, 28, new ForecastOutput.WindData("NW", 20)),
                new ForecastOutput.DataSeries(39, 24, new ForecastOutput.WindData("S", 12)),
                new ForecastOutput.DataSeries(42, 26, new ForecastOutput.WindData("SE", 18)),
                new ForecastOutput.DataSeries(45, 23, new ForecastOutput.WindData("SW", 16)),
                new ForecastOutput.DataSeries(48, 21, new ForecastOutput.WindData("N", 16))
        );
        return new ForecastOutput(init, dataSeriesList);
    }
}
