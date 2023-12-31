package pl.levant.challenge.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public WireMockServer wireMockServer() {
        var wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8099));
        wireMockServer.start();
        return wireMockServer;
    }
}

