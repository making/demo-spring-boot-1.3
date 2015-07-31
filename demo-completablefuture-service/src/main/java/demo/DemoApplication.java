package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableAsync
@RestController
public class DemoApplication {
    @Autowired
    WeatherService weatherService;

    @RequestMapping("/")
    CompletableFuture<String> hello(@RequestParam Optional<String> where) {
        return weatherService.getWeather(where.orElse("Tokyo"))
                .exceptionally(e -> "Error! " + e.getMessage());
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Service
class WeatherService {
    final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    @Autowired
    RestTemplate restTemplate;

    @Async
    @SuppressWarnings("unchecked")
    public CompletableFuture<String> getWeather(String where) {
        logger.info("get weather @ {}", where);
        Map<String, Object> result = (Map<String, Object>) restTemplate.getForObject("http://api.openweathermap.org/data/2.5/weather?q=" + where, Map.class);
        Double temperature = (Double) ((Map<String, Object>) result.get("main")).get("temp") - 273;
        return CompletableFuture.completedFuture("The current temperature " + temperature + " degrees.");
    }
}