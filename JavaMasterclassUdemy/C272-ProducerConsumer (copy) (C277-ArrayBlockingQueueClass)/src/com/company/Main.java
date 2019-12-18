package com.company;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    static final String EOF = "EOF";

    public static void main(String[] args) {
	// write your code here
        ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<>(6);

        //  Executive Service
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        MyProducer producer = new MyProducer(buffer, ThreadColor.ANSI_YELLOW);
        MyConsumer consumer1 = new MyConsumer(buffer, ThreadColor.ANSI_PURPLE);
        MyConsumer consumer2 = new MyConsumer(buffer, ThreadColor.ANSI_CYAN);

//        new Thread(producer).start();
//        new Thread(consumer1).start();
//        new Thread(consumer2).start();

        executorService.execute(producer);
        executorService.execute(consumer1);
        executorService.execute(consumer2); //  does not shutdown automatically

        //  Callable Class
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(ThreadColor.ANSI_WHITE + "I'm being printed for the Callable Class");
                return "This is the callable result";
            }
        });

        try {
            System.out.println(ThreadColor.ANSI_WHITE + future.get());
        } catch (InterruptedException e) {
            System.out.println("Thread running the task was interrupted");
        } catch (ExecutionException e) {
            System.out.println("Something went wrong");
        }

        executorService.shutdown();
    }
}
