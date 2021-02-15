package com.multithreading.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CyclicBarrierTest {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        executorService.execute(new Task1(cyclicBarrier));
        executorService.execute(new Task2(cyclicBarrier));
        executorService.execute(new Task3(cyclicBarrier));

        // Main thread will continue to execute the below lines once the above three threads are just initiated
        System.out.println("Main Thread : " + Thread.currentThread().getName() + " thread continues once all above assigned tasks are just started... with the help of CyclicBarrier");
        Stream.iterate(1, i -> i + 2).limit(20).filter(e -> e % 3 == 2).forEach(System.out::println);

        boolean flag = true;
        int i = 0;

        try {
            while(flag) {
                i += 1;
                System.out.println("Main : " + i);
                Thread.sleep(100);
                if(i == 150){
                    break;
                }
            }
        } catch (Exception e) {
        }

        executorService.shutdown();
    }
}

class CommonClass {
    public static void sendMessage(int number, String taskName) {
        System.out.println("Sending Message for Task " + taskName + " at its cycle " + number);
    }
}

class Task1 implements Runnable {

    private CyclicBarrier cyclicBarrier;

    public Task1(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        boolean flag = true;
        int i = 0;

        try {
            while(flag) {
                i += 1;
                System.out.println("Task 1: ------- " + i);
                Thread.sleep(1000);
                if(i % 6 == 0){
                    System.out.println("Task 1: " + Thread.currentThread().getName() + " - " + i + "waits for other threads to reach a certain point to send the message at the same time");
                    cyclicBarrier.await();
                    CommonClass.sendMessage(i, this.getClass().getName());
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
        }
    }
}

class Task2 implements Runnable {

    private CyclicBarrier cyclicBarrier;

    public Task2(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        boolean flag = true;
        int i = 0;

        try {
            while(flag) {
                i += 1;
                System.out.println("Task 2: ------- " + i);
                Thread.sleep(1000);
                if(i % 10 == 0){
                    System.out.println("Task 2: " + Thread.currentThread().getName() + " - " + i + "waits for other threads to reach a certain point to send the message at the same time");
                    cyclicBarrier.await();
                    CommonClass.sendMessage(i, this.getClass().getName());
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
        }
    }
}

class Task3 implements Runnable {

    private CyclicBarrier cyclicBarrier;

    public Task3(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        boolean flag = true;
        int i = 0;

        try {
            while(flag) {
                i += 1;
                System.out.println("Task 3: ------- " + i);
                Thread.sleep(1000);
                if(i % 8 == 0){
                    System.out.println("Task 3: " + Thread.currentThread().getName() + " - " + i + "waits for other threads to reach a certain point to send the message at the same time");
                    cyclicBarrier.await();
                    CommonClass.sendMessage(i, this.getClass().getName());
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
        }
    }
}