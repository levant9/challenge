package pl.levant.challenge.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.levant.challenge.forecast.weather.WeatherComponent;
import pl.levant.challenge.forecast.weather.model.Location;
import pl.levant.challenge.forecast.weather.rest.ForecastRestResponse;

import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ChallengeApplicationTests {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WireMockServer wireMockServer;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    WeatherComponent weatherComponent;

    @Test
    void givenProperForecastFeedFrom7Time_whenFeedingForecastsAndGettingWeatherForStavanger_thenReceived7DaysForecast() {
        //given - simulate that historical data is present
        wireMockServer.stubFor(get(anyUrl()).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBodyFile("full_historical_response_7time.json"))
        );
        weatherComponent.feedForecast(); //feed from 2023110106
        //when
        wireMockServer.stubFor(get(anyUrl()).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBodyFile("full_actual_response_7time.json"))
        );
        weatherComponent.feedForecast(); //feed from 2023111306
        //and
        var getWeather = RequestEntity.get("http://localhost:" + serverPort + "/weather-forecasts/stavanger").build();
        var response = restTemplate.exchange(getWeather, ForecastRestResponse.class);
        //then
        var expectedForecasts = List.of(
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-13"), "MONDAY", 5, 7, 3, "E"),
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-14"), "TUESDAY", -1, 4, 2, "SE"),
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-15"), "WEDNESDAY", -1, 5, 3, "S"),
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-16"), "THURSDAY", -1, 5, 3, "SE"),
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-17"), "FRIDAY", -3, 2, 2, "SE"),
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-18"), "SATURDAY", -2, 7, 3, "SE"),
                new ForecastRestResponse.ForecastRest(LocalDate.parse("2023-11-19"), "SUNDAY", 5, 8, 3, "SE")
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(new ForecastRestResponse(
                Location.Stavanger.getLatitude(),
                Location.Stavanger.getLongitude(),
                expectedForecasts
        ));
    }

    // complex test with forecasts feeds that are obtained from different days and checking weather on different days
    // requires mocking and changing java.time.Clock

    @Test
    void givenProblemWith7Time_whenFeedingForecasts_thenExceptionIsRaised() {
        //given
        wireMockServer.stubFor(get(anyUrl()).willReturn(
                aResponse()
                        .withStatus(503)
                        .withBody("{}"))
        );
        assertThatThrownBy(() -> weatherComponent.feedForecast())
                .hasMessage("Retries exhausted: 3/3");
    }

    @Test
    void givenIncorrectResponseFrom7Time_whenFeedingForecasts_thenExceptionIsRaised() {
        //given
        wireMockServer.stubFor(get(anyUrl()).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody("not-json"))
        );
        assertThatThrownBy(() -> weatherComponent.feedForecast())
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to parse response from 7time");
    }

    @AfterEach
    public void verify() {
        wireMockServer.checkForUnmatchedRequests();
    }

}
