package com.vigi.async;

import com.vigi.Quote;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by vigi on 2/21/2015.
 */
public class Shop {

    private static final List<Shop> shops = Arrays.asList(new Shop("Hodor"),
            new Shop("Tyrion"),
            new Shop("Jon Snow"),
            new Shop("Daenerys"),
            new Shop("Tywin"),
            new Shop("Jaime"));

    private final String name;
    private final Random random = new Random();
    private static final Executor executor = executor();

    public Shop(String name) {
        this.name = name;
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[
                random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    public double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> findPrices(String product) {
        Function<Shop, CompletableFuture<String>> asyncPrice = shop -> CompletableFuture.supplyAsync(
                () -> shop.getPrice(product), executor
        );
        // pipeline two asynchronous operations, passing the result of the first
        // operation to the second operation when it becomes available
        Function<CompletableFuture<Quote>, CompletableFuture<String>> asyncDiscount = future -> future.thenCompose(quote ->
                CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), executor
                ));
        List<CompletableFuture<String>> priceFuture = shops.stream()
                .map(asyncPrice)
                .map(future -> future.thenApply(Quote::parse))
                // future composition is applied here
                .map(asyncDiscount)
                .collect(toList());
        return priceFuture.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    private static Executor executor() {
        return Executors.newFixedThreadPool(Math.min(100, shops.size()),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                });
    }

    private static void invokeAsync() {
        Shop shop = new Shop("hodor?");
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
                .map(String::valueOf);
        stream.limit(1000)
                .filter(s -> s.length() > 3)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        System.out.println("Prices: " + findPrices("Hodor"));
        long end = (System.nanoTime() - start) / 1_000_000;
        System.out.println("It took: " + end + " millis.");
    }
}
