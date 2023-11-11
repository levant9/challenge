package pl.levant.challenge.forecast.scraper.client;

public interface SevenTimerClient {

    ForecastOutput readForecastForEightDays(double longitude, double latitude);
}
