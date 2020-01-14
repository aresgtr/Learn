package io.github.aresgtr;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * https://www.baeldung.com/java-future
 * "Simply put, the Future class represents a future result of an asynchronous computation – a result that will eventually appear in the Future after the processing is complete."
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
	// write your code here
        Future<Integer> future = new SquareCalculator().calculate(10);

        while (!future.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }

        Integer result = future.get(500, TimeUnit.MILLISECONDS);
        System.out.println(result);

        Future<Integer> future2 = new SquareCalculator().calculate(4);

        boolean canceled = future2.cancel(true);
        System.out.println(canceled);

    }
}
