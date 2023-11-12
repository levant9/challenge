package pl.levant.challenge.forecast.scraper;

import lombok.extern.slf4j.Slf4j;
import pl.levant.challenge.forecast.scraper.client.ForecastOutput;
import pl.levant.challenge.forecast.weather.model.Forecast;
import pl.levant.challenge.forecast.weather.model.ForecastFeed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
class ForecastOutputConverter {

    static ForecastFeed convert(double longitude, double latitude, ForecastOutput forecastOutput) {
        if (forecastOutput == null) {
            throwValidationException();
        }
        var initLocalDateTime = convertInitDate(forecastOutput.init());
        if (forecastOutput.dataseries() == null || forecastOutput.dataseries().isEmpty()) {
            throwValidationException();
        }
        int timeSpan = forecastOutput.dataseries().get(0).timepoint();
        var forecasts = computeForecastPerDay(forecastOutput.dataseries(), initLocalDateTime, timeSpan);
        return new ForecastFeed(longitude, latitude, forecasts);
    }

    private static LocalDateTime convertInitDate(String init) {
        if (init == null) {
            throwValidationException();
        }
        var formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        return LocalDateTime.parse(init, formatter);
    }

    private static List<Forecast> computeForecastPerDay(
            List<ForecastOutput.DataSeries> dataseries,
            LocalDateTime initLocalDateTime,
            int timeSpan
    ) {
        var initOffset = initLocalDateTime.getHour();
        Map<LocalDate, List<ForecastOutput.DataSeries>> dataSeriesPerDay = new LinkedHashMap<>();
        int timeSum = 0;
        List<ForecastOutput.DataSeries> periods = new ArrayList<>();
        var date = initLocalDateTime.toLocalDate();
        for (ForecastOutput.DataSeries forecastPerPeriod : dataseries) {
            periods.add(forecastPerPeriod);
            if ((initOffset + forecastPerPeriod.timepoint() - timeSum) % 24 == 0) {
                dataSeriesPerDay.computeIfAbsent(
                                date,
                                k -> new ArrayList<>())
                        .addAll(periods);
                if (timeSum == 0) {
                    timeSum = initOffset;
                }
                timeSum = timeSum + periods.size() * timeSpan;
                periods.clear();
                date = date.plusDays(1);
            }
        }
        return dataSeriesPerDay.entrySet().stream()
                .map(entry -> {
                    var forecasts = entry.getValue();
                    int maxTemperature = forecasts.stream()
                            .mapToInt(ForecastOutput.DataSeries::temp2m)
                            .max()
                            .orElse(0);
                    int minTemperature = forecasts.stream()
                            .mapToInt(ForecastOutput.DataSeries::temp2m)
                            .min()
                            .orElse(0);
                    var avgWindSpeed = (int) forecasts.stream()
                            .map(ForecastOutput.DataSeries::wind10m)
                            .mapToInt(ForecastOutput.WindData::speed)
                            .average()
                            .orElse(0);
                    var windDirectionCounts = forecasts.stream()
                            .map(ForecastOutput.DataSeries::wind10m)
                            .map(ForecastOutput.WindData::direction)
                            .collect(Collectors.groupingBy(windDirection -> windDirection, Collectors.counting()));
                    var mostFrequentWindDirection = windDirectionCounts.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Unknown");

                    return new Forecast(entry.getKey(), minTemperature, maxTemperature, avgWindSpeed, mostFrequentWindDirection);
                })
                .toList();
    }

    private static void throwValidationException() {
        log.error("Wrong forecast output received");
        throw new IllegalStateException("Wrong forecast output received");
    }
}
