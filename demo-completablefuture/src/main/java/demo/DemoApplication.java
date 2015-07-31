package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableScheduling
@RestController
public class DemoApplication {
    final BlockingQueue<CompletableFuture<String>> q = new ArrayBlockingQueue<>(100);
    final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    @RequestMapping("/")
    CompletableFuture hello() {
        CompletableFuture<String> future = new CompletableFuture<>();
        q.add(future);
        return future;
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 1_000)
    void emit() throws IOException {
        for (int i = 0; i < 10; i++) {
            CompletableFuture<String> future = q.poll();
            if (future == null) return;
            log.info("emit");
            future.complete("Hello!");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
