package org.example.concurrentebenchmark.controller;

import org.example.concurrentebenchmark.service.BenchmarkService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/benchmark")
public class BenchmarkController {

    private final BenchmarkService service;
    private Map<String, Object> lastResult = new HashMap<>();

    public BenchmarkController(BenchmarkService service) {
        this.service = service;
    }

    @GetMapping("/modes")
    public List<Map<String, String>> getModes() {
        return List.of(
                Map.of("mode", "SEQUENTIAL", "description", "Ejecución secuencial en un solo hilo"),
                Map.of("mode", "EXECUTOR_SERVICE", "description", "Ejecución concurrente con ExecutorService manual"),
                Map.of("mode", "SPRING_ASYNC", "description", "Ejecución asíncrona con anotaciones @Async")
        );
    }

    @PostMapping("/start")
    public Map<String, Object> startBenchmark(
            @RequestParam(defaultValue = "50") int tasks,
            @RequestParam(defaultValue = "8") int threads) throws Exception {

        long t1 = service.runSequential(tasks);
        long t2 = service.runExecutorService(tasks, threads);
        long t3 = service.runSpringAsync(tasks);

        double speedup2 = (double)t1 / t2;
        double speedup3 = (double)t1 / t3;
        double eff2 = speedup2 / threads;
        double eff3 = speedup3 / threads;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalTasks", tasks);
        result.put("threadsUsed", threads);
        result.put("results", List.of(
                Map.of("mode", "SEQUENTIAL", "timeMs", t1, "speedup", 1.0, "efficiency", 1.0),
                Map.of("mode", "EXECUTOR_SERVICE", "timeMs", t2, "speedup", speedup2, "efficiency", eff2),
                Map.of("mode", "SPRING_ASYNC", "timeMs", t3, "speedup", speedup3, "efficiency", eff3)
        ));

        lastResult = result;
        return result;
    }

    @GetMapping("/result")
    public Map<String, Object> getLastResult() {
        return lastResult;
    }
}
