package org.example.concurrentebenchmark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class BenchmarkService {

    private final Executor asyncExecutor;

    @Autowired
    public BenchmarkService(Executor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    // Simula una tarea intensiva de CPU
    private void computeTask() {
        long x = 0;
        for (int i = 0; i < 1_000_000; i++) {
            x += Math.sqrt(i);
        }
    }

    // Ejecución secuencial
    public long runSequential(int tasks) {
        long start = System.nanoTime();
        for (int i = 0; i < tasks; i++) computeTask();
        return (System.nanoTime() - start) / 1_000_000;
    }

    // Ejecución concurrente manual
    public long runExecutorService(int tasks, int threads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<?>> futures = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < tasks; i++) {
            futures.add(executor.submit(this::computeTask));
        }
        for (Future<?> f : futures) {
            try { f.get(); } catch (ExecutionException ignored) {}
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
        return (System.nanoTime() - start) / 1_000_000;
    }

    // Ejecución asíncrona con Spring
    @Async("benchmarkExecutor")
    public CompletableFuture<Void> runAsyncTask() {
        computeTask();
        return CompletableFuture.completedFuture(null);
    }

    public long runSpringAsync(int tasks) throws Exception {
        long start = System.nanoTime();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            futures.add(runAsyncTask());
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        return (System.nanoTime() - start) / 1_000_000;
    }
}
