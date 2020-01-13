package demo.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureSquareCalculator {

    public static ExecutorService es = Executors.newSingleThreadExecutor();

    public static Future<Integer> calculate(Integer input) throws InterruptedException {
        Thread.sleep(1000);
        return es.submit(() -> input * input);
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Future<Integer> future = calculate(2);

        while (!future.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }

        System.out.println(future.get());

        es.shutdown();  //  stop and kill
    }
}
