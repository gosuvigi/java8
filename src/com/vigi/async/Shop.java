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
    private static final Random random = new Random();
    private static final Executor executor = executor();

    private final String name;

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
        int delay = 500 + random.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> findPrices(String product) {
        List<CompletableFuture<String>> priceFuture = findPricesStream(product)
                .collect(toList());
        return priceFuture.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public static Stream<CompletableFuture<String>> findPricesStream(String product) {
        Function<Shop, CompletableFuture<String>> asyncPrice = shop -> CompletableFuture.supplyAsync(
                () -> shop.getPrice(product), executor
        );
        // pipeline two asynchronous operations, passing the result of the first
        // operation to the second operation when it becomes available
        Function<CompletableFuture<Quote>, CompletableFuture<String>> asyncDiscount = f -> f.thenCompose(
                quote -> CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), executor
                ));
        return shops.stream()
                .map(asyncPrice)
                .map(f -> f.thenApply(Quote::parse))
                .map(asyncDiscount);
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
        CompletableFuture[] futures = findPricesStream("Hodor")
                .map(f -> f.thenAccept(s -> System.out.println(s + " (done in " +
                        ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
                .toArray(size -> new CompletableFuture[size]);
        CompletableFuture.allOf(futures).join();
        System.out.println("All shops have now responded in "
                + ((System.nanoTime() - start) / 1_000_000) + " msecs");
    }
}
