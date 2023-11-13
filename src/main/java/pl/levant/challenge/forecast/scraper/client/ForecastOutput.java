package pl.levant.challenge.forecast.scraper.client;

import java.util.List;

public record ForecastOutput(
        String init,
        List<DataSeries> dataseries
) {
    public record DataSeries(
            int timepoint,
            int temp2m,
            WindData wind10m
    ) {
    }

    public record WindData(
            String direction,
            int speed
    ) {
    }
}
