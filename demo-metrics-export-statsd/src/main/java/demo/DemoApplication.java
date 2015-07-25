package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.statsd.StatsdMetricWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    @Bean
    @ExportMetricWriter
    StatsdMetricWriter metricWriter(@Value("${statsd.prefix:demo}") String prefix,
                                    @Value("${statsd.host:192.168.99.100}") String host,
                                    @Value("${statsd.port:8125}") int port) {
        return new StatsdMetricWriter(prefix, host, port);
    }

    @RequestMapping("/")
    String hello() {
        return "hello!";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
