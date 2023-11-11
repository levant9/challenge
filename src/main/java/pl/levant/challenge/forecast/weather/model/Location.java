package pl.levant.challenge.forecast.weather.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Location {
    Stavanger("Stavanger", 58.97, 5.73);

    private final String name;
    private final double latitude;

    private final double longitude;
}
