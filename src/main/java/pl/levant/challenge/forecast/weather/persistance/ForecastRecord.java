package pl.levant.challenge.forecast.weather.persistance;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import pl.levant.challenge.forecast.weather.model.Forecast;

import java.util.Objects;

@Entity
@Table(name = "forecast")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
public class ForecastRecord {

    @EmbeddedId
    private ForecastId id;
    private int minTemperature;
    private int maxTemperature;
    private int windSpeed;
    private String windDirection;

    public Forecast toDomain() {
        log.debug("Converting to domain {}", this);
        return new Forecast(
                id.getDate(),
                minTemperature,
                maxTemperature,
                windSpeed,
                windDirection
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ForecastRecord that = (ForecastRecord) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
