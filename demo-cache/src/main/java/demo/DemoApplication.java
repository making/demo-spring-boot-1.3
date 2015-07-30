package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@EnableCaching
@SpringBootApplication
@RestController
public class DemoApplication {
    @Autowired
    WeatherService weatherService;

    @RequestMapping
    String hello(@RequestParam Optional<String> where) {
        long start = System.currentTimeMillis();
        String result = weatherService.getWeather(where.orElse("Tokyo"));
        long elapsed = System.currentTimeMillis() - start;
        return result + " took " + elapsed + " [ms]";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@CacheConfig(cacheNames = "weather")
@Service
class WeatherService {
    @Autowired
    RestTemplate restTemplate;

    @Cacheable
    @SuppressWarnings("unchecked")
    public String getWeather(String where) {
        Map<String, Object> result = (Map<String, Object>) restTemplate.getForObject("http://api.openweathermap.org/data/2.5/weather?q=" + where, Map.class);
        Double temperature = (Double) ((Map<String, Object>) result.get("main")).get("temp") - 273;
        Double wind = (Double) ((Map<String, Object>) result.get("wind")).get("speed") * 3.6;
        return "The current temperature " + temperature + " degrees and the wind is " + wind + " km/h. (" + LocalDateTime.now() + ")";
    }
}