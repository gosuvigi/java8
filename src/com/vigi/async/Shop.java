package com.vigi.async;

import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by vigi on 2/21/2015.
 */
public class Shop {

    public double getPrice(String product) {
        delay();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = getPrice(product);
                futurePrice.complete(price);
            } catch (Exception e) {
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;
    }

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void invokeAsync() {
        Shop shop = new Shop();
        long start = System.nanoTime();
        Future<Double> price = shop.getPriceAsync("Hodor");
        long end = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Invocation returned after: " + end + " millis.");

        doSomethingElse();
        try {
            price.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        end = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned returned after: " + end + " millis.");
    }

    private static void doSomethingElse() {
        Stream<String> stream = IntStream.rangeClosed(1, 1_000_000)
                .boxed()
                .map(i -> String.valueOf(i));
        stream.limit(1000)
                .filter(s -> s.length() > 3)
                .forEach(i -> System.out.println(i));
    }

    public static void main(String[] args) {
        invokeAsync();
    }
}
