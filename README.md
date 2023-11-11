# Weather forecast provided app


## Forecast obtaining
http://www.7timer.info/doc.php?lang=en

Data must be fetched when the project is initialized and every 1 minute after.

## Output specification
7 days forecast.

For each day it returns the week day, min and max temperature, wind speed, and wind direction

Stavanger, Norway
Latitude and longitude coordinates are: 58.969975, 5.733107.


## How to run locally
Spin PostgreSQL database from docker-compose.yml file (create and start container) before starting SpringBoot application
(e.g. by `./gradlew bootRun`)