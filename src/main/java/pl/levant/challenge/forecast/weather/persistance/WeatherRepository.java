package pl.levant.challenge.forecast.weather.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherRepository extends JpaRepository<ForecastRecord, ForecastId> {

    @Query(
            "SELECT f from ForecastRecord f"
    )
    List<ForecastRecord> getForecasts(int days, double latitude, double longitude);

}
