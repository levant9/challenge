package pl.levant.challenge.forecast.weather.persistance;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WeatherRepository extends JpaRepository<ForecastRecord, ForecastId> {

    @Query(
            "SELECT f FROM ForecastRecord f WHERE f.id.date >= :date " +
                    "AND f.id.latitude = :latitude AND f.id.longitude = :longitude " +
                    "ORDER BY f.id.date ASC"
    )
    List<ForecastRecord> getForecasts(
            @Param("date") LocalDate date,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            Pageable pageable
    );

}
