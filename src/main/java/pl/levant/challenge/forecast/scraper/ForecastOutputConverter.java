package pl.levant.challenge.forecast.scraper;

import lombok.extern.slf4j.Slf4j;
import pl.levant.challenge.forecast.scraper.client.ForecastOutput;
import pl.levant.challenge.forecast.weather.model.ForecastFeed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
class ForecastOutputConverter {

    static ForecastFeed convert(double longitude, double latitude, ForecastOutput forecastOutput) {
        if (forecastOutput == null) {
            throwValidationException();
        }
        var initLocalDate = convertInitDate(forecastOutput.init());
        if (forecastOutput.dataseries() == null || forecastOutput.dataseries().isEmpty()) {
            throwValidationException();
        }
        var entriesPerDay = calculateNumberOfEntriesPerDay(forecastOutput.dataseries().get(0).timepoint());
        //FIXME
        return new ForecastFeed(longitude, latitude, List.of());
    }

    private static int calculateNumberOfEntriesPerDay(int timepoint) {
        return 24 / timepoint;
    }

    private static LocalDate convertInitDate(String init) {
        if (init == null) {
            throwValidationException();
        }
        var formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        return LocalDate.parse(init, formatter);
    }

    private static void throwValidationException() {
        log.error("Wrong forecast output received");
        throw new IllegalStateException("Wrong forecast output received");
    }
}
