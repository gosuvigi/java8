package com.vigi.streams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.partitioningBy;

/**
 * Created by vigi on 2/12/2015.
 */
public class Partitioning {

    private static boolean isPrime(int n) {
        int squareRoot = (int) Math.sqrt(n);
        return IntStream.rangeClosed(2, squareRoot)
                .noneMatch(i -> n % i == 0);
    }

    private static void partitionPrimes(int n) {
        Map<Boolean, List<Integer>> primes = IntStream.rangeClosed(2, n).boxed()
                .collect(partitioningBy(candidate -> isPrime(candidate)));
        System.out.println("--- primes: " + primes);
    }

    public static void main(String[] args) {
        partitionPrimes(200);
    }
}
