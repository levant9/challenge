package pl.levant.challenge.forecast.weather.persistance;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private double latitude;
    private double longitude;
}
