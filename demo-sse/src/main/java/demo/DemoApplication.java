package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@RestController
public class DemoApplication {
    final BlockingQueue<EmitterAndCount> emitters = new ArrayBlockingQueue<>(10);
    final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    @RequestMapping("/hello")
    SseEmitter hello() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(new EmitterAndCount(emitter));
        return emitter;
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 1_000)
    void emit() throws IOException {
        List<EmitterAndCount> keeps = new ArrayList<>();
        while (!emitters.isEmpty()) {
            EmitterAndCount e = emitters.poll();
            log.info("send " + e.count);
            e.emitter.send("hello " + e.count);
            if (e.count.getAndIncrement() < 10) {
                keeps.add(e);
            } else {
                e.emitter.complete();
            }
        }
        // keep
        emitters.addAll(keeps);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

class EmitterAndCount {
    final SseEmitter emitter;
    final AtomicInteger count = new AtomicInteger(1);

    EmitterAndCount(SseEmitter emitter) {
        this.emitter = emitter;
    }
}
