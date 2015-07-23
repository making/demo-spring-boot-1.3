package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.opentsdb.OpenTsdbMetricWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    @Bean
    @ExportMetricWriter
    @ConfigurationProperties("metrics.export")
    OpenTsdbMetricWriter metricWriter() {
        return new OpenTsdbMetricWriter();
    }

    @RequestMapping("/")
    String hello() {
        return "hello!";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
