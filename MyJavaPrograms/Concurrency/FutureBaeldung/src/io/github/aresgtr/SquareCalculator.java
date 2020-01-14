package io.github.aresgtr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SquareCalculator {

//    public ExecutorService executor = Executors.newSingleThreadExecutor();
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public Future<Integer> calculate(Integer input) throws InterruptedException {
        System.out.println("Calculating square for: " + input);
        Thread.sleep(1000);
        return executor.submit(() -> input * input);

    }

    public void shutdown() {
        executor.shutdown();
    }
}
