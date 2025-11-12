package org.example.concurrentebenchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ConcurrenteBenchmarkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrenteBenchmarkApplication.class, args);
    }
}
