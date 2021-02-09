package com.multithreading.countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class
CountdownLatchTest {
    public static void main(String[] args) {

        /**
         * When we want multi tasking (multi threading), there, in case of a situation where we need to start a particular task only after certain pre-requisite
         * tasks are completed, then we can put that main task in wait using await() method. and can use coundown() method for all pre-requisite task at the required point
         * So, once all those pre-requisite tasks are reached that countdown() statement, then the task which is waiting for those prerequisite tasks will start its process
         *
         * In this case, the Main thread/task is the waiter. It is waiting for three chunks of prerequisite tasks names Task1, Task2, Task3. Each of those prerequisite tasks
         * should have countdown method where required. Once they all reached that point, then the countdown will become Zero(0) from the count mentioned
         * in CountDownLatch(int count) constructor
         */

        CountDownLatch countDownLatch = new CountDownLatch(3);
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        //executorService.execute(new Task4(countDownLatch));
        executorService.execute(new Task1(countDownLatch));
        executorService.execute(new Task2(countDownLatch));
        executorService.execute(new Task3(countDownLatch));

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Shutting down the executor services once its usage is over
        executorService.shutdown();

        // Remaining Main thread execution lines
        System.out.println("Main Thread : " + Thread.currentThread().getName() + " thread continues once all above assigned tasks (DB Initialization, Connecting to HTTP Server," +
                "Reading external source file) are reached its countdown point... with the help of Countdownlatch");
        Stream.iterate(1, i -> i + 2).limit(20).filter(e -> e % 3 == 2).forEach(System.out::println);

    }
}

// Create few [here, three] tasks classes implementing Runnable interface
class Task1 implements Runnable {

    private CountDownLatch countDownLatch;

    public Task1(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("Task 1 : " + Thread.currentThread().getName() + " Arrived... But yet to complete DB initialization."); // Just for demo

        try {
            // Let us assume that DB initialization takes 5 Seconds. we demonstrate it using sleep method
            System.out.println("Task 1 : " + Thread.currentThread().getName() + " - DB initialization is in progress");
            Thread.sleep(5000);
            System.out.println("Task 1 : Completed - DB initialization is completed");
            // Once DB initialization is completed in 5 seconds, then count it down.
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Below part will be executed continuously
        System.out.println("Task 1 : " + Thread.currentThread().getName() + " continues to do its further execution after counting it down - " + new Random().nextInt());
    }
}

class Task2 implements Runnable {

    private CountDownLatch countDownLatch;

    public Task2(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("Task 2 : " + Thread.currentThread().getName() + " Arrived... But yet to Connect to server and get the response."); // Just for demo

        try {
            // Let us assume that HTTP Server connection takes 15 Seconds. we demonstrate it using sleep method
            System.out.println("Task 2 : " + Thread.currentThread().getName() + " - Connecting to HTTPS Server");
            Thread.sleep(15000);
            System.out.println("Task 2 : Completed - Connection to HTTP Server is established and got the response");
            // Once HTTP Server is connected and response is received in 15 seconds, then count it down.
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Below part will be executed continuously
        System.out.println("Task 2 : " + Thread.currentThread().getName() + " continues to do its further execution after counting it down - " + new Random().nextDouble());
    }
}

class Task3 implements Runnable {

    private CountDownLatch countDownLatch;

    public Task3(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("Task 3 : " + Thread.currentThread().getName() + " Arrived... But yet to collect data from external source"); // Just for demo

        try {
            // Let us assume that HTTP Server connection takes 9 Seconds. we demonstrate it using sleep method
            System.out.println("Task 3 : " + Thread.currentThread().getName() + " - Connecting to external data source");
            Thread.sleep(8000);
            System.out.println("Task 3 : Completed - Connected to external resource and collected the data");
            // Once an external resource is connected and collected the information in 8 seconds, then count it down.
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Below part will be executed continuously
        System.out.println("Task 3 : " + Thread.currentThread().getName() + " continues to do its further execution after counting it down - " + new Random().nextLong());
    }

}

/**
 * We can even make another task [not necessary that always Main thread should wait for other threads] to wait for all
 * prerequisite tasks to get completed.
 * <p>
 * Just mention countDownLatch.await(); to this task. Very simple.
 */
class Task4 implements Runnable {

    private CountDownLatch countDownLatch;

    public Task4(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println("Task 4 : " + Thread.currentThread().getName() + " is waiting for Task 1, Task 2 and Task 3"); // Just for demo
            countDownLatch.await();
            System.out.println("Task 4 : " + Thread.currentThread().getName() + " is running now as Prerequisite tasks namely Task 1, Task 2 and Task 3 are completed its tasks");

            System.out.println("Task 4 : " + Thread.currentThread().getName() + " - uses DB Connection continuously");
            System.out.println("Task 4 : " + Thread.currentThread().getName() + " - uses the response from HTTP server");
            System.out.println("Task 4 : " + Thread.currentThread().getName() + " - uses the external resource data for further processting");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Below part will be executed continuously
        System.out.println("Task 4 : " + Thread.currentThread().getName() + " continues...");
    }

}